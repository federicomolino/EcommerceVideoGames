package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Gioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface giocoRepository extends JpaRepository<Gioco,Integer> {

    @Query("SELECT g FROM Gioco g WHERE g.titolo = :title")
    List<Gioco> TitleGioco(@Param("title")String title);

    @Query("SELECT g FROM Gioco g WHERE g.keyAttivazione = :keyAttivazione")
    Gioco KeyGioco(@Param("keyAttivazione")String keyAttivazione);

    @Query("SELECT g.codiceProdotto FROM Gioco g WHERE g.codiceProdotto = :codiceProdotto")
    Optional<Long> findcodiceProdottoGioco(@Param("codiceProdotto")long codiceProdotto);
}
