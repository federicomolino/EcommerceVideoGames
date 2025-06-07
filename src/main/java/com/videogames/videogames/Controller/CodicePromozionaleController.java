package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.giocoRepository;
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
public class CodicePromozionaleController {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    @Autowired
    private CodicePromozionaleService codicePromozionaleService;

    @GetMapping()
    public String showCodicePromozionale (Model model){
        List<Gioco> gioco = giocoRepository.findAll();
        model.addAttribute("listGiochi", gioco);
        model.addAttribute("formAddCodicePromozionale", new CodiciPromozionale());
        return "CodicePromozionale/CodicePromozionale";
    }

    @PostMapping()
    public String addCodiceSconto(@Valid @ModelAttribute("formAddCodicePromozionale") CodiciPromozionale codiciPromozionale,
                                  BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        List<Gioco> gioco = giocoRepository.findAll();

        List<CodiciPromozionale> codiciPromozionali = codicePromozionaleRepository.findAll();
        for (CodiciPromozionale codice : codiciPromozionali){
            if (codice.getCodicePromozionale().equals(codiciPromozionale.getCodicePromozionale())){
                bindingResult.rejectValue("codicePromozionale","ErrorCodicePromozionale",
                        "Codice già presente nel sistema");
                model.addAttribute("listGiochi",gioco);
                return "CodicePromozionale/CodicePromozionale";
            }
        }
        if (codiciPromozionale.getCodicePromozionale().trim().isEmpty()){
            bindingResult.rejectValue("codicePromozionale","ErrorCodicePromozionale",
                    "Codice non valido");
            model.addAttribute("listGiochi",gioco);
        }

        if (codiciPromozionale.getValoreCodicePromozionale() != 20 && codiciPromozionale.getValoreCodicePromozionale() != 50
                && codiciPromozionale.getValoreCodicePromozionale() != 100){
            bindingResult.rejectValue("valoreCodicePromozionale","ErrorValoreCodice",
                    "Valore inserito non valido");
            model.addAttribute("listGiochi",gioco);
            return "CodicePromozionale/CodicePromozionale";
        }

        codicePromozionaleService.addCodicePromoziale(codiciPromozionale);

        if (bindingResult.hasErrors()){
            model.addAttribute("listGiochi",gioco);
            return "CodicePromozionale/CodicePromozionale";
        }
        redirectAttributes.addFlashAttribute("codiceAggiunto","Il codice è stato aggiunto");
        return "redirect:/gioco/addCodicePromozionale";
    }

    @GetMapping("/listaCodici")
    public String showCodiciPresenti(Model model){
        List<CodiciPromozionale> codiciPromozionale = codicePromozionaleRepository.findAll();
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
