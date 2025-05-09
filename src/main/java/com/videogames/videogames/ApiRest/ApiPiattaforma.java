package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Service.PiattaformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/piattaforma")
public class ApiPiattaforma {

    @Autowired
    private PiattaformaService piattaformaService;

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @GetMapping
    public ResponseEntity<?> showPiattaforma(){
        List<Piattaforma> piattaforma = piattaformaRepository.findAll();
        if (piattaforma.isEmpty()){
            throw new NessunaPiattaformaPresente("PI500_NESSUNA_PIATTAFORMA_PRESENTE");
        }
        return ResponseEntity.ok(piattaforma);
    }
}
