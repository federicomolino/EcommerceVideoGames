package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.giocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class serviceGioco {

    @Autowired
    private giocoRepository giocoRepository;

    public Gioco addGioco(Gioco giocoForm){
       return giocoRepository.save(giocoForm);
    }

    public void cancellaGioco(Integer id){
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
