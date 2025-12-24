package com.videogames.videogames.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Gioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGioco;

    @Column(unique = true)
    private String titolo;

    private String descrizione;

    private String softwareHouse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataUscitaGioco;

    @Column(unique = true)
    private long codiceProdotto;

    @Column(unique = true)
    @Size(max = 20)
    private String keyAttivazione;

    @NotNull
    @Min(value = 0, message = "Valore inserito non valido, non può essere inferiore a 0")
    private double prezzo;

    @Min(value = 0, message = "Quantità non valida")
    private int quantita;

    @JsonIgnore
    @OneToMany(mappedBy = "gioco")
    private List<CarrelloGioco> carrelliAssociati;

    @ManyToMany
    @JoinTable(
            name = "gioco_piattaforma",
            joinColumns = @JoinColumn(name = "gioco_id"),
            inverseJoinColumns = @JoinColumn(name = "piattaforma_id")
    )
    private List<Piattaforma> piattaforma;

    @OneToMany(mappedBy = "gioco")
    @JsonIgnore
    List<Recensione> recensione;

    @OneToMany(mappedBy = "gioco")
    @JsonIgnore
    List<CodiciPromozionale> codiciPromozionali;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<Recensione> getRecensione() {
        return recensione;
    }

    public void setRecensione(List<Recensione> recensione) {
        this.recensione = recensione;
    }

    public List<Piattaforma> getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(List<Piattaforma> piattaforma) {
        this.piattaforma = piattaforma;
    }

    public List<CarrelloGioco> getCarrelliAssociati() {
        return carrelliAssociati;
    }

    public void setCarrelliAssociati(List<CarrelloGioco> carrelliAssociati) {
        this.carrelliAssociati = carrelliAssociati;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getIdGioco() {
        return idGioco;
    }

    public void setIdGioco(int idGioco) {
        this.idGioco = idGioco;
    }

    public long getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(long codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getSoftwareHouse() {
        return softwareHouse;
    }

    public void setSoftwareHouse(String softwareHouse) {
        this.softwareHouse = softwareHouse;
    }

    public String getKeyAttivazione() {
        return keyAttivazione;
    }

    public void setKeyAttivazione(String keyAttivazione) {
        this.keyAttivazione = keyAttivazione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public LocalDate getDataUscitaGioco() {
        return dataUscitaGioco;
    }

    public void setDataUscitaGioco(LocalDate dataUscitaGioco) {
        this.dataUscitaGioco = dataUscitaGioco;
    }

    public List<CodiciPromozionale> getCodiciPromozionali() {
        return codiciPromozionali;
    }

    public void setCodiciPromozionali(List<CodiciPromozionale> codiciPromozionali) {
        this.codiciPromozionali = codiciPromozionali;
    }
}
