package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PiattaformaService {

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private giocoRepository giocoRepository;

    public Piattaforma newPiattaforma(Piattaforma formPiattaforma){
        return piattaformaRepository.save(formPiattaforma);
    }

    public void deletePiattaforma (Integer id){
        Piattaforma piattaforma = piattaformaRepository.findById(id).get();
        List<Gioco> gioco = piattaforma.getGioco();
        if (!gioco.isEmpty()){
            for (Gioco g : gioco){
                g.setPiattaforma(null);
            }
        }
         piattaformaRepository.delete(piattaforma);
    }
}
