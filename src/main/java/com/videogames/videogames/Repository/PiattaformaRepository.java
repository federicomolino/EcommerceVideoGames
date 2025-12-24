package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Piattaforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PiattaformaRepository extends JpaRepository<Piattaforma, Integer> {

    @Query("SELECT p FROM Piattaforma p WHERE p.utente.id_utente = :idUtente")
    List<Piattaforma> findPiattaformaByUtenteId(@Param("idUtente") Integer idUtente);
}
