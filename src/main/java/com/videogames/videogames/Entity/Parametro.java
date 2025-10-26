package com.videogames.videogames.Entity;

import com.videogames.videogames.Dto.ParametriDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Parametro extends ParametriDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idParametro;

    private String descrizioneParamentro;

    private Boolean valoreParametro;

    private String codiceValore;

    public String getCodiceValore() {
        return codiceValore;
    }

    public void setCodiceValore(String codiceValore) {
        this.codiceValore = codiceValore;
    }

    public Integer getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public String getDescrizioneParamentro() {
        return descrizioneParamentro;
    }

    public void setDescrizioneParamentro(String descrizioneParamentro) {
        this.descrizioneParamentro = descrizioneParamentro;
    }

    public Boolean getValoreParametro() {
        return valoreParametro;
    }

    public void setValoreParametro(Boolean valoreParametro) {
        this.valoreParametro = valoreParametro;
    }
}
