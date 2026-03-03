package com.videogames.videogames.Dto;

import jakarta.validation.constraints.NotNull;

public class CarrelloDto {

    public static class AggiungiAlCarrelloRequest{

        private Integer piattaformaId;

        @NotNull(message = "Il Valore non può essere nullo")
        private String username;

        public Integer getPiattaformaId() {
            return piattaformaId;
        }

        public void setPiattaformaId(Integer piattaformaId) {
            this.piattaformaId = piattaformaId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
