package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.CodiciPromozionale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodicePromozionaleRepository extends JpaRepository<CodiciPromozionale,Long> {

    @Query("SELECT cp FROM CodiciPromozionale cp WHERE cp.utente.id_utente = :idUtente")
    List<CodiciPromozionale> findCodiciPromozionaliByUtenteId(@Param("idUtente") Integer idUtente);

    @Query("SELECT CASE WHEN cp IS NOT NULL THEN true ELSE false END " +
            "FROM CodiciPromozionale cp " +
            "WHERE cp.codicePromozionale = :codice " +
            "AND cp.utente.id_utente = :idUtente")
    Optional<CodiciPromozionale> existsByCodiceAndUtenteId(@Param("codice") String codice, @Param("idUtente") Integer idUtente);
}
