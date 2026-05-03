package com.videogames.videogames.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TableLogAPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idLog;
    @Lob
    private String request;
    @Lob
    private String response;
    private LocalDateTime date;
    private String servizio;
    private String responseType;

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getServizio() {
        return servizio;
    }

    public void setServizio(String servizio) {
        this.servizio = servizio;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
