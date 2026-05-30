package com.videogames.videogames.ApiRestController;

import com.videogames.videogames.ApiRestController.Token.Token;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Service.TokenService;
import com.videogames.videogames.VideoGamesGlobal.JsonUtil;
import com.videogames.videogames.VideoGamesGlobal.TableLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class ApiToken extends HelpUtente {

    private final TokenService tokenService;
    private TableLog tableLog;
    private JsonUtil jsonUtil;

    @Autowired
    public ApiToken(TokenService tokenService, TableLog tableLog, JsonUtil JsonUtil){
        this.tokenService = tokenService;
        this.tableLog = tableLog;
        this.jsonUtil = JsonUtil;
    }

    @PostMapping
    public ResponseEntity<Token.Response> Token(@RequestBody(required = false) Token.Command token,
                                                HttpServletRequest request){
        String requestJson = jsonUtil.toJson(token);
        String username = null;
        //Caso API
        if (token != null && token.username != null & !token.username.isBlank()){
            if (tokenService.ControlloPreGenerazioneToken(token.username, token.password)){
                username = token.username;
            }else {
                Token.Response response = new Token.Response(
                        "Credenziali Errate, Impossibile generare il token",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }
        }else {
            //Caso Utente Autenticato
            Utente u = GetUtente();
            if (u != null){
                username = u.getUsername();
            }else {
                return null;
            }
        }

        try {
            String t = tokenService.generaToken(username);
            if (t.isBlank()){

                Token.Response response = new Token.Response(
                        "Errore Sconusciuto",
                        BaseCommad.ResponseType.ERROR
                );
                tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                        request.getRequestURI(), BaseCommad.ResponseType.ERROR.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);

            }
            Token.Response response = new Token.Response(
                    t,
                    BaseCommad.ResponseType.OK
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (UsernameNotFoundException ex){
            Token.Response response = new Token.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.OK
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }catch (Exception ex){
            Token.Response response = new Token.Response(
                    ex.getMessage(),
                    BaseCommad.ResponseType.OK
            );
            tableLog.InizializzaLog(requestJson, jsonUtil.toJson(response),
                    request.getRequestURI(), BaseCommad.ResponseType.OK.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
    }
}
