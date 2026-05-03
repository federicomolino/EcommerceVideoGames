package com.videogames.videogames.ApiRestController;

import java.time.LocalDateTime;

public class BaseCommad {

    public static class BaseCmd{
        public String username;
        public BaseCmd(String username){
            this.username = username;
        }
    }

    public static class BaseResponse {

        public String message;
        public ResponseType responseType;
        public LocalDateTime data;

        public BaseResponse() {}

        public BaseResponse(String message, ResponseType responseType, LocalDateTime data) {
            this.message = message;
            this.responseType = responseType;
            this.data = data;
        }
    }

    public enum ResponseType {
        OK,
        ALERT,
        ERROR
    }
}