package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Recensione;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.RecensioneRepository;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Repository.utenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class RecensioniService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private utenteRepository utenteRepository;

    public Recensione addRecensione(Recensione recensione, Integer idGioco, Principal principal){
        Gioco gioco = giocoRepository.findById(idGioco).get();
        //Assegno l'utente corretto
        String usernameUtente = principal.getName();
        List<Utente> utente = utenteRepository.findByUsername(usernameUtente);
        Utente user = utente.get(0);
        recensione.setGioco(gioco);
        recensione.setUtente(user);
        return recensioneRepository.save(recensione);
    }

}
