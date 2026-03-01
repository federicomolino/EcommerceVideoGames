package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carrello")
public class ApiCarrello {

    CarrelloService carrelloService;

    @Autowired
    public ApiCarrello(CarrelloService carrelloService){
        this.carrelloService = carrelloService;
    }

    @PostMapping("/add/{id}")
    @ResponseBody
    public ResponseEntity<String> giocoAlCarrello(@PathVariable("id") Integer id, Principal principal,
                                  @RequestParam(value = "piattaformaId", required = false) Integer piattaformaId){
        //utente guest
        if(principal.getName().equalsIgnoreCase("guest")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accedere per poter procedere con l'acquisto");
        }
        Integer idPiattaforma = Optional.ofNullable(piattaformaId).orElse(0);
        try {
            carrelloService.addCarrelloGioco(id, principal, idPiattaforma);
            return ResponseEntity.ok("Gioco Aggiunto Al Carrello");
        }catch (NessunGiocoTrovato e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Gioco esaurito");
        }catch (NessunaPiattaformaPresente ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
