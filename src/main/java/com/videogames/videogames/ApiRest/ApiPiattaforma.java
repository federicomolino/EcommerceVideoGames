package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Service.PiattaformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("{id}")
    public void deletePiattaforma(@PathVariable() Integer id){
        Optional<Piattaforma> piattaforma = piattaformaRepository.findById(id);
        if (piattaforma.isEmpty()){
            throw new NessunaPiattaformaPresente("PI500_NESSUNA_PIATTAFORMA_PRESENTE");
        }
        piattaformaService.deletePiattaforma(id);
    }
}
