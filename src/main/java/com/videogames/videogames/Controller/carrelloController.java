package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.QuantitaInsufficenteException;
import com.videogames.videogames.Repository.CarrelloGiocoRepository;
import com.videogames.videogames.Repository.CarrelloRepository;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Service.carrelloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrello")
public class carrelloController {

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private carrelloService carrelloService;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

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
                                  RedirectAttributes redirectAttributes){
        //utente guest
        if(principal.getName().equalsIgnoreCase("guest")){
            redirectAttributes.addFlashAttribute("erroreCarrello", "Per poter procedere" +
                    " è necessaria la registrazione" +
                    "<a href='/login/register' class='alert-link'> Clicca qui per procedere</a>");
            return "redirect:/";
        }
        //Mi recupero il gioco
        Gioco optGioco = giocoRepository.findById(id).get();
        if (optGioco.getQuantita() <= 0) {
            redirectAttributes.addFlashAttribute("erroreCarrello", "Gioco esaurito");
            return "redirect:/"; // o la pagina di dettaglio del gioco
        }

        carrelloService.addCarrelloGioco(id, principal);
        return "redirect:/carrello";
    }

    //Sconto per codice promozionale inserito
    @PostMapping("/codicePromozionale")
    public String scountCodicePromozionale(@ModelAttribute("formAddCodicePromozionale") CodiciPromozionale codiciPromozionale,
                                           RedirectAttributes redirectAttributes, Model model, Principal principal){
        List<Gioco> giochi = giocoRepository.findAll();
        List<CodiciPromozionale> codiciPromozionaliPresenti = codicePromozionaleRepository.findAll();
        Optional<Utente> u = carrelloService.recuperoUtente(principal);
        List<CarrelloGioco> carrello = carrelloGiocoRepository.findByUtente(u);

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
                        //Applico sconto
                        for (Gioco g : giochi){
                            if (g.getIdGioco() == idGioco){
                                codice.setUsato(true);
                                codicePromozionaleRepository.save(codice);
                                double prezzo = carrelloService.prezzoFinaleCarrello(principal,codice.getValoreCodicePromozionale());
                                //Trasformo il prezzo in modo che abbia due decimali
                                DecimalFormat priceDecimal = new DecimalFormat("#.##");
                                String priceDecimalFormatter = priceDecimal.format(prezzo);
                                model.addAttribute("prezzoTotale", priceDecimalFormatter);
                                model.addAttribute("listCarrello", carrello);
                                model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
                                return "redirect:/carrello";
                            }
                        }
                    }
                }
            }
        }
        return "Carrello/carrello";
    }

    @PostMapping("/delete/{id}")
    public String cancellaGiocoCarrello(@PathVariable("id") Integer id, Principal principal){
        carrelloService.cancellaGiocoCarrello(id, principal);
        return "redirect:/carrello";
    }

    @GetMapping("/edit/{id}")
    public String showPageEditCarrello(@PathVariable("id") Integer id, Model model){
        Optional<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findById(id);
        model.addAttribute("carrelloGioco",carrelloGioco.get());
        model.addAttribute("formEditCarrello", carrelloGioco.get());
        return "Carrello/editCarrello";
    }

    @PostMapping("/edit/{id}")
    public String carrelloGiocoEdit(@PathVariable("id") Integer id, @Valid
                                    @ModelAttribute("formEditCarrello") CarrelloGioco formEditCarrello,
                                    BindingResult bindingResult, Model model, Principal principal){
        if (bindingResult.hasErrors()){
            return "Carrello/editCarrello";
        }
        try {
            carrelloService.modificaQuantitaCarrello(id, formEditCarrello,principal);
            carrelloService.prezzoFinaleCarrello(principal, 0);
        }catch (QuantitaInsufficenteException ex){
            CarrelloGioco carrelloGioco = carrelloGiocoRepository.findById(id).get();
            bindingResult.rejectValue("quantita", "errorQuantita",
                    "Nel magazzino i prodotti ancora disponibili sono " + ex.getDisponibilitaMagazzino());
            model.addAttribute("carrelloGioco", carrelloGioco);
            model.addAttribute("formEditCarrello", formEditCarrello);
            return "Carrello/editCarrello";
        }
        return "redirect:/carrello";
    }
}
