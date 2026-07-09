package com.videogames.videogames.Repository;

import com.videogames.videogames.Batch.BatchDTO;
import com.videogames.videogames.Entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Integer> {

    @Query("""
    SELECT new com.videogames.videogames.Batch.BatchDTO(
        tb.idBatch,
        tb.nomeBatch,
        tb.isAttivo,
        tb.ultimoGiroBatch
    )
    FROM Table_batch tb
    WHERE tb.nomeBatch = :nomeBatch
    AND tb.isAttivo = true
""")
    List<BatchDTO> findBatchAttiviByNomeBatch(@Param("nomeBatch") String nomeBatch);

    @Query("""
    SELECT new com.videogames.videogames.Batch.BatchDTO(
        tb.idBatch,
        tb.nomeBatch,
        tb.isAttivo,
        tb.ultimoGiroBatch
    )
    FROM Table_batch tb
    WHERE tb.utente.id_utente = :idUtente
""")
    List<BatchDTO> findBatchPerUtente(@Param("idUtente") Integer idUtente);
}
