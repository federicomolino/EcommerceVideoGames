package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneRepository extends JpaRepository<Recensione, Integer> {

    List<Recensione> findByGiocoIdGioco(Integer idGioco);
}
