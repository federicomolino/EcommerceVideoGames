package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CodicePromozionaleService {

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    private static final String caratteri = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                            "abcdefghijklmnopqrstuvwxyz" +
                                            "0123456789";

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

    public String GeneraCodice(Integer idUtente){
        boolean isCodicePresente = false;
        Random r = new Random();
        StringBuilder codice = new StringBuilder();
        while (!isCodicePresente){
            for (int i = 0; i <= 29; i++){
                int index = r.nextInt(caratteri.length());
                codice.append(caratteri.charAt(index));
            }
            if (codice.toString().length() == 30){
               if(codicePromozionaleRepository.existsByCodiceAndUtenteId(codice.toString(), idUtente).isPresent()){
                   codice = new StringBuilder();
               }else {
                   isCodicePresente = true;
               }
            }
        }
        return codice.toString();
    }
}
