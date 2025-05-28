package com.videogames.videogames.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recensione_id;

    @ManyToOne
    @JoinColumn(name = "gioco_id")
    private Gioco gioco;

    private String recensione;

    private LocalDate dataRecensione;

    public Recensione(String recensione){
        this.recensione = recensione;
        this.dataRecensione = LocalDate.now();
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
