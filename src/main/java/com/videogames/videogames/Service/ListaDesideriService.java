package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Repository.ListaDesideriGiocoRepository;
import com.videogames.videogames.Repository.ListaDesideriRepository;
import com.videogames.videogames.Repository.PiattaformaRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ListaDesideriService extends HelpUtente {

    private GiocoRepository giocoRepository;
    private PiattaformaRepository piattaformaRepository;
    private ListaDesideriRepository listaDesideriRepository;
    private ListaDesideriGiocoRepository listaDesideriGiocoRepository;

    public ListaDesideriService(GiocoRepository giocoRepository, PiattaformaRepository piattaformaRepository,
                                ListaDesideriRepository listaDesideriRepository, ListaDesideriGiocoRepository listaDesideriGiocoRepository){
        this.giocoRepository = giocoRepository;
        this.piattaformaRepository = piattaformaRepository;
        this.listaDesideriRepository = listaDesideriRepository;
        this.listaDesideriGiocoRepository = listaDesideriGiocoRepository;
    }

    public void AddListaDesideri(Integer id, int piattaformaId, Utente u){

        //Mi recupero il gioco
        Gioco optGioco = giocoRepository.findById(id).get();
        if (optGioco.getQuantita() <= 0)
            throw new NessunGiocoTrovato("Gioco Non disponibile");

        //Se per il gioco sono associate più piattaforme è necessario selezionarla
        Optional<Piattaforma> p = null;
        if (piattaformaId > 0){
            Optional<Piattaforma> verificaPiattaformaExist =piattaformaRepository.findById(piattaformaId);
            if (verificaPiattaformaExist.isEmpty()){
                throw new NessunaPiattaformaPresente("Piattaforma inserita non valida");
            }
        }

        if(piattaformaId <= 0){
            if(optGioco.getPiattaforma().size() > 1) {
                throw new NessunaPiattaformaPresente("Nessuna piattaforma selezionata");
            }
        }
        //Mi recupero le listeDesideri associate all'utente
        Optional<ListaDesideri> listaDesideri = listaDesideriRepository.findByUtente(u);
        p = piattaformaRepository.findById(piattaformaId);
        Piattaforma piattaforma = p.orElse(null); // piattaforma può essere null

        if (listaDesideri.isEmpty()) {

            ListaDesideri nuovaListaDesideri = new ListaDesideri();

            nuovaListaDesideri.setUtente(u);
            nuovaListaDesideri.setDataCreazione(LocalDateTime.now().withNano(0));
            nuovaListaDesideri.setUltimaModifica(LocalDateTime.now().withNano(0));
            nuovaListaDesideri = listaDesideriRepository.save(nuovaListaDesideri);
            AggiungiListaDesideriGioco(nuovaListaDesideri, optGioco, piattaforma);
        }else {
            listaDesideri.get().setUltimaModifica(LocalDateTime.now().withNano(0));
            AggiungiListaDesideriGioco(listaDesideri.get(), optGioco, piattaforma);
        }
    }


    private void AggiungiListaDesideriGioco(ListaDesideri list, Gioco gioco, @Nullable Piattaforma piattaforma){
        ListaDesideriGioco relazione = new ListaDesideriGioco();

        relazione.setListaDesideri(list);
        relazione.setGioco(gioco);
        relazione.setPiattaforma(piattaforma);

        listaDesideriGiocoRepository.save(relazione);
    }

    public void Rimuovi(Integer id) throws Exception{
        try {
            Optional<ListaDesideriGioco> lst = listaDesideriGiocoRepository.findById(id);
            if (lst.isPresent()){
                listaDesideriGiocoRepository.deleteById(id);
            }
        }catch (Exception ex){
            throw new Exception("Errore generico durante il tentativo di cancellazione");
        }
    }
}
