package com.videogames.videogames.ApiRestController.Gioco;

import com.videogames.videogames.ApiRestController.BaseCommad;
import com.videogames.videogames.Entity.Gioco;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecuperaGiochi {

    public static class Response extends BaseCommad.BaseResponse {

        private List<GiocoDto> giochi;

        public Response(String message,
                        BaseCommad.ResponseType responseType,
                        List<GiocoDto> giochi) {
            super(message, responseType, LocalDateTime.now());
            this.giochi = giochi;
        }

        public List<GiocoDto> getGiochi() {
            return giochi;
        }

        public static class GiocoDto {

            private int idGioco;
            private long codiceProdotto;
            private String descrizione;
            private String keyAttivazione;
            private double prezzo;
            private String softwareHouse;
            private String titolo;
            private int quantita;
            private LocalDate dataUscitaGioco;
            private String username;

            public GiocoDto(Gioco gioco) {
                this.idGioco = gioco.getIdGioco();
                this.codiceProdotto = gioco.getCodiceProdotto();
                this.descrizione = gioco.getDescrizione();
                this.keyAttivazione = gioco.getKeyAttivazione();
                this.prezzo = gioco.getPrezzo();
                this.softwareHouse = gioco.getSoftwareHouse();
                this.titolo = gioco.getTitolo();
                this.quantita = gioco.getQuantita();
                this.dataUscitaGioco = gioco.getDataUscitaGioco();
            }

            public int getIdGioco() { return idGioco; }
            public long getCodiceProdotto() { return codiceProdotto; }
            public String getDescrizione() { return descrizione; }
            public String getKeyAttivazione() { return keyAttivazione; }
            public double getPrezzo() { return prezzo; }
            public String getSoftwareHouse() { return softwareHouse; }
            public String getTitolo() { return titolo; }
            public int getQuantita() { return quantita; }
            public LocalDate getDataUscitaGioco() { return dataUscitaGioco; }
            public String getUsername() { return username; }
        }
    }
}
