package com.videogames.videogames.VideoGamesGlobal;
import com.videogames.videogames.Entity.TableLogAPI;
import com.videogames.videogames.Repository.TableLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TableLog {
    private TableLogRepository tableLogRepository;
    @Autowired
    public TableLog(TableLogRepository tableLogRepository){
        this.tableLogRepository = tableLogRepository;
    }

    public void InizializzaLog(String request, String response, String servizio, String responseType) {
        TableLogDto tld = new TableLogDto();
        tld.setRequest(request);
        tld.setResponse(response);
        tld.setServizio(servizio);
        tld.setResponseType(responseType);
        NewLog(tld);
    }

    public boolean NewLog(TableLogDto tld) {
        try {
            TableLogAPI tla = CompilaTableLog(tld);
            tableLogRepository.save(tla);
            return true;
        }catch (Exception ex){
            throw new RuntimeException("Errore generico + " + ex.getMessage());
        }
    }

    private TableLogAPI CompilaTableLog(TableLogDto tld){
        TableLogAPI tla = new TableLogAPI();
        tla.setDate(LocalDateTime.now());
        tla.setRequest(tld.getRequest());
        tla.setResponse(tld.getResponse());
        tla.setServizio(tld.getServizio());
        tla.setResponseType(tld.getResponseType());
        return tla;
    }
}
