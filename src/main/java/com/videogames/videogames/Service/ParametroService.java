package com.videogames.videogames.Service;

import com.videogames.videogames.Dto.ParametriDto;
import com.videogames.videogames.Entity.Parametro;
import com.videogames.videogames.Repository.ParametroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ParametroService {

    private ParametroRepository parametroRepository;

    @Autowired
    public ParametroService(ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }


    public List<Parametro> showParametro(String parametro){
        List<Parametro> parametri;
        if (parametro == null || parametro.isEmpty()){
            parametri = parametroRepository.findAll();
        }else {
            parametri = parametroRepository.findBycodiceValoreContainingIgnoreCase(parametro);
        }
        return parametri;
    }

    public void aggiornaValoreParametro(List<ParametriDto> idParametro){
        for(ParametriDto id : idParametro){
            Parametro p = parametroRepository.findById(id.getIdParam()).get();
            boolean valoreDbParametro = p.getValoreParametro();
            if (valoreDbParametro != id.isValore()){
                p.setValoreParametro(id.isValore());
            }
            parametroRepository.save(p);
        }
    }


    @Transactional
    public void aggiungiParametri(List<ParametriDto> parametriDto){
        boolean salvataggioEffettuato = false;

        if(parametriDto == null || parametriDto.isEmpty()){
            throw  new NullPointerException();
        }
        for (ParametriDto p : parametriDto){
            //Verifico se il codice è già esistente
            Optional<Parametro> par = parametroRepository.findParametroByCodiceValore(p.getCodiceValore());
            if (par.isPresent()){
                throw new IllegalArgumentException();
            }
            //Salvataggio
            Parametro parametro = new Parametro();
            parametro.setCodiceValore(p.getCodiceValore().toUpperCase());
            parametro.setDescrizioneParamentro(p.getDescrizioneParamentro());
            parametro.setValoreParametro(p.isValore());
            parametroRepository.save(parametro);
        }
    }

    public HashMap<String, Boolean> VerificaFlagParametro(List<String> codiciParametro){
        HashMap<String, Boolean> codiceValore = new HashMap<>();

        List<Parametro> parm = parametroRepository.findParametroByCodiciValore(codiciParametro);

        //Mi prendo solo i codici Attivi
        for (Parametro p : parm){
            boolean codiceAttivo = p.getValoreParametro();
            if (codiceAttivo){
                codiceValore.put(p.getCodiceValore(), codiceAttivo);
            }
        }

        return codiceValore;
    }
}
