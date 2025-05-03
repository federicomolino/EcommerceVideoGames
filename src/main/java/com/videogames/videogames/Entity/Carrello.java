package com.videogames.videogames.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_carrello;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @OneToMany(mappedBy = "carrello")
    private List<CarrelloGioco> giochiCarrello;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<CarrelloGioco> getGiochiCarrello() {
        return giochiCarrello;
    }

    public void setGiochiCarrello(List<CarrelloGioco> giochiCarrello) {
        this.giochiCarrello = giochiCarrello;
    }

    public Integer getId_carrello() {
        return id_carrello;
    }

    public void setId_carrello(Integer id_carrello) {
        this.id_carrello = id_carrello;
    }

}
