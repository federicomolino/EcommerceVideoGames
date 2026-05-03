package com.videogames.videogames.Entity;

import jakarta.persistence.*;

@Entity
public class ListaDesideriGioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "id_lista_desideri")
    private ListaDesideri listaDesideri;

    @ManyToOne
    @JoinColumn(name = "id_gioco")
    private Gioco gioco;

    @ManyToOne
    @JoinColumn(name = "id_piattaforma")
    private Piattaforma piattaforma;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }

    public ListaDesideri getListaDesideri() {
        return listaDesideri;
    }

    public void setListaDesideri(ListaDesideri listaDesideri) {
        this.listaDesideri = listaDesideri;
    }

    public Piattaforma getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(Piattaforma piattaforma) {
        this.piattaforma = piattaforma;
    }
}
