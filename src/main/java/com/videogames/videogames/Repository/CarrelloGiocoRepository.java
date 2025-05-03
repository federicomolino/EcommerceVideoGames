package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.CarrelloGioco;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarrelloGiocoRepository extends JpaRepository<CarrelloGioco, Integer> {

    List<CarrelloGioco> findByUtente (Optional<Utente> utente);

    Optional<CarrelloGioco> findByUtenteAndGioco (Utente utente, Gioco gioco);

    @Query("SELECT cg FROM CarrelloGioco cg WHERE cg.gioco.id = :giocoId")
    Optional<CarrelloGioco> findByIdGiocoCarrello(@Param("giocoId")int giocoId);
}
