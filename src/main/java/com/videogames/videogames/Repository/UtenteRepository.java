package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    List<Utente> findByUsername (String username);

    List<Utente> findByEmail (String email);

    Optional<Utente> findByIban (String iban);
}
