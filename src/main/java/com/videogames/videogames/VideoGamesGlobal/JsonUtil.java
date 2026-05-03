package com.videogames.videogames.VideoGamesGlobal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public JsonUtil(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String toJson(Object obj){
        if (obj == null) return null;

        try {
            return objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            return "Errore Serielizzazione: " + obj.getClass().getSimpleName();
        }
    }
}
