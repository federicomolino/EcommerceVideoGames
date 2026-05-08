package com.videogames.videogames.ApiRestController;

import com.videogames.videogames.ApiRestController.Piattaforma.CreaPiattaforma;
import com.videogames.videogames.ApiRestController.Piattaforma.PiattaformaCommand;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Service.PiattaformaService;
import com.videogames.videogames.VideoGamesGlobal.JsonUtil;
import com.videogames.videogames.VideoGamesGlobal.TableLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private TableLog tableLog;

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

    @PostMapping("/aggiungiPiattaforma")
    public ResponseEntity<CreaPiattaforma.Response> AddPiattaforma(@RequestBody CreaPiattaforma.Command p,
                                                       HttpServletRequest request) {
        String requestJson = jsonUtil.toJson(p);
        try {
            Piattaforma piattaforma = CompilaPiattaforma(p);
            Piattaforma newpiattaforma = piattaformaService.newPiattaforma(piattaforma, piattaformaService.GetUtenteUsername(p.getUsername()));
            if (newpiattaforma == null){

                CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                        "Errore Sconusciuto durante il tentativo di salvataggio del gioco",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);

            }
            CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                    "Piattaforma Creata",
                    BaseCommad.ResponseType.OK
            );
            response.idPiattaforma = newpiattaforma.getId_piattaforma();
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (Exception ex){
            CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.ALERT
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.ALERT.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/editPiattaforma")
    public ResponseEntity<CreaPiattaforma.Response> EditPiattaforma(@RequestBody CreaPiattaforma.Command p,
                                                                   HttpServletRequest request) {
        String requestJson = jsonUtil.toJson(p);
        try {
            Piattaforma piattaforma = CompilaPiattaforma(p);
            Piattaforma newpiattaforma = piattaformaService.EditPiattaforma(piattaforma, piattaformaService.GetUtenteUsername(p.getUsername()));
            if (newpiattaforma == null){

                CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                        "Errore Sconusciuto durante il tentativo di salvataggio del gioco",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);

            }
            CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                    "Piattaforma Modificata",
                    BaseCommad.ResponseType.OK
            );
            response.idPiattaforma = newpiattaforma.getId_piattaforma();
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (Exception ex){
            CreaPiattaforma.Response response = new CreaPiattaforma.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.ALERT
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.ALERT.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    private Piattaforma CompilaPiattaforma(PiattaformaCommand cmd){
        Piattaforma p = new Piattaforma();
        if (cmd.getIdPiattaforma() > 0){
            p.setId_piattaforma(cmd.getIdPiattaforma());
        }
        p.setNomePiattaforma(cmd.getNomePiattaforma());
        return p;
    }
}
