package com.videogames.videogames.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recensione_id;

    @ManyToOne
    @JoinColumn(name = "gioco_id")
    @JsonIgnore
    private Gioco gioco;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    @JsonIgnore
    private Utente utente;

    private String recensione;

    private LocalDate dataRecensione;

    public Recensione(){
        this.dataRecensione = LocalDate.now();
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Long getRecensione_id() {
        return recensione_id;
    }

    public void setRecensione_id(Long recensione_id) {
        this.recensione_id = recensione_id;
    }

    public LocalDate getDataRecensione() {
        return dataRecensione;
    }

    public void setDataRecensione(LocalDate dataRecensione) {
        this.dataRecensione = dataRecensione;
    }

    public Long getId() {
        return recensione_id;
    }

    public void setId(Long recensione_id) {
        this.recensione_id = recensione_id;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }

    public String getRecensione() {
        return recensione;
    }

    public void setRecensione(String recensione) {
        this.recensione = recensione;
    }
}
