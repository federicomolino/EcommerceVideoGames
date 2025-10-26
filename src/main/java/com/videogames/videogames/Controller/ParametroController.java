package com.videogames.videogames.Controller;

import com.videogames.videogames.Dto.ParametriDto;
import com.videogames.videogames.Dto.ParametriForm;
import com.videogames.videogames.Entity.Parametro;
import com.videogames.videogames.Repository.ParametroRepository;
import com.videogames.videogames.Service.ParametroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/parametro")
public class ParametroController {

    private ParametroRepository parametroRepository;
    private ParametroService parametroService;

    @Autowired
    public ParametroController(ParametroRepository parametroRepository, ParametroService parametroService){
        this.parametroRepository = parametroRepository;
        this.parametroService = parametroService;
    }

    @GetMapping
    public String IndexParametro(@RequestParam(name = "parametro", required = false) String parametro,
                                 Model model){
        List<Parametro> parametroList = parametroRepository.findAll();
        model.addAttribute("parametroList", parametroList);

        // la lista di DTO con stato iniziale
        List<ParametriDto> dtoList = parametroList.stream().map(p -> {
            ParametriDto dto = new ParametriDto();
            dto.setIdParam(p.getIdParametro());
            dto.setValore(p.getValoreParametro()); // true o false dal database
            return dto;
        }).collect(Collectors.toList());

        ParametriForm parametriForm = new ParametriForm();
        parametriForm.setParametri(dtoList);

        model.addAttribute("parametriForm", parametriForm);
        model.addAttribute("parametroList", parametroService.showParametro(parametro));
        return "Parametro/IndexParametro";
    }

    @PostMapping("/aggiornaParametro")
    public String AggiornaParametro(@ModelAttribute ParametriForm parametriForm, RedirectAttributes redirectAttributes){
        List<ParametriDto> parametri = parametriForm.getParametri();

        if (parametri == null || parametri.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Nessun Parametro Selezionato");
            return "redirect:/parametro";
        }
        parametroService.aggiornaValoreParametro(parametri);

        redirectAttributes.addFlashAttribute("successMessage",
                "Salvataggio Eseguito Correttamente");
       return "redirect:/parametro";
    }

    @GetMapping("/aggiungiParamentro")
    public String AggiungiParametroIndex(Model model){
        ParametriForm parametroObj = new ParametriForm();
        parametroObj.setParametri(new ArrayList<>());
        parametroObj.getParametri().add(new Parametro());

        model.addAttribute("parametroObj", parametroObj);
        return "Parametro/AggiungiParametro";
    }

    @PostMapping("/aggiungiParamentro")
    public String AggiungiParametro(@ModelAttribute("parametroObj") ParametriForm parametriForm, RedirectAttributes
                                    redirectAttributes){
        for (ParametriDto p : parametriForm.getParametri()){
            if(p.getCodiceValore().isBlank()){
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Codici Passati Non Validi, non si accettano stringhe vuote");
                return "redirect:/parametro/aggiungiParamentro";
            }
        }
        try {
            parametroService.aggiungiParametri(parametriForm.getParametri());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Salvataggio Eseguito Correttamente");
        }catch (IllegalArgumentException ex){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Codice Già presente");
            return "redirect:/parametro/aggiungiParamentro";
        }catch (NullPointerException ex){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Nessun Valore Passato");
            return "redirect:/parametro/aggiungiParamentro";
        }
        return "redirect:/parametro";
    }
}
