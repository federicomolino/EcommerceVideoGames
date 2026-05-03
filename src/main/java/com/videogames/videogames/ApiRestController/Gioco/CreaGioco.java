package com.videogames.videogames.ApiRestController.Gioco;

import com.videogames.videogames.ApiRestController.BaseCommad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreaGioco {

    public static class Command extends BaseCommad.BaseCmd implements GiocoCommand{
        public String titolo;
        public String descrizione;
        public String softwareHouse;
        public LocalDate dataUscitaGioco;
        public long codiceProdotto;
        public String keyAttivazione;
        public double prezzo;
        public int quantita;
        public List<InfoPiattaforma> piattaforma;


        public Command(String username){
            super(username);
        }

        @Override
        public String getTitolo() { return titolo;}

        @Override
        public String getDescrizione() { return descrizione; }

        @Override
        public String getSoftwareHouse() { return softwareHouse;}

        @Override
        public LocalDate getDataUscitaGioco() { return dataUscitaGioco; }

        @Override
        public long getCodiceProdotto() { return codiceProdotto; }

        @Override
        public String getKeyAttivazione() { return keyAttivazione; }

        @Override
        public double getPrezzo() { return prezzo; }

        @Override
        public int getQuantita() { return quantita; }

        @Override
        public String getUsername() { return username; }
    }

    public static class Response extends BaseCommad.BaseResponse{

        public Response(String message, BaseCommad.ResponseType responseType) {
            super(message, responseType, LocalDateTime.now());
        }
        public int idGioco;
    }

    public static class InfoPiattaforma{
        public int idPiattaforma;
    }

}