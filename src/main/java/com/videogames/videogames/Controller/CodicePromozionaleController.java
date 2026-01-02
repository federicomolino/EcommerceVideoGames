package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Service.CodicePromozionaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gioco/addCodicePromozionale")
public class CodicePromozionaleController extends HelpUtente {

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    @Autowired
    private CodicePromozionaleService codicePromozionaleService;

    @GetMapping()
    public String showCodicePromozionale (Model model){
        Utente utente = GetUtente();
        List<Gioco> gioco = giocoRepository.findGiochiByUtenteId(utente.getId_utente());
        model.addAttribute("listGiochi", gioco);
        model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
        return "CodicePromozionale/CodicePromozionale";
    }

    @PostMapping()
    public String addCodiceSconto(@Valid @ModelAttribute("formAddCodicePromozionale") CodiciPromozionale codiciPromozionale,
                                  @RequestParam(required = false) String GeneraCodice, BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes){
        Utente utente = GetUtente();
        List<Gioco> gioco = giocoRepository.findGiochiByUtenteId(utente.getId_utente());

        List<CodiciPromozionale> codiciPromozionali =
                codicePromozionaleRepository.findCodiciPromozionaliByUtenteId(utente.getId_utente());
        for (CodiciPromozionale codice : codiciPromozionali){
            if (codice.getCodicePromozionale().equals(codiciPromozionale.getCodicePromozionale())){
                bindingResult.rejectValue("codicePromozionale","ErrorCodicePromozionale",
                        "Codice già presente nel sistema");
                model.addAttribute("listGiochi",gioco);
                return "CodicePromozionale/CodicePromozionale";
            }
        }
        if (GeneraCodice == null){
            if (codiciPromozionale.getCodicePromozionale().trim().isEmpty()){
                bindingResult.rejectValue("codicePromozionale","ErrorCodicePromozionale",
                        "Codice non valido");
                model.addAttribute("listGiochi",gioco);
            }
        }else {
            String codiceGenerato = codicePromozionaleService.GeneraCodice(utente.getId_utente());
            if (codiceGenerato != null && codiceGenerato.length() == 30){
                codiciPromozionale.setCodicePromozionale(codiceGenerato);
            }else {
                redirectAttributes.addFlashAttribute("codiceAggiunto",
                        "Errore durante la generazione del Codice Sconto");
            }
        }

        if (codiciPromozionale.getValoreCodicePromozionale() != 20 && codiciPromozionale.getValoreCodicePromozionale() != 50
                && codiciPromozionale.getValoreCodicePromozionale() != 100){
            bindingResult.rejectValue("valoreCodicePromozionale","ErrorValoreCodice",
                    "Valore inserito non valido");
            model.addAttribute("listGiochi",gioco);
            return "CodicePromozionale/CodicePromozionale";
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("listGiochi",gioco);
            return "CodicePromozionale/CodicePromozionale";
        }
        try {
            codicePromozionaleService.addCodicePromoziale(codiciPromozionale, utente);
            redirectAttributes.addFlashAttribute("codiceAggiunto","Il codice è stato aggiunto");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("codiceAggiunto","Errore durante il salvataggio");
        }
        return "redirect:/gioco/addCodicePromozionale";
    }

    @GetMapping("/listaCodici")
    public String showCodiciPresenti(Model model){
        Utente utente = GetUtente();
        List<CodiciPromozionale> codiciPromozionale =
                codicePromozionaleRepository.findCodiciPromozionaliByUtenteId(utente.getId_utente());
        model.addAttribute("listCodiciPromozinali", codiciPromozionale);
        return "CodicePromozionale/listaCodiciPromozionali";
    }

    @PostMapping("/listaCodici-delete")
    public String deleteCodicePromozionale(@RequestParam(value = "selectedIds",required = false) List<Long> selectedIds,
                                           @RequestParam("action") String action,
                                           RedirectAttributes redirectAttributes){

        System.out.println("Azione ricevuta: " + action);

        if (selectedIds == null || selectedIds.isEmpty()){
            redirectAttributes.addFlashAttribute("listCodici",
                    "Non è stato selezionato nessun codice");
            return "redirect:/gioco/addCodicePromozionale/listaCodici";
        }

        if ("delete".equals(action)){
            codicePromozionaleService.cancellaCodicePromozionale(selectedIds);
        } else if ("active".equals(action)) {
            codicePromozionaleService.riattivaCodiciPromozionali(selectedIds);
        }

        return "redirect:/gioco/addCodicePromozionale/listaCodici";
    }
}
