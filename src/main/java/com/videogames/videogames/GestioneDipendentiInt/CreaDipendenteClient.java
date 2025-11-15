package com.videogames.videogames.GestioneDipendentiInt;

import com.videogames.videogames.Exception.ApiBadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreaDipendenteClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void creaDipendente(CreaDipendeteUtenteDTO dipendeteUtenteDTO, String token) throws LoginException,
            ApiBadRequestException{

        String url = "http://localhost:9090/api/v1/utente/dipendente/new";

        //Headrs con token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        //Creiamo la Richiesta
        RequestDipendeteUtente request = new RequestDipendeteUtente();
        //regione dipendente
        request.setNome(dipendeteUtenteDTO.getNome());
        request.setCognome(dipendeteUtenteDTO.getCognome());
        request.setDataDiNascita(dipendeteUtenteDTO.getDataDiNascita());
        request.setLuogoDiNascita(dipendeteUtenteDTO.getLuogoDiNascita());
        //end region
        //regione utente
        RequestDipendeteUtente.Utente utente = new RequestDipendeteUtente.Utente();
        utente.setUsername(dipendeteUtenteDTO.getUsername());
        utente.setEmail(dipendeteUtenteDTO.getEmail());
        utente.setPassword(dipendeteUtenteDTO.getPassword());
        //end region
        //region Role
        List<RequestDipendeteUtente.Role> ruoli = new ArrayList<>();
        for (String r : dipendeteUtenteDTO.getRuoli()){
            RequestDipendeteUtente.Role role = new RequestDipendeteUtente.Role();
            role.setNomeRole(r);
            ruoli.add(role);
        }
        utente.setRole(ruoli);
        request.setUtente(utente);
        //end region


        HttpEntity<RequestDipendeteUtente> req = new HttpEntity<>(request, headers);
        //Facciamo la post
        try{
            ResponseEntity<String> response = restTemplate.postForEntity(url,req, String.class);
            if(response.getStatusCode().is2xxSuccessful()){
                //log;
                System.out.println("Creato correttamente" + response.getBody());
            }
        }catch (HttpClientErrorException.Unauthorized ex){
            throw new LoginException("token non passato " + ex);
        }catch (HttpClientErrorException.BadRequest e){
            System.out.println("Body response: " + e.getResponseBodyAsString());
            throw new ApiBadRequestException("Errore nella richiesta " + e.getMessage());
        }catch (Exception genericEx){
            throw new RuntimeException("Errore generico", genericEx);
        }
    }
}
