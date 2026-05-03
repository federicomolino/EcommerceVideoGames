package com.videogames.videogames.ApiRestController;

import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Service.ListaDesideriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/listaDesideri")
public class ApiListaDesideri {

    ListaDesideriService listaDesideriService;

    @Autowired
    public ApiListaDesideri(ListaDesideriService listaDesideriService){
        this.listaDesideriService = listaDesideriService;
    }

    @PostMapping("/add/{id}")
    @ResponseBody
    public ResponseEntity<String> AggiungiGiocoListaDesideri(@PathVariable("id") Integer id,
                                                             Principal principal,
                                                             @RequestParam(value = "piattaformaId", required = false) Integer piattaformaId ){
        if(principal.getName().equalsIgnoreCase("guest")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accedere per poter inserire il" +
                    " gioco nella lista preferiti");
        }
        Integer idPiattaforma = Optional.ofNullable(piattaformaId).orElse(0);
        try {
            listaDesideriService.AddListaDesideri(id, idPiattaforma, listaDesideriService.GetUtente());
            return ResponseEntity.ok("Gioco Aggiunto Alla Lista Dei Desideri");
        }catch (NessunGiocoTrovato e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Gioco esaurito");
        }catch (NessunaPiattaformaPresente ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/rimuovi/{id}")
    @ResponseBody
    public ResponseEntity<String> Rimuovi(@PathVariable("id") Integer id,
                                          Principal principal){
        //utente guest
        if(principal.getName().equalsIgnoreCase("guest")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accedere per poter procedere con l'acquisto");
        }

        try {
            listaDesideriService.Rimuovi(id);
            return ResponseEntity.ok("Gioco tolto dalla lista dei preferiti");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Errore durante il tentativo di cancellazione " +
                    "dalla lista dei preferiti");
        }
    }
}
