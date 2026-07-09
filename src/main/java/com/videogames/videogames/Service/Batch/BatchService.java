package com.videogames.videogames.Service.Batch;

import com.videogames.videogames.Batch.BatchDTO;
import com.videogames.videogames.Entity.Batch;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.BatchRepository;
import com.videogames.videogames.Service.PiattaformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BatchService extends HelpUtente{

    private final BatchRepository batchRepository;
    private final PiattaformaService piattaformaService;

    @Autowired
    public BatchService(BatchRepository batchRepository, PiattaformaService piattaformaService){
        this.batchRepository = batchRepository;
        this.piattaformaService = piattaformaService;
    }

    public List<BatchDTO> isBatchActive(String nameBatch) {
        List<BatchDTO> lstBatchAttivi = batchRepository.findBatchAttiviByNomeBatch(nameBatch);
        if (!lstBatchAttivi.isEmpty()) {
            return lstBatchAttivi;
        }
        return null;
    }

    public List<BatchDTO> getBatchPerUtente(int idUtente){
        if (idUtente <= 0){
            return null;
        }

        List<BatchDTO> batch = batchRepository.findBatchPerUtente(idUtente);
        if (batch.isEmpty()){
            return null;
        }
        return batch;
    }

    public void eliminaBatch(int idBatch){
        if (idBatch <= 0){
            throw new IllegalArgumentException("id Batch non valido");
        }
        batchRepository.deleteById(idBatch);
    }

    public void toggleBatch(int idBatch){
        if (idBatch <= 0){
            throw new IllegalArgumentException("idBatch non valido");
        }
        Optional<Batch> batch = batchRepository.findById(idBatch);
        if (batch.get().isAttivo()){
            batch.get().setAttivo(false);
        }else {
            batch.get().setAttivo(true);
        }
        batchRepository.save(batch.get());
    }

    public IllegalArgumentException creaPiattaforma(List<BatchDTO> batchDto) throws Exception{

        if (batchDto == null || batchDto.isEmpty())
            return new IllegalArgumentException("nessun batch presente in lista");

        for (BatchDTO batch : batchDto){
            Batch b = batchRepository.findById(batch.getIdBatch()).get();
            try {
                Piattaforma p = new Piattaforma();
                p.setNomePiattaforma("provabatch");
                p.setUtente(b.getUtente());
                p.setQuantitaPerPiattaforma(3);
                piattaformaService.newPiattaforma(p, b.getUtente());

                //Mi Salvo il giro del batch
                b.setUltimoGiroBatch(LocalDateTime.now().withNano(0));
                batchRepository.save(b);
            }catch (Exception ex){
                throw new Exception(ex.getMessage());
            }
        }
        return null;
    }
}
