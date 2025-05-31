package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Recensione;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.RecensioneRepository;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/v1/recensione")
public class ApiRecensioni {

    private RecensioneRepository recensioneRepository;

    private giocoRepository giocoRepository;

    @Autowired
    public ApiRecensioni(RecensioneRepository recensioneRepository, giocoRepository giocoRepository){
        this.recensioneRepository = recensioneRepository;
        this.giocoRepository = giocoRepository;
    }

    @GetMapping("/{idGioco}")
    public ResponseEntity<?> showRecensioniPerIdGioco(@PathVariable Integer idGioco){
        Optional<Gioco> gioco = giocoRepository.findById(idGioco);
        if (gioco.isPresent()){
            List<Recensione> recensione = recensioneRepository.findByGiocoIdGioco(idGioco);

            Map<String, Object> response = new HashMap<>();
            response.put("idGioco", idGioco);
            response.put("recensione",recensione);
            return ResponseEntity.ok(recensione);
        }
        throw new NessunGiocoTrovato("CG_404_NESSUN_GIOCO_TROVATO");
    }
}
