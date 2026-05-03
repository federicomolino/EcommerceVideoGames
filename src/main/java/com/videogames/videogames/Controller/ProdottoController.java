package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.CodiciPromozionale;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Recensione;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.GiocoRepository;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Repository.RecensioneRepository;
import com.videogames.videogames.Service.GiocoService;
import com.videogames.videogames.Service.PiattaformaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gioco")
public class ProdottoController {

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private GiocoService GiocoService;

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    @Autowired
    private PiattaformaService piattaformaService;

    @GetMapping("/newGioco")
    public String showNewGioco(Model model){
        model.addAttribute("formAdd", new Gioco());
        model.addAttribute("listPiattaforma",
                piattaformaRepository.findPiattaformaByUtenteId(piattaformaService.GetUtente().getId_utente()));
        return "gioco/Addgioco";
    }


    @PostMapping("newGioco")
    public String AddGioco(@Valid @ModelAttribute("formAdd") Gioco giocoForm, BindingResult bindingResult, Model model,
                           @RequestParam(value = "piattaformeSelezionate", required = false)
                           List<Integer> piattaformaSelezionataId, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            bindingResult.rejectValue("codiceProdotto","errorcodiceProdotto",
                    "Error generico durante l'esecuzione");
            model.addAttribute("listPiattaforma",piattaformaRepository.findAll());
            return  "redirect:/gioco/newGioco";
        }
        try {
            GiocoService.addGioco(giocoForm, piattaformaSelezionataId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Prodotto inserito correttamente");
            return  "redirect:/gioco/newGioco";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossibile procedere con l'inserimento del prodotto" + e.getMessage());
            return  "redirect:/gioco/newGioco";
        }
    }


    @GetMapping("infoGame/{idGioco}")
    public String infoGiocoId(@PathVariable("idGioco") Integer idGioco, Model model){
        Gioco idSingoloGioco = giocoRepository.findById(idGioco).get();
        List<Recensione> recensioneIdGioco = recensioneRepository.findByGiocoIdGioco(idGioco);
        model.addAttribute("gioco", idSingoloGioco);
        //visualizzo le piattaforme presenti per il gioco
        model.addAttribute("listPiattaforma", idSingoloGioco.getPiattaforma());
        model.addAttribute("listRecensioni",recensioneIdGioco);
        return "gioco/infoGioco";
    }


    @GetMapping("editGioco/{idGioco}")
    public String showEditGioco(@PathVariable("idGioco") Integer idGioco, Model model,
                                RedirectAttributes redirectAttributes){
        Optional<Gioco> idSingoloGioco = giocoRepository.findById(idGioco);
        try {
            ControlloModificaGioco(idSingoloGioco);
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Errore: " + ex.getMessage());
            return "redirect:/gioco/infoGame/" + idGioco;
        }
        model.addAttribute("gioco", idSingoloGioco.get());
        model.addAttribute("EditFormGioco", idSingoloGioco.get());
        model.addAttribute("listPiattaforma",
                piattaformaRepository.findPiattaformaByUtenteId(piattaformaService.GetUtente().getId_utente()));
        model.addAttribute("listCodicePromozionale", codicePromozionaleRepository.findAll());
        model.addAttribute("codicePromozionale", new CodiciPromozionale());
        return "gioco/editGioco";
    }

    @PostMapping("editGioco/{idGioco}")
    public String editGioco(@PathVariable("idGioco")Integer idGioco, @Valid  @ModelAttribute("EditFormGioco")
                                Gioco editFormGioco, BindingResult bindingResult,
                            @RequestParam(name = "piattaformeSelezionate", required = false) List<Integer> selezionePiattaformaID,
                            Model model, RedirectAttributes redirectAttributes){

        Optional<Gioco> gioco = giocoRepository.findById(idGioco);
        if (bindingResult.hasErrors()){
            model.addAttribute("EditFormGioco", gioco.get());
            model.addAttribute("listPiattaforma", piattaformaRepository.findAll());
            model.addAttribute("listCodicePromozionale", codicePromozionaleRepository.findAll());
            return "gioco/editGioco";
        }

        try{
            GiocoService.editGioco(editFormGioco,selezionePiattaformaID);
            redirectAttributes.addFlashAttribute("successMessage",
                        "Modifica effettuata");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Errore imprevisto durante il tentativo di salvataggio " + e.getMessage());
        }
        return "redirect:/gioco/infoGame/" + idGioco;
    }

    @PostMapping("delete/{idGioco}")
    public String cancellaGioco(@PathVariable("idGioco") Integer idGioco, Principal principal){
        GiocoService.cancellaGioco(idGioco,principal);
        return "redirect:/";
    }

    @PostMapping("newGioco/file")
    public String addGiochiFile(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        if (file.isEmpty()){
            return "redirect:/gioco/newGioco";
        }

        try {
            List<Gioco> listGiochi = GiocoService.parseFile(file);
            giocoRepository.saveAll(listGiochi);
        }catch (IOException | DataAccessException ex){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Errore durante l'importazione del csv");
            return "redirect:/gioco/newGioco";
        }
        redirectAttributes.addFlashAttribute("successMessage",
                "Giochi creati correttamente!!!");
        return  "redirect:/gioco/newGioco";
    }


    private void ControlloModificaGioco(Optional<Gioco> idGioco) throws IllegalAccessException {
        Utente user = GiocoService.GetUtente();
        if (user.getId_utente() != idGioco.get().getUtente().getId_utente())
            throw new IllegalAccessException("Non puoi modificare un gioco non caricato dal tuo utente");
    }
}
