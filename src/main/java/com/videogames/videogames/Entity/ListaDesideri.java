package com.videogames.videogames.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ListaDesideri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idListaDesideri;

    @OneToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @OneToMany(mappedBy = "listaDesideri", cascade = CascadeType.ALL)
    private List<ListaDesideriGioco> giochi = new ArrayList<>();

    private LocalDateTime dataCreazione;

    private LocalDateTime ultimaModifica;

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getUltimaModifica() {
        return ultimaModifica;
    }

    public void setUltimaModifica(LocalDateTime ultimaModifica) {
        this.ultimaModifica = ultimaModifica;
    }

    public int getIdListaDesideri() {
        return idListaDesideri;
    }

    public void setIdListaDesideri(int idListaDesideri) {
        this.idListaDesideri = idListaDesideri;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<ListaDesideriGioco> getGiochi() {
        return giochi;
    }

    public void setGiochi(List<ListaDesideriGioco> giochi) {
        this.giochi = giochi;
    }

}
