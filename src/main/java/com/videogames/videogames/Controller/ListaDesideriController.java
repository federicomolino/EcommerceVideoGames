package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.ListaDesideri;
import com.videogames.videogames.Entity.ListaDesideriGioco;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.ListaDesideriGiocoRepository;
import com.videogames.videogames.Repository.ListaDesideriRepository;
import com.videogames.videogames.Service.ListaDesideriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("Preferiti")
public class ListaDesideriController {

    ListaDesideriService listaDesideriService;
    ListaDesideriRepository listaDesideriRepository;
    ListaDesideriGiocoRepository listaDesideriGiocoRepository;

    @Autowired
    public ListaDesideriController(ListaDesideriRepository listaDesideriRepository,
                                   ListaDesideriService listaDesideriService, ListaDesideriGiocoRepository listaDesideriGiocoRepository){
        this.listaDesideriRepository = listaDesideriRepository;
        this.listaDesideriService = listaDesideriService;
        this.listaDesideriGiocoRepository = listaDesideriGiocoRepository;
    }

    @GetMapping()
    public String GetPreferiti(Model model){
        Utente u = listaDesideriService.GetUtente();
        if (u == null){
            return "redirect:/";
        }
        Optional <ListaDesideri> list = listaDesideriRepository.findByUtente(u);
        if (list.isPresent()){
            List<ListaDesideriGioco>  l = listaDesideriGiocoRepository.findByUtenteId(u.getId_utente());
            model.addAttribute("listPreferiti", l);
        }else {
            model.addAttribute("listPreferiti", null);
        }
        return "Preferiti/Preferiti";
    }
}
