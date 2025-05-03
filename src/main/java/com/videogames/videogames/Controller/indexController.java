package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class indexController {

    @Autowired
    private giocoRepository giocoRepository;

    @GetMapping("/")
    public String indexPage(Model model){
        List<Gioco> giochi = giocoRepository.findAll();
        model.addAttribute("list", giochi);
        return "gioco/index";
    }
}
