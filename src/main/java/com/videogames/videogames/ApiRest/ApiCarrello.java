package com.videogames.videogames.ApiRest;

import com.videogames.videogames.Dto.CarrelloDto;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carrello")
public class ApiCarrello extends HelpUtente {

    CarrelloService carrelloService;

    @Autowired
    public ApiCarrello(CarrelloService carrelloService){
        this.carrelloService = carrelloService;
    }

    @PostMapping("/add/{id}")
    @ResponseBody
    public ResponseEntity<String> giocoAlCarrello(@PathVariable("id") Integer id,
                                                  Principal principal,
                                                  @RequestBody(required = false) CarrelloDto.AggiungiAlCarrelloRequest aggiungiAlCarrelloRequest,
                                                  @RequestParam(value = "piattaformaId", required = false) Integer piattaformaId){
        if(aggiungiAlCarrelloRequest != null){
            try {
                carrelloService.VerificaAggiungiAlCarrello(id, aggiungiAlCarrelloRequest);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch (NessunaPiattaformaPresente e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }catch (UsernameNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }catch (NessunGiocoTrovato e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        //Da qui entro solo se non sono api
        //utente guest
        if(principal.getName().equalsIgnoreCase("guest")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accedere per poter procedere con l'acquisto");
        }
        Integer idPiattaforma = Optional.ofNullable(piattaformaId).orElse(0);
        try {
            carrelloService.AddCarrelloGioco(id, idPiattaforma, carrelloService.GetUtente());
            return ResponseEntity.ok("Gioco Aggiunto Al Carrello");
        }catch (NessunGiocoTrovato e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Gioco esaurito");
        }catch (NessunaPiattaformaPresente ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
