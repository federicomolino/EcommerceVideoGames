package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.ListaDesideri;
import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListaDesideriRepository extends JpaRepository<ListaDesideri, Integer> {
    Optional<ListaDesideri> findByUtente (Utente utente);
}
