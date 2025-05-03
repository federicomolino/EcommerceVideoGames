package com.videogames.videogames.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class CarrelloGioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_carrelloGioco;

    @ManyToOne
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    @ManyToOne
    private Gioco gioco;

    @ManyToOne
    private Utente utente;

    @Valid
    @Min(value = 1, message = "Valore minimo: 1")
    @Max(value = 9, message = "Valore massimo: 9")
    private Integer quantita;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente( Utente utente) {
        this.utente = utente;
    }

    public Integer getId_carrelloGioco() {
        return id_carrelloGioco;
    }

    public void setId_carrelloGioco(Integer id_carrelloGioco) {
        this.id_carrelloGioco = id_carrelloGioco;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
