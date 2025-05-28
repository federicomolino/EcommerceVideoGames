package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Service.GiocoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gioco")
public class giocoController {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private GiocoService GiocoService;

    @GetMapping("/newGioco")
    public String showNewGioco(Model model){
        model.addAttribute("formAdd", new Gioco());
        model.addAttribute("listPiattaforma",piattaformaRepository.findAll());
        return "gioco/Addgioco";
    }


    @PostMapping("newGioco")
    public String AddGioco(@Valid @ModelAttribute("formAdd") Gioco giocoForm, BindingResult bindingResult, Model model,
                           @RequestParam(value = "piattaformeSelezionate", required = false)
                           List<Integer> piattaformaSelezionataId){
        if (giocoForm.getTitolo().trim().isEmpty() ||
                giocoForm.getTitolo().equals(giocoRepository.TitleGioco(giocoForm.getTitolo()))){
            bindingResult.rejectValue("titolo","errorTitolo",
                    "Titolo inserito non corretto");

        }else if (giocoForm.getKeyAttivazione().length() > 20 ||
                giocoForm.getKeyAttivazione().equals(giocoRepository.KeyGioco(giocoForm.getKeyAttivazione()))){
            bindingResult.rejectValue("keyAttivazione","errorkeyAttivazione",
                    "Chiave inserita non valida");
        }
        Optional<Long> CodiceProdotto = giocoRepository.findcodiceProdottoGioco(giocoForm.getCodiceProdotto());
        if (CodiceProdotto.isPresent()) {
            long codice = CodiceProdotto.get();
            if (codice == giocoForm.getCodiceProdotto()){
                bindingResult.rejectValue("codiceProdotto","errorcodiceProdotto",
                        "Codice Prodotto non valido");
            }
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("listPiattaforma",piattaformaRepository.findAll());
            return "gioco/AddGioco";
        }

        GiocoService.addGioco(giocoForm, piattaformaSelezionataId);
        return "redirect:/";
    }


    @GetMapping("infoGame/{idGioco}")
    public String infoGiocoId(@PathVariable("idGioco") Integer idGioco, Model model){
        Gioco idSingoloGioco = giocoRepository.findById(idGioco).get();
        model.addAttribute("gioco", idSingoloGioco);
        //visualizzo le piattaforme presenti per il gioco
        model.addAttribute("listPiattaforma", idSingoloGioco.getPiattaforma());
        return "gioco/infoGioco";
    }

    @GetMapping("editGioco/{idGioco}")
    public String showEditGioco(@PathVariable("idGioco") Integer idGioco, Model model){
        Optional<Gioco> idSingoloGioco = giocoRepository.findById(idGioco);
        model.addAttribute("gioco", idSingoloGioco.get());
        model.addAttribute("EditFormGioco", idSingoloGioco.get());
        model.addAttribute("listPiattaforma", piattaformaRepository.findAll());
        return "gioco/editGioco";
    }

    @PostMapping("editGioco/{idGioco}")
    public String editGioco(@PathVariable("idGioco")Integer idGioco, @Valid  @ModelAttribute("EditFormGioco")
                                Gioco editFormGioco, BindingResult bindingResult,
                            @RequestParam(name = "piattaformeSelezionate", required = false) List<Integer> selezionePiattaformaID,
                            Model model){

        Optional<Gioco> gioco = giocoRepository.findById(idGioco);
        if (gioco.isPresent()){
            if (!editFormGioco.getTitolo().equals(gioco.get().getTitolo())){
                bindingResult.rejectValue("titolo", "errorTitolo",
                        "Titolo non valido");
            } else if (editFormGioco.getCodiceProdotto() != gioco.get().getCodiceProdotto()) {
                bindingResult.rejectValue("codiceProdotto", "errorCodiceProdotto",
                        "Codice Prodotto non valido");
            }
        }

        if (bindingResult.hasErrors()){
            return "gioco/editGioco";
        }

        if (selezionePiattaformaID == null){
            //Salvo che non c'è nulla
            editFormGioco.setPiattaforma(new ArrayList<>());
            giocoRepository.save(editFormGioco);
            model.addAttribute("gioco",editFormGioco);
            model.addAttribute("listPiattaforma",piattaformaRepository.findAll());
            return "redirect:/gioco/infoGame/" + idGioco;
        }
        GiocoService.editGioco(editFormGioco,selezionePiattaformaID);
        return "redirect:/gioco/infoGame/" + idGioco;
    }

    @PostMapping("delete/{idGioco}")
    public String cancellaGioco(@PathVariable("idGioco") Integer idGioco){
        GiocoService.cancellaGioco(idGioco);
        return "redirect:/";
    }

}
