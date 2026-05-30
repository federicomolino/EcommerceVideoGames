package com.videogames.videogames.ApiRestController.Token;

import com.videogames.videogames.ApiRestController.BaseCommad;

import java.time.LocalDateTime;

public class Token {
    public static class Command extends BaseCommad.BaseCmd{
        public String password;
        public Command(String username){
            super(username);
        }
    }

    public static class Response extends BaseCommad.BaseResponse{
        public Response(String message, BaseCommad.ResponseType responseType){
            super(message, responseType, LocalDateTime.now());
        }
    }
}
