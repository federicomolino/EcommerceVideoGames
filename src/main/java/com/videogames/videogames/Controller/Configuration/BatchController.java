package com.videogames.videogames.Controller.Configuration;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Service.Batch.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/configuration")
public class BatchController extends HelpUtente {

    private final BatchService batchService;

    @Autowired
    public BatchController(BatchService batchService){
        this.batchService = batchService;
    }

    @GetMapping()
    public String getViewConfiguration(Model model){
        Utente ute = GetUtente();
        if (ute == null){
            return "redirect:/";
        }

        model.addAttribute("nomeUtente", ute.getName());
        model.addAttribute("cognomeUtente", ute.getSurname());
        model.addAttribute("usernameUtente", ute.getUsername());
        model.addAttribute("lstBatch", batchService.getBatchPerUtente(ute.getId_utente()));
        return "Batch/batchConfiguration";
    }

    @PostMapping("/batch/eliminaBatch")
    public String eliminaBatch(@RequestParam("idBatch") int idBatch){
        try {
            batchService.eliminaBatch(idBatch);
        }catch (Exception e){
            return "redirect:/configuration";
        }
        return "redirect:/configuration";
    }

    @PostMapping("/batch/toggle")
    public String toggleBatch(@RequestParam("idBatch") int idBatch){
        try {
            batchService.toggleBatch(idBatch);
        }catch (Exception e){
            return "redirect:/configuration";
        }
        return "redirect:/configuration";
    }
}
