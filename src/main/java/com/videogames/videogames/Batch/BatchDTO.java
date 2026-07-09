package com.videogames.videogames.Batch;

import java.time.LocalDateTime;

public class BatchDTO {

    private int idBatch;
    private String nomeBatch;
    private boolean isAttivo;
    private LocalDateTime ultimoGiroBatch;

    public BatchDTO(int idBatch, String nomeBatch, boolean isAttivo, LocalDateTime ultimoGiroBatch) {
        this.idBatch = idBatch;
        this.nomeBatch = nomeBatch;
        this.isAttivo = isAttivo;
        this.ultimoGiroBatch = ultimoGiroBatch;
    }

    public int getIdBatch() {
        return idBatch;
    }

    public void setIdBatch(int idBatch) {
        this.idBatch = idBatch;
    }

    public String getNomeBatch() {
        return nomeBatch;
    }

    public void setNomeBatch(String nomeBatch) {
        this.nomeBatch = nomeBatch;
    }

    public LocalDateTime getUltimoGiroBatch() {
        return ultimoGiroBatch;
    }

    public void setUltimoGiroBatch(LocalDateTime ultimoGiro) {
        this.ultimoGiroBatch = ultimoGiro;
    }

    public boolean isAttivo() {
        return isAttivo;
    }

    public void setAttivo(boolean attivo) {
        isAttivo = attivo;
    }
}
