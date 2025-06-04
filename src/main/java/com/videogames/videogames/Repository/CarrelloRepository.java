package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Carrello;
import com.videogames.videogames.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrelloRepository extends JpaRepository<Carrello,Integer> {

     Carrello findByUtente(Utente utente);
}
