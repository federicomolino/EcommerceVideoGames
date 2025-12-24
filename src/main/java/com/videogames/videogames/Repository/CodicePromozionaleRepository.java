package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.CodiciPromozionale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodicePromozionaleRepository extends JpaRepository<CodiciPromozionale,Long> {

    @Query("SELECT cp FROM CodiciPromozionale cp WHERE cp.utente.id_utente = :idUtente")
    List<CodiciPromozionale> findCodiciPromozionaliByUtenteId(@Param("idUtente") Integer idUtente);
}
