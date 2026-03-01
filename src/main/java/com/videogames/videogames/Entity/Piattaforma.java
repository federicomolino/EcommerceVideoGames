package com.videogames.videogames.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Piattaforma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_piattaforma;

    private String nomePiattaforma;

    private int quantitaPerPiattaforma;

    @ManyToMany(mappedBy = "piattaforma")
    @JsonIgnore
    private List<Gioco> gioco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @JsonIgnore
    @OneToMany(mappedBy = "piattaforma")
    private List<CarrelloGioco> carrelliAssociati;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<CarrelloGioco> getCarrelliAssociati() {
        return carrelliAssociati;
    }

    public void setCarrelliAssociati(List<CarrelloGioco> carrelliAssociati) {
        this.carrelliAssociati = carrelliAssociati;
    }

    public List<Gioco> getGioco() {
        return gioco;
    }

    public void setGioco(List<Gioco> gioco) {
        this.gioco = gioco;
    }

    public Integer getId_piattaforma() {
        return id_piattaforma;
    }

    public void setId_piattaforma(Integer id_piattaforma) {
        this.id_piattaforma = id_piattaforma;
    }

    public int getQuantitaPerPiattaforma() {
        return quantitaPerPiattaforma;
    }

    public void setQuantitaPerPiattaforma(int quantitaPerPiattaforma) {
        this.quantitaPerPiattaforma = quantitaPerPiattaforma;
    }

    public String getNomePiattaforma() {
        return nomePiattaforma;
    }

    public void setNomePiattaforma(String nomePiattaforma) {
        this.nomePiattaforma = nomePiattaforma;
    }
}
