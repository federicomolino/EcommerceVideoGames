package com.videogames.videogames.Security;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> optUtente = userRepository.findByUsername(username);

        if (optUtente.isPresent()){
            return new DataBaseUserDetails(optUtente.get());
        }else {
            throw new UsernameNotFoundException("Utente non valido");
        }
    }
}
