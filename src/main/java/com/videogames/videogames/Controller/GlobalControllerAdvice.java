package com.videogames.videogames.Controller;

import com.videogames.videogames.Service.ParametroService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private ParametroService parametroService;

    public GlobalControllerAdvice(ParametroService parametroService){
        this.parametroService = parametroService;
    }
    //Grazie a questo metodo, ad ogni controller andiamo a verificare i parametri attivi, in modo da non preoccuparci
    //per nessun redirect

    @ModelAttribute("parametri")
    public Map<String,Boolean> globalParametri(Model model){
        //Inserire il codice Parametro da ricercare
        List<String> codiceParametri = List.of(
                "BARRA_RICERCA",
                "DETTAGLIO_RICERCA",
                "IMPORT_GIOCHI_EXCEL",
                "GEST_DIPENDENTI_INT");

        HashMap<String, Boolean> parametri = parametroService.VerificaFlagParametro(codiceParametri);
        model.addAttribute("parametri", parametri);
        return parametri;
    }
}
