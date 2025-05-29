package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Recensione;
import com.videogames.videogames.Repository.RecensioneRepository;
import com.videogames.videogames.Service.RecensioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/gioco/addRecensione")
public class RecensioneController {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private RecensioniService recensioniService;

    @GetMapping("/{idGioco}")
    public String showPageAddRecensione(@PathVariable("idGioco") Integer idGioco, Model model, Principal principal){
        model.addAttribute("formAddRecensione",new Recensione());
        model.addAttribute("utente", principal);
        return "gioco/Recensione/AddRecensione";
    }

    @PostMapping("/{idGioco}")
    public String addRecensione(@ModelAttribute("formAddRecensione") Recensione formAddRecensione,
                                @PathVariable("idGioco") Integer idGioco, Principal principal){

        recensioniService.addRecensione(formAddRecensione, idGioco, principal);
        return "redirect:/gioco/infoGame/" + idGioco;
    }
}
