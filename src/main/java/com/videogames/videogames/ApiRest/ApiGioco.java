package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Exception.ExceptionAddGioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Service.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gioco")
public class ApiGioco {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private GiocoService GiocoService;

    @GetMapping
    public ResponseEntity<?> gioco(){
        List<Gioco> gioco = giocoRepository.findAll();
        if (gioco.isEmpty()){
            throw new NessunGiocoTrovato("CG404_NESSUN_GIOCO_TROVATO");
        }
        return ResponseEntity.ok(gioco);
    }

    @DeleteMapping("/{id}")
    public void deleteGioco(@PathVariable Integer id){
        GiocoService.cancellaGioco(id);
    }

    @PostMapping
    public ResponseEntity<?> addGioco(@RequestBody Gioco gioco) {
        try {
            Gioco newGioco = GiocoService.addGioco(gioco);
            return ResponseEntity.status(HttpStatus.CREATED).body(newGioco);
        }catch (ExceptionAddGioco ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
