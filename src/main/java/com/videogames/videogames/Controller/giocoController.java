package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Service.GiocoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/gioco")
public class giocoController {

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private GiocoService GiocoService;

    @GetMapping("/newGioco")
    public String showNewGioco(Model model){
        model.addAttribute("formAdd", new Gioco());
        return "gioco/Addgioco";
    }

    @PostMapping("newGioco")
    public String AddGioco(@Valid @ModelAttribute("formAdd") Gioco giocoForm, BindingResult bindingResult){
        if (giocoForm.getTitolo().trim().isEmpty() ||
                giocoForm.getTitolo().equals(giocoRepository.TitleGioco(giocoForm.getTitolo()))){
            bindingResult.rejectValue("titolo","errorTitolo",
                    "Titolo inserito non corretto");

        }else if (giocoForm.getPrezzo() < 0){
            bindingResult.rejectValue("prezzo","errorPrezzo",
                    "Il prezzo non può essere inferiore a 0€");
        }else if (giocoForm.getKeyAttivazione().length() > 20 ||
                giocoForm.getKeyAttivazione().equals(giocoRepository.KeyGioco(giocoForm.getKeyAttivazione()))){
            bindingResult.rejectValue("keyAttivazione","errorkeyAttivazione",
                    "Chiave inserita non valida");
        }else if (giocoForm.getQuantita() < 0){
            bindingResult.rejectValue("quantita","errorquantita",
                    "Quantità non valida");
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
            return "gioco/AddGioco";
        }

        GiocoService.addGioco(giocoForm);
        return "redirect:/";
    }


    @GetMapping("infoGame/{idGioco}")
    public String infoGiocoId(@PathVariable("idGioco") Integer idGioco, Model model){
        Gioco idSingoloGioco = giocoRepository.findById(idGioco).get();
        model.addAttribute("gioco", idSingoloGioco);
        return "gioco/infoGioco";
    }

    @GetMapping("editGioco/{idGioco}")
    public String showEditGioco(@PathVariable("idGioco") Integer idGioco, Model model){
        Optional<Gioco> idSingoloGioco = giocoRepository.findById(idGioco);
        model.addAttribute("gioco", idSingoloGioco.get());
        model.addAttribute("EditFormGioco", idSingoloGioco.get());
        return "gioco/editGioco";
    }

    @PostMapping("editGioco/{idGioco}")
    public String editGioco(@PathVariable("idGioco")Integer idGioco, @Valid  @ModelAttribute("EditFormGioco") Gioco editFormGioco,
                            BindingResult bindingResult){

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

        if (editFormGioco.getPrezzo() < 0){
            bindingResult.rejectValue("prezzo","errorPrezzo",
                    "Il prezzo non può essere inferiore a 0€");
        } else if (editFormGioco.getQuantita() < 0) {
            bindingResult.rejectValue("quantita","quantita",
                    "Quantità non valida");
        }


        if (bindingResult.hasErrors()){
            return "gioco/editGioco";
        }

        GiocoService.editGioco(editFormGioco);
        return "redirect:/gioco/infoGame/" + idGioco;
    }

    @PostMapping("delete/{idGioco}")
    public String cancellaGioco(@PathVariable("idGioco") Integer idGioco){
        GiocoService.cancellaGioco(idGioco);
        return "redirect:/";
    }

}
