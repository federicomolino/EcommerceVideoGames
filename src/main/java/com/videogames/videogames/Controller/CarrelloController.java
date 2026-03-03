package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.*;
import com.videogames.videogames.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/carrello")
public class CarrelloController extends HelpUtente {

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping()
    public String showCarrello(Model model, Principal principal){
        Optional<Utente> u = carrelloService.recuperoUtente(principal);
        Utente user = u.get();
        List<CarrelloGioco> carrello = carrelloGiocoRepository.findByUtente(u);
        Carrello carr = carrelloRepository.findByUtente(user);
        BigDecimal prezzoFinaleScontato = carr.getPrezzoFinaleSconto();

        if (prezzoFinaleScontato == null || prezzoFinaleScontato.compareTo(BigDecimal.ZERO) == 0){
            model.addAttribute("prezzoTotale", carr.getPrezzoFinale());
        }else {
            model.addAttribute("prezzoTotale", carr.getPrezzoFinaleSconto());
            model.addAttribute("scontoApplicato", true);
        }

        model.addAttribute("listCarrello", carrello);
        model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
        return "Carrello/carrello";
    }

    @PostMapping("/add/{id}")
    public String giocoAlCarrello(@PathVariable("id") Integer id, Principal principal,
                                  @RequestParam(value = "piattaformaId", required = false) Integer piattaformaId,
                                  RedirectAttributes redirectAttributes){
        //utente guest
        if(principal.getName().equalsIgnoreCase("guest")){
            redirectAttributes.addFlashAttribute("erroreCarrello", "Per poter procedere" +
                    " è necessaria la registrazione" +
                    "<a href='/login/register' class='alert-link'> Clicca qui per procedere</a>");
            return "redirect:/";
        }
        Integer idPiattaforma = Optional.ofNullable(piattaformaId).orElse(0);
        try {
            carrelloService.AddCarrelloGioco(id, idPiattaforma, GetUtente());
        }catch (NessunGiocoTrovato e){
            redirectAttributes.addFlashAttribute("errorMessage", "Gioco esaurito");
            return "redirect:/gioco/infoGame/" + id;
        }catch (NessunaPiattaformaPresente ex){
            redirectAttributes.addFlashAttribute("errorMessage", ex);
            return "redirect:/gioco/infoGame/" + id;
        }

        return "redirect:/carrello";
    }

    @PostMapping("/delete/{id}")
    public String cancellaGiocoCarrello(@PathVariable("id") Integer id){
        carrelloService.cancellaGiocoCarrello(id, GetUtente());
        return "redirect:/carrello";
    }

    @PostMapping("/aumenta")
    @ResponseBody
    public Map<String, Objects> aumentaQuantita(@RequestBody Map<String, Integer> body) throws Exception {
        int nuovaQuantita = body.get("nuovaQuantita");
        int idCarrello = body.get("idCarrello");
        try{
            carrelloService.modificaQuantitaCarrello(nuovaQuantita, idCarrello, GetUtente());
            carrelloService.prezzoFinaleCarrello( 0, GetUtente());
        }catch (Exception e){
            throw new Exception(e);
        }
        return null;
    }

    @PostMapping("/diminuisci")
    @ResponseBody
    public Map<String, Objects> diminuisciQuantita(@RequestBody Map<String, Integer> body) throws Exception {
        int nuovaQuantita = body.get("nuovaQuantita");
        int idCarrello = body.get("idCarrello");
        try{
            carrelloService.modificaQuantitaCarrello(nuovaQuantita, idCarrello, GetUtente());
            carrelloService.prezzoFinaleCarrello( 0, GetUtente());
        }catch (Exception e){
            throw new Exception(e);
        }
        return null;
    }

    //Sconto per codice promozionale inserito
    @PostMapping("/codicePromozionale")
    public String scountCodicePromozionale(@ModelAttribute("formAddCodicePromozionale") CodiciPromozionale codiciPromozionale,
                                           RedirectAttributes redirectAttributes, Model model){
        List<Gioco> giochi = giocoRepository.findAll();
        List<CodiciPromozionale> codiciPromozionaliPresenti = codicePromozionaleRepository.findAll();
        Utente u = GetUtente();
        List<CarrelloGioco> carrello = carrelloGiocoRepository.findByUtente(u);

        if (carrello.isEmpty()){
            redirectAttributes.addFlashAttribute("codicePromozionale",
                    "Carrello Vuoto");
            return "redirect:/carrello";
        }
        if (codiciPromozionale.getCodicePromozionale().isEmpty()){
            redirectAttributes.addFlashAttribute("codicePromozionale",
                    "Il codice non può essere vuoto");
            return "redirect:/carrello";
        }

        //Verifico se il codice esiste
        boolean codiceTrovato = false;

        for (int i = 0; i < codiciPromozionaliPresenti.size(); i ++){
            if (codiciPromozionaliPresenti.get(i).getCodicePromozionale()
                    .equals(codiciPromozionale.getCodicePromozionale())){
                codiceTrovato = true;
                break;
            }
        }
        if (!codiceTrovato){
            redirectAttributes.addFlashAttribute("codicePromozionale",
                    "Codice non valido");
            return "redirect:/carrello";
        }


        for (CodiciPromozionale codice : codiciPromozionaliPresenti){
            if (codice.getCodicePromozionale().equals(codiciPromozionale.getCodicePromozionale())){
                if (codice.isUsato()){
                    redirectAttributes.addFlashAttribute("codicePromozionale",
                            "Codice già utilizzato");
                    return "redirect:/carrello";
                }
                int idGioco = codice.getGioco().getIdGioco();

                //Verifico se il gioco è nel carello
                for (CarrelloGioco c : carrello){
                    if (c.getGioco().getIdGioco()==idGioco){
                        //Se lo sconto che si vuole applicare è maggiore del costo del
                        //gioco vado in errore
                        boolean scontoValido = carrelloService.VerificaPrezzoConSconto(c.getCarrello().getId_carrello(),
                                codice.getValoreCodicePromozionale());
                        if (!scontoValido){
                            redirectAttributes.addFlashAttribute("codicePromozionale",
                                    "Codice inserito non valido in quanto lo sconto è inferiore al prezzo" +
                                            " del gioco, sconto applicabile dal totale %d" .formatted(codice.getValoreCodicePromozionale() + 1));
                            model.addAttribute("listCarrello", carrello);
                            model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
                            return "redirect:/carrello";
                        }
                        //Applico sconto
                        for (Gioco g : giochi){
                            if (g.getIdGioco() == idGioco){
                                codice.setUsato(true);
                                codicePromozionaleRepository.save(codice);
                                double prezzo = carrelloService.prezzoFinaleCarrello(codice.getValoreCodicePromozionale(), GetUtente());
                                //Trasformo il prezzo in modo che abbia due decimali
                                DecimalFormat priceDecimal = new DecimalFormat("#.##");
                                String priceDecimalFormatter = priceDecimal.format(prezzo);
                                model.addAttribute("prezzoTotale", priceDecimalFormatter);
                                model.addAttribute("listCarrello", carrello);
                                model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
                                return "redirect:/carrello";
                            }
                        }
                    }else{
                        redirectAttributes.addFlashAttribute("codicePromozionale",
                                "Codice inserito non valido per il gioco presente nel carrello, " +
                                        "codice valido per: " + c.getGioco().getDescrizione().toUpperCase());
                        model.addAttribute("listCarrello", carrello);
                        model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
                        return "redirect:/carrello";
                    }
                }
            }
        }
        return "Carrello/carrello";
    }
}
