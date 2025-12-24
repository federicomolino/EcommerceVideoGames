package com.videogames.videogames.Helpers;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class HelpUtente implements IHelpers {

    @Autowired
    private UserRepository userRepository;

    //Restituisce l'utente, se non lo trova restituisce null
    @Override
    public Utente GetUtente (){
        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        if(aut == null || !aut.isAuthenticated()){
            return null;
        }
        String username = aut.getName();
        Optional<Utente> utente = userRepository.findByUsername(username);
        if (utente.isPresent()){
            return utente.get();
        }else {
            return null;
        }
    }
}
