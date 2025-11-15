package com.videogames.videogames.GestioneDipendentiInt;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.Map;

@Service
public class AutenticazioneToken {

    //Oggetto per fare le chiamate HTTP
    private final RestTemplate restTemplate = new RestTemplate();
    private final String username = "Api.VideoGames";
    private final String password = "Federico004@";

    public String autenticazione() throws LoginException{
        String url = "http://localhost:9090/api/v1/token";

        Map<String, String> body = Map.of("username", username);
        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(url, body, AuthResponse.class);
            return response.getBody().getToken();
        }catch (HttpClientErrorException.Unauthorized ex){
            throw new LoginException("Credenziali non valide");
        }catch (HttpClientErrorException.Forbidden e){
            throw new LoginException("Username non abilitato");
        }
    }
}
