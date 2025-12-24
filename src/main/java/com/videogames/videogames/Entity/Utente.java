package com.videogames.videogames.Entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_utente;

    private String name;

    private String surname;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    private boolean disabilitaUtente;

    @Column(unique = true)
    @Length(min = 15, max = 34)
    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utente_roles", // Nome della tabella intermedia
            joinColumns = @JoinColumn(name = "utente_id_utente", referencedColumnName = "id_utente"),
            inverseJoinColumns = @JoinColumn(name = "role_id_role", referencedColumnName = "id_role")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "recensione_id")
    List<Recensione> recensione;

    @OneToMany
    @JoinColumn(name = "utente")
    private List<Carrello> carrello;

    @OneToMany(mappedBy = "utente")
    public List<Gioco> giochi = new ArrayList<>();

    @OneToMany(mappedBy = "utente")
    private List<Piattaforma> piattaforma = new ArrayList<>();

    @OneToMany(mappedBy = "utente")
    private List<CodiciPromozionale> codiciPromozionale = new ArrayList<>();

    public List<CodiciPromozionale> getCodiciPromozionale() {
        return codiciPromozionale;
    }

    public void setCodiciPromozionale(List<CodiciPromozionale> codiciPromozionale) {
        this.codiciPromozionale = codiciPromozionale;
    }

    public List<Piattaforma> getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(List<Piattaforma> piattaforma) {
        this.piattaforma = piattaforma;
    }

    public boolean isDisabilitaUtente() {
        return disabilitaUtente;
    }

    public void setDisabilitaUtente(boolean disabilitaUtente) {
        this.disabilitaUtente = disabilitaUtente;
    }

    public List<Recensione> getRecensione() {
        return recensione;
    }

    public void setRecensione(List<Recensione> recensione) {
        this.recensione = recensione;
    }

    public List<Carrello> getCarrello() {
        return carrello;
    }

    public void setCarrello(List<Carrello> carrello) {
        this.carrello = carrello;
    }

    public List<Gioco> getGiochi() {
        return giochi;
    }

    public void setGiochi(List<Gioco> giochi) {
        this.giochi = giochi;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getId_utente() {
        return id_utente;
    }

    public void setId_utente(Integer id_utente) {
        this.id_utente = id_utente;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

