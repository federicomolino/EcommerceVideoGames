package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.CarrelloGioco;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.CarrelloGiocoRepository;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class serviceGioco {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private carrelloService carrelloService;

    public List<Gioco> showGiochi(String titolo){
        List<Gioco> giochi;
        if (titolo == null || titolo.isEmpty()){
            giochi = giocoRepository.findAll();
        }else {
            giochi = giocoRepository.findByTitoloContainingIgnoreCase(titolo);
        }
        return giochi;
    }

    public Gioco addGioco(Gioco giocoForm){
       return giocoRepository.save(giocoForm);
    }

    public void cancellaGioco(Integer id){
        //Cerchiamo se il gioco è in qualche carrello
        Optional<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findByIdGiocoCarrello(id);
        if (carrelloGioco.isPresent()){
            carrelloService.cancellaGiocoCarrello(carrelloGioco.get().getId_carrelloGioco());
        }
        giocoRepository.deleteById(id);
    }

    //Modifica Gioco
    public Gioco editGioco(Gioco editFormGioco){
        editFormGioco.setTitolo(editFormGioco.getTitolo());
        editFormGioco.setDescrizione(editFormGioco.getDescrizione());
        editFormGioco.setPrezzo(editFormGioco.getPrezzo());
        editFormGioco.setCodiceProdotto(editFormGioco.getCodiceProdotto());
        editFormGioco.setQuantita(editFormGioco.getQuantita());
        editFormGioco.setSoftwareHouse(editFormGioco.getSoftwareHouse());
        return giocoRepository.save(editFormGioco);
    }
}
