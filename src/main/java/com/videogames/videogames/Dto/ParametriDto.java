package com.videogames.videogames.Dto;

public class ParametriDto {
    private int idParam;
    private boolean valore;
    private String codiceValore;
    private String descrizioneParamentro;

    public String getCodiceValore() {
        return codiceValore;
    }

    public void setCodiceValore(String codiceValore) {
        this.codiceValore = codiceValore;
    }

    public String getDescrizioneParamentro() {
        return descrizioneParamentro;
    }

    public void setDescrizioneParamentro(String descrizioneParamentro) {
        this.descrizioneParamentro = descrizioneParamentro;
    }

    public int getIdParam() {
        return idParam;
    }

    public void setIdParam(int idParam) {
        this.idParam = idParam;
    }

    public boolean isValore() {
        return valore;
    }

    public void setValore(boolean valore) {
        this.valore = valore;
    }
}
