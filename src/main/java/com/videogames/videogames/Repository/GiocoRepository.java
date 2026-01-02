package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Gioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GiocoRepository extends JpaRepository<Gioco,Integer> {

    @Query("SELECT g.titolo FROM Gioco g WHERE g.titolo = :title")
    String TitleGioco(@Param("title")String title);

    @Query("SELECT g.keyAttivazione FROM Gioco g WHERE g.keyAttivazione = :keyAttivazione")
    String KeyGioco(@Param("keyAttivazione")String keyAttivazione);

    @Query("SELECT g.codiceProdotto FROM Gioco g WHERE g.codiceProdotto = :codiceProdotto")
    Optional<Long> findcodiceProdottoGioco(@Param("codiceProdotto")long codiceProdotto);

    List<Gioco> findByTitoloContainingIgnoreCase(String titolo);

    @Query("""
    SELECT g FROM Gioco g JOIN g.piattaforma p
    WHERE (:piattaforma IS NULL OR p.nomePiattaforma IN :piattaforma)
      AND (:prezzo IS NULL OR g.prezzo >= :prezzo)
      AND (:inizio IS NULL OR :fine IS NULL OR g.dataUscitaGioco BETWEEN :inizio AND :fine)
""")
    List<Gioco> searchGiochi(@Param("piattaforma") List<String> piattaforma,
                             @Param("prezzo") Double prezzo,
                             @Param("inizio") LocalDate inizio,
                             @Param("fine") LocalDate fine);

    @Query("SELECT g FROM Gioco g WHERE g.utente.id_utente = :idUtente")
    List<Gioco> findGiochiByUtenteId(@Param("idUtente") Integer idUtente);
}
