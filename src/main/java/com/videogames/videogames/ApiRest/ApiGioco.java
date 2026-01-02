package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Exception.ExceptionAddGioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Service.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gioco")
public class ApiGioco {

    @Autowired
    private GiocoRepository giocoRepository;

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
    public ResponseEntity<?> deleteGioco(@PathVariable Integer id, Principal principal){
        try{
            GiocoService.cancellaGioco(id, principal);
        }catch (NessunGiocoTrovato ex){
            throw new NessunGiocoTrovato("CG_ID_PASSATO_NON_VALIDO");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping
    public ResponseEntity<?> addGioco(@RequestBody Gioco gioco) {
        try {
            //Mi prendo l'id della piattaforma passato
            List<Integer> piattaformaIdPassata = new ArrayList<>();
            for (Piattaforma piattaforma : gioco.getPiattaforma()){
                piattaformaIdPassata.add(piattaforma.getId_piattaforma());
            }

            Gioco newGioco = GiocoService.addGioco(gioco,piattaformaIdPassata);
            return ResponseEntity.status(HttpStatus.CREATED).body(newGioco);
        }catch (ExceptionAddGioco ex){
            throw new ExceptionAddGioco("CG_500_DATI_INSERITI_PRESENTI_NEL_SISTEMA");
        }
    }
}
