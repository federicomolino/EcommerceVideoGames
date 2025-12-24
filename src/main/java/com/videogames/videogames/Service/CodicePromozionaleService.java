package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodicePromozionaleService {

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    public CodiciPromozionale addCodicePromoziale(CodiciPromozionale codiciPromozionaleInserito, Utente utente){
        codiciPromozionaleInserito.setUtente(utente);
        return codicePromozionaleRepository.save(codiciPromozionaleInserito);
    }

    public void cancellaCodicePromozionale(List<Long> idCodiciPromozionali){
        for (Long idCodicePromozionle : idCodiciPromozionali){
            codicePromozionaleRepository.deleteById(idCodicePromozionle);
        }
    }

    public void riattivaCodiciPromozionali(List<Long> idCodiciPromozionali){
        //Mi prendo i codici passati
        List<CodiciPromozionale> codiciPromozionalePresenti = codicePromozionaleRepository.findAllById(idCodiciPromozionali);

        for (CodiciPromozionale codiciPromozionale : codiciPromozionalePresenti){
            //Riattivo nel caso siano stati già usati
            if (codiciPromozionale.isUsato()){
                codiciPromozionale.setUsato(false);
                codicePromozionaleRepository.save(codiciPromozionale);
            }
        }
    }
}
