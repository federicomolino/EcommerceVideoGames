package com.videogames.videogames.Batch;

import com.videogames.videogames.Service.Batch.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchPiattaforma{

    protected final String nameBatchPiattaforma = "BATCH_PIATTAFORMA";

    private final BatchService batchPiattaformaService;

    @Autowired
    public BatchPiattaforma(BatchService batchPiattaformaService){
        this.batchPiattaformaService = batchPiattaformaService;
    }

    /*
    * secondi minuti ore giornoMese mese giornoSettimana
       0       0/5    *     *         *       *
    */
    @Scheduled(cron = "0 */2 * * * *")
    public void excuteBatch(){

        List<BatchDTO> batchDto = batchPiattaformaService.isBatchActive(nameBatchPiattaforma);
        if (batchDto == null || batchDto.isEmpty()){
            System.out.println(nameBatchPiattaforma + " nessun batch presente");
            return;
        }

        try {
            batchPiattaformaService.creaPiattaforma(batchDto);
        }catch (IllegalArgumentException ex){
            System.out.println("batch in errore");
        }
        catch (Exception ex){
            System.out.println("batch in errore");
        }
    }
}
