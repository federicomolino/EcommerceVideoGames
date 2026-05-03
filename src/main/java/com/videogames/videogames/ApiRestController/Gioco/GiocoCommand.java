package com.videogames.videogames.ApiRestController.Gioco;

import java.time.LocalDate;

public interface GiocoCommand {
    String getTitolo();
    String getDescrizione();
    String getSoftwareHouse();
    LocalDate getDataUscitaGioco();
    long getCodiceProdotto();
    String getKeyAttivazione();
    double getPrezzo();
    int getQuantita();
    String getUsername();
}
