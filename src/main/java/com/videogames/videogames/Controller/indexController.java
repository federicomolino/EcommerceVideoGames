package com.videogames.videogames.Controller;

import com.videogames.videogames.Service.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class indexController {

//    @Autowired
//    private giocoRepository giocoRepository;

    @Autowired
    private GiocoService GiocoService;

    @GetMapping("/")
    public String indexPage(@RequestParam(name = "titolo", required = false)String titolo, Model model){
        model.addAttribute("list", GiocoService.showGiochi(titolo));
        return "gioco/index";
    }
}
