package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Piattaforma;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Service.PiattaformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/piattaforma")
public class piattaformaController {

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private PiattaformaService piattaformaService;

    @GetMapping()
    public String showPiattaforma(Model model){
        model.addAttribute("formPiattaforma", new Piattaforma());
        model.addAttribute("listPiattaforma", piattaformaRepository.findAll());
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
