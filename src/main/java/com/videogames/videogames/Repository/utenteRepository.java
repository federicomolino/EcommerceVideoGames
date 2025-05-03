package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface utenteRepository extends JpaRepository<Utente, Integer> {

    List<Utente> findByUsername (String username);

    List<Utente> findByEmail (String email);
}
