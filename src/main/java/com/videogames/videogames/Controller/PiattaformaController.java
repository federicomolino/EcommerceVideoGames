package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Service.PiattaformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/piattaforma")
public class PiattaformaController extends HelpUtente {

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private PiattaformaService piattaformaService;

    @GetMapping()
    public String showPiattaforma(Model model, Principal principal){
        if(principal.getName().equalsIgnoreCase("guest")){
            model.addAttribute("listPiattaforma", piattaformaRepository.findAll());
        }else{
            Utente utente = GetUtente();
            model.addAttribute("listPiattaforma",
                    piattaformaRepository.findPiattaformaByUtenteId(utente.getId_utente()));
        }
        model.addAttribute("formPiattaforma", new Piattaforma());
        return "Piattaforma/indexPiattaforma";
    }

    @PostMapping("/newPiattaforma")
    public String AddPiattaforma(@ModelAttribute("formPiattaforma") Piattaforma formPiattaforma, BindingResult bindingResult, Model model){
        List<Piattaforma> piattaforma = piattaformaRepository.findAll();

        if (formPiattaforma.getNomePiattaforma().trim().equals("")){
            bindingResult.rejectValue("nomePiattaforma", "errorPiattaforma",
                    "Il nome della piattaforma non può essere vuoto");
        }

        if (!piattaforma.isEmpty()){
            for (int i = 0; i < piattaforma.size(); i ++){
                if (formPiattaforma.getNomePiattaforma().equalsIgnoreCase((piattaforma.get(i).getNomePiattaforma()))){
                    bindingResult.rejectValue("nomePiattaforma","errorPiattaforma",
                            "Piattaforma già presente");
                }
            }
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("formPiattaforma", formPiattaforma);
            model.addAttribute("listPiattaforma", piattaforma);
            return "Piattaforma/indexPiattaforma";
        }

        piattaformaService.newPiattaforma(formPiattaforma);
        return "redirect:/piattaforma";
    }

    @PostMapping("/delete/{id_piattaforma}")
    public String cancellaPiattaforma(@PathVariable("id_piattaforma") Integer id_piattaforma){
        piattaformaService.deletePiattaforma(id_piattaforma);
        return "redirect:/piattaforma";
    }
}
