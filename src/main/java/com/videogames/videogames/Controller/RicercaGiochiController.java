package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Service.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gioco")
public class RicercaGiochiController {

    private GiocoService giocoService;

    @Autowired
    public RicercaGiochiController(GiocoService giocoService){
        this.giocoService = giocoService;
    }

    @GetMapping("/searchGiochi")
    public String GetSearchGioco(@RequestParam(required = false) String rangePrezzo,
                                 @RequestParam(required = false) String annoPubblicazione,
                                 @RequestParam(required = false)List<String> piattaforma,
                                 RedirectAttributes redirectAttributes, Model model){

        if (rangePrezzo == null || rangePrezzo.isEmpty() || annoPubblicazione == null || annoPubblicazione.isEmpty() ||
                piattaforma == null || piattaforma.isEmpty()){
            redirectAttributes.addFlashAttribute("nessunCampoRicercaSelezionato",
                    "Nessun Campo Ricerca Selezionato");
        }
        List<Gioco> giochi = null;
        try{
           giochi = giocoService.RicercaGiochi(rangePrezzo,annoPubblicazione,piattaforma);
        }catch (Exception e) {
        redirectAttributes.addFlashAttribute("nessunCampoRicercaSelezionato",
                "Nessun Gioco Trovato!!!");
        return "redirect:/";
    }
        model.addAttribute("list", giochi);
        return "gioco/Index";
    }
}
