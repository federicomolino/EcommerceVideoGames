package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Utente,Integer> {

    Optional<Utente> findByUsername(String username);
}
