package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.CarrelloGioco;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Exception.ExceptionAddGioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Repository.CarrelloGiocoRepository;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class GiocoService {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private carrelloService carrelloService;

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    public List<Gioco> showGiochi(String titolo){
        List<Gioco> giochi;
        if (titolo == null || titolo.isEmpty()){
            giochi = giocoRepository.findAll();
        }else {
            giochi = giocoRepository.findByTitoloContainingIgnoreCase(titolo);
        }
        return giochi;
    }

    public Gioco addGioco(Gioco giocoForm, List<Integer> piattaformaSelezionataId){
        try{
            //Salvo la/e piattaforma selezionata
            List<Piattaforma> piattaformaSelezionata = piattaformaRepository.findAllById(piattaformaSelezionataId);
            giocoForm.setPiattaforma(piattaformaSelezionata);
            return giocoRepository.save(giocoForm);
        }catch (Exception ex) {
            throw new ExceptionAddGioco("CG_500_DATI_INSERITI_PRESENTI_NEL_SISTEMA");
        }
    }

    public void cancellaGioco(Integer id, Principal principal){
        Optional<Gioco> gioco = giocoRepository.findById(id);
        if (!gioco.isPresent()){
            throw new NessunGiocoTrovato("CG400_ID_PASSATO_NON_VALIDO");
        }
        //Cerchiamo se il gioco è in qualche carrello
        Optional<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findByIdGiocoCarrello(id);
        if (carrelloGioco.isPresent()){
            carrelloService.cancellaGiocoCarrello(carrelloGioco.get().getId_carrelloGioco(),principal);
        }
        giocoRepository.deleteById(id);
    }

    //Modifica Gioco
    public Gioco editGioco(Gioco editFormGioco, List<Integer> selezionePiattaformaID) {
        //Salvo la/e piattaforma selezionata
        List<Piattaforma> piattaformaSelezionata = piattaformaRepository.findAllById(selezionePiattaformaID);
        editFormGioco.setPiattaforma(piattaformaSelezionata);
        editFormGioco.setTitolo(editFormGioco.getTitolo());
        editFormGioco.setDescrizione(editFormGioco.getDescrizione());
        editFormGioco.setPrezzo(editFormGioco.getPrezzo());
        editFormGioco.setCodiceProdotto(editFormGioco.getCodiceProdotto());
        editFormGioco.setQuantita(editFormGioco.getQuantita());
        editFormGioco.setSoftwareHouse(editFormGioco.getSoftwareHouse());
        return giocoRepository.save(editFormGioco);
    }
}
