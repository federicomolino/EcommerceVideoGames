package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodicePromozionaleService {

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    public CodiciPromozionale addCodicePromoziale(CodiciPromozionale codiciPromozionaleInserito){
        return codicePromozionaleRepository.save(codiciPromozionaleInserito);
    }

    public void cancellaCodicePromozionale(List<Long> idCodiciPromozionali){
        for (Long idCodicePromozionle : idCodiciPromozionali){
            codicePromozionaleRepository.deleteById(idCodicePromozionle);
        }
    }
}
