package com.videogames.videogames.ApiRestController.Piattaforma;

import com.videogames.videogames.ApiRestController.BaseCommad;

import java.time.LocalDateTime;

public class CreaPiattaforma {

    public static class Command extends BaseCommad.BaseCmd implements PiattaformaCommand{

        public String nomePiattaforma;
        public int idPiattaforma;

        public Command (String username){
            super(username);
        }

        @Override
        public int getIdPiattaforma() { return idPiattaforma; }

        @Override
        public String getUsername() { return  username; }

        @Override
        public String getNomePiattaforma() { return nomePiattaforma; }
    }

    public static class Response extends BaseCommad.BaseResponse{

        public Response(String message, BaseCommad.ResponseType responseType){
            super(message, responseType, LocalDateTime.now());
        }
        public int idPiattaforma;
    }
}
