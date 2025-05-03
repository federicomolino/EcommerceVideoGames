package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gioco")
public class ApiAddGioco {

    @Autowired
    private giocoRepository giocoRepository;

    @GetMapping
    public ResponseEntity<?> gioco(@RequestParam(value = "name", required = false)String name){
        List<Gioco> gioco = giocoRepository.findAll();
        
        if (gioco.isEmpty()){
            throw new NessunGiocoTrovato("CG404_NESSUN_GIOCO_TROVATO");
        }
        return ResponseEntity.ok(gioco);
    }
}
