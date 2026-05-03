package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.ListaDesideriGioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListaDesideriGiocoRepository extends JpaRepository<ListaDesideriGioco, Integer> {
    @Query(value = """
        select ldg.* 
        from lista_desideri ld
        join lista_desideri_gioco ldg on ldg.id_lista_desideri = ld.id_lista_desideri
        where ld.id_utente = :idUtente
        """, nativeQuery = true)
    List<ListaDesideriGioco> findByUtenteId(@Param("idUtente") int idUtente);

    @Query(value = """
        select ldg.* 
        from lista_desideri ld
        join lista_desideri_gioco ldg on ldg.id_lista_desideri = ld.id_lista_desideri
        where ld.id_utente = :idUtente and ldg.id_gioco = :idGioco
        """, nativeQuery = true)
    List<ListaDesideriGioco> findByUtenteIdAndGioco(@Param("idUtente") int idUtente,
                                                    @Param("idGioco") int idGioco);
}
