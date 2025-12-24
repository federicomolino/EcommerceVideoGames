package com.videogames.videogames.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class CodiciPromozionale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCodicePromozionle;

    @Column(unique = true)
    @Size(max = 30, message = "Lunghezza massima di 30 caratteri")
    private String codicePromozionale;

    private boolean usato;

    private int valoreCodicePromozionale;

    @ManyToOne
    private Gioco gioco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public int getValoreCodicePromozionale() {
        return valoreCodicePromozionale;
    }

    public void setValoreCodicePromozionale(int valoreCodicePromozionale) {
        this.valoreCodicePromozionale = valoreCodicePromozionale;
    }

    public boolean isUsato() {
        return usato;
    }

    public void setUsato(boolean usato) {
        this.usato = usato;
    }

    public Long getIdCodicePromozionle() {
        return idCodicePromozionle;
    }

    public void setIdCodicePromozionle(Long idCodicePromozionle) {
        this.idCodicePromozionle = idCodicePromozionle;
    }

    public String getCodicePromozionale() {
        return codicePromozionale;
    }

    public void setCodicePromozionale(String codicePromozionale) {
        this.codicePromozionale = codicePromozionale;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }
}
