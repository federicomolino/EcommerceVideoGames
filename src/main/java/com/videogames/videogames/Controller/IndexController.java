package com.videogames.videogames.Controller;

import com.videogames.videogames.Repository.ParametroRepository;
import com.videogames.videogames.Service.GiocoService;
import com.videogames.videogames.Service.ParametroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class IndexController {

    private GiocoService GiocoService;
    private ParametroRepository parametroRepository;

    @Autowired
    public IndexController(GiocoService giocoService, ParametroRepository parametroRepository,
                           ParametroService parametroService){
        this.GiocoService = giocoService;
        this.parametroRepository = parametroRepository;
    }


    @GetMapping("/")
    public String indexPage(@RequestParam(name = "titolo", required = false)String titolo,
                            Model model, Principal principal){
        model.addAttribute("list", GiocoService.showGiochi(titolo, principal));
        return "gioco/index";
    }
}
