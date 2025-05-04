package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Gioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface giocoRepository extends JpaRepository<Gioco,Integer> {

    @Query("SELECT g.titolo FROM Gioco g WHERE g.titolo = :title")
    String TitleGioco(@Param("title")String title);

    @Query("SELECT g.keyAttivazione FROM Gioco g WHERE g.keyAttivazione = :keyAttivazione")
    String KeyGioco(@Param("keyAttivazione")String keyAttivazione);

    @Query("SELECT g.codiceProdotto FROM Gioco g WHERE g.codiceProdotto = :codiceProdotto")
    Optional<Long> findcodiceProdottoGioco(@Param("codiceProdotto")long codiceProdotto);

    List<Gioco> findByTitoloContainingIgnoreCase(String titolo);
}
