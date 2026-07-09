package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Repository.PiattaformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PiattaformaService extends HelpUtente{

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private GiocoRepository giocoRepository;

    public Piattaforma newPiattaforma(Piattaforma formPiattaforma, Utente utente) {
        ControlliPreSalvataggio(formPiattaforma, utente);
        formPiattaforma.setUtente(utente);
        return piattaformaRepository.save(formPiattaforma);
    }

    public Piattaforma EditPiattaforma(Piattaforma formPiattaforma, Utente utente) {
        ControlliPreSalvataggio(formPiattaforma, utente);
        Optional<Piattaforma> optionalP = piattaformaRepository.findById(formPiattaforma.getId_piattaforma());

        if (optionalP.isPresent()) {
            Piattaforma p = optionalP.get();
            p.setNomePiattaforma(formPiattaforma.getNomePiattaforma());
            p.setUtente(utente);
            return piattaformaRepository.save(p);
        } else {
            throw new NessunaPiattaformaPresente("Piattaforma non trovata per id:" + formPiattaforma.getId_piattaforma());
        }
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

    private void ControlliPreSalvataggio(Piattaforma piattaforma, Utente utente){
        if (utente == null) { throw new RuntimeException("Utente non presente"); }
        if (piattaforma.getNomePiattaforma().trim().isEmpty()) { throw  new RuntimeException("Il campo nomePiattaforma non può essere vuoto");}
    }
}
