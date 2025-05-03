package com.videogames.videogames.Entity;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;

    private String path;

    private LocalDateTime timeStamp;

    public ErrorResponse(String message, String requestURI) {
        this.message = message;
        this.path = requestURI;
        this.timeStamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
