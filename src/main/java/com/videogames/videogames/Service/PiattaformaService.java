package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Repository.PiattaformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PiattaformaService {

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    public Piattaforma newPiattaforma(Piattaforma formPiattaforma){
        return piattaformaRepository.save(formPiattaforma);
    }

    public void deletePiattaforma (Integer id){
        Piattaforma piattaforma = piattaformaRepository.findById(id).get();
         piattaformaRepository.delete(piattaforma);
    }
}
