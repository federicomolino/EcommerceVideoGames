package com.videogames.videogames.GestioneDipendentiInt;

import java.time.LocalDate;
import java.util.List;

public class RequestDipendeteUtente {

    private String nome;

    private String cognome;

    private LocalDate dataDiNascita;

    private String luogoDiNascita;

    private Utente utente;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getLuogoDiNascita() {
        return luogoDiNascita;
    }

    public void setLuogoDiNascita(String luogoDiNascita) {
        this.luogoDiNascita = luogoDiNascita;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    //Utente
    public static class Utente{
        private String username;

        private String password;

        private String email;

        private List<Role> role;

        public List<Role> getRole() {
            return role;
        }

        public void setRole(List<Role> role) {
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Role{
        private String nomeRole;

        public String getNomeRole() {
            return nomeRole;
        }

        public void setNomeRole(String nomeRole) {
            this.nomeRole = nomeRole;
        }
    }
}