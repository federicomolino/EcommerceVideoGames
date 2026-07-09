package com.videogames.videogames.ApiRestController.Gioco;

import com.videogames.videogames.ApiRestController.BaseCommad;
import com.videogames.videogames.ApiRestController.Interfacce.IGioco;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Exception.ExceptionAddGioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Service.GiocoService;
import com.videogames.videogames.VideoGamesGlobal.JsonUtil;
import com.videogames.videogames.VideoGamesGlobal.TableLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gioco")
public class ApiGiocoController implements IGioco {

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private GiocoService GiocoService;

    @Autowired
    private TableLog tableLog;

    @Autowired
    private JsonUtil jsonUtil;

    @GetMapping
    public ResponseEntity<?> gioco(){
        List<Gioco> gioco = giocoRepository.findAll();
        if (gioco.isEmpty()){
            throw new NessunGiocoTrovato("CG404_NESSUN_GIOCO_TROVATO");
        }
        return ResponseEntity.ok(gioco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteGioco(@PathVariable Integer id, HttpServletRequest request,
                                         Principal principal){
        String requestJson = jsonUtil.toJson(id);
        try{
            GiocoService.cancellaGioco(id, principal);
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(null),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
        }catch (NessunGiocoTrovato ex){
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(null),
                    request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
            throw new NessunGiocoTrovato("CG_ID_PASSATO_NON_VALIDO");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping("/addGioco")
    public ResponseEntity<CreaGioco.Response> AddGioco(@RequestBody CreaGioco.Command g, HttpServletRequest request) {
        String requestJson = jsonUtil.toJson(g);
        try {
            //Mi prendo l'id della piattaforma passato
            List<Integer> piattaformaIdPassata = g.piattaforma == null
                    ? new ArrayList<>()
                    : g.piattaforma.stream()
                    .map(p -> p.idPiattaforma)
                    .toList();

            Gioco gioco = CompilaGioco(g);
            Gioco newGioco = GiocoService.addGioco(gioco,piattaformaIdPassata);
            if (newGioco == null){

                CreaGioco.Response response = new CreaGioco.Response(
                        "Errore Sconusciuto durante il tentativo di salvataggio del gioco",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
            CreaGioco.Response response = new CreaGioco.Response(
                    "Gioco Creato",
                    BaseCommad.ResponseType.OK
            );
            response.idGioco = newGioco.getIdGioco();
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (ExceptionAddGioco ex){
            CreaGioco.Response response = new CreaGioco.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.ALERT
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.ALERT.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @PostMapping("/editGioco/{idGioco}")
    public ResponseEntity<EditGioco.Response> EditGioco(@PathVariable Integer idGioco,
                                                        @RequestBody EditGioco.Command g, HttpServletRequest request) {
        String requestJson = jsonUtil.toJson(g);
        try {
            //Mi prendo l'id della piattaforma passato
            List<Integer> piattaformaIdPassata = g.piattaforma == null
                    ? new ArrayList<>()
                    : g.piattaforma.stream()
                    .map(p -> p.idPiattaforma)
                    .toList();

            Gioco gioco = CompilaGioco(g);
            gioco.setIdGioco(idGioco);
            Gioco newGioco = GiocoService.editGioco(gioco,piattaformaIdPassata);
            if (newGioco == null){

                EditGioco.Response response = new EditGioco.Response(
                        "Errore Sconusciuto durante il tentativo di salvataggio del gioco",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
            EditGioco.Response response = new EditGioco.Response(
                    "Gioco Aggiornato",
                    BaseCommad.ResponseType.OK
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (ExceptionAddGioco ex){
            EditGioco.Response response = new EditGioco.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.ALERT
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.ALERT.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/RecuperaGiochi")
    public ResponseEntity<RecuperaGiochi.Response> GetGiochiPerUtente(@RequestParam String username){
        try {
            if (username.isBlank()){

                RecuperaGiochi.Response response = new RecuperaGiochi.Response(
                    "Username non indicato",
                    BaseCommad.ResponseType.ERROR,
                    null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            Utente user = null;
            try {
                user = GiocoService.GetUtenteUsername(username);
                if (user == null){
                    throw new UsernameNotFoundException("Username Non valido");
                }
            }catch (UsernameNotFoundException e){
                RecuperaGiochi.Response response = new RecuperaGiochi.Response(
                        e.getMessage(),
                        BaseCommad.ResponseType.ERROR,
                        null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            List<Gioco> listGiocoPerUtente = GiocoService.GetGiochiPerUtente(user.getId_utente());
            RecuperaGiochi.Response response = new RecuperaGiochi.Response(
                    "",
                    BaseCommad.ResponseType.OK,
                    listGiocoPerUtente.stream()
                            .map(RecuperaGiochi.Response.GiocoDto::new)
                            .toList()
            );
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        }catch (Exception ex){
            RecuperaGiochi.Response response = new RecuperaGiochi.Response(
                    "Errore generico durante il tentativo di recupero dei giochi",
                    BaseCommad.ResponseType.ERROR,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    private Gioco CompilaGioco(GiocoCommand cmd){
        Gioco g = new Gioco();
        g.setTitolo(cmd.getTitolo());
        g.setDescrizione(cmd.getDescrizione());
        g.setSoftwareHouse(cmd.getSoftwareHouse());
        g.setDataUscitaGioco(cmd.getDataUscitaGioco());
        g.setCodiceProdotto(cmd.getCodiceProdotto());
        g.setKeyAttivazione(cmd.getKeyAttivazione());
        g.setPrezzo(cmd.getPrezzo());
        g.setQuantita(cmd.getQuantita());
        g.setUtente(GiocoService.GetUtenteUsername(cmd.getUsername()));
        return g;
    }
}
