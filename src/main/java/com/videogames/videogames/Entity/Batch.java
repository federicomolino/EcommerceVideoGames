package com.videogames.videogames.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity(name = "Table_batch")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBatch;

    @NotNull
    private String nomeBatch;

    @NotNull
    private boolean isAttivo;

    private LocalDateTime ultimoGiroBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    @NotNull
    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getNomeBatch() {
        return nomeBatch;
    }

    public void setNomeBatch(String nomeBatch) {
        this.nomeBatch = nomeBatch;
    }

    public boolean isAttivo() {
        return isAttivo;
    }

    public void setAttivo(boolean attivo) {
        isAttivo = attivo;
    }

    public LocalDateTime getUltimoGiroBatch() {
        return ultimoGiroBatch;
    }

    public void setUltimoGiroBatch(LocalDateTime ultimoGiroBatch) {
        this.ultimoGiroBatch = ultimoGiroBatch;
    }


    public int getIdBatch() {
        return idBatch;
    }

    public void setIdBatch(int idBatch) {
        this.idBatch = idBatch;
    }
}
