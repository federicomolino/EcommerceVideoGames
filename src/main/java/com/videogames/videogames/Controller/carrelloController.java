package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.CarrelloGioco;
import com.videogames.videogames.Entity.Gioco;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Exception.QuantitaInsufficenteException;
import com.videogames.videogames.Repository.CarrelloGiocoRepository;
import com.videogames.videogames.Repository.giocoRepository;
import com.videogames.videogames.Service.carrelloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrello")
public class carrelloController {

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private giocoRepository giocoRepository;

    @Autowired
    private carrelloService carrelloService;

    @GetMapping()
    public String showCarrello(Model model, Principal principal){
        Optional<Utente> u = carrelloService.recuperoUtente(principal);
        List<CarrelloGioco> carrello = carrelloGiocoRepository.findByUtente(u);
        double prezzo = carrelloService.prezzoFinaleCarrello(principal);
        //Trasformo il prezzo in modo che abbia due decimali
        DecimalFormat priceDecimal = new DecimalFormat("#.##");
        String priceDecimalFormatter = priceDecimal.format(prezzo);

        model.addAttribute("listCarrello", carrello);
        model.addAttribute("prezzoTotale", priceDecimalFormatter);
        return "Carrello/carrello";
    }

    @PostMapping("/add/{id}")
    public String giocoAlCarrello(@PathVariable("id") Integer id, Principal principal,
                                  RedirectAttributes redirectAttributes){
        //Mi recupero il gioco
        Gioco optGioco = giocoRepository.findById(id).get();
        if (optGioco.getQuantita() <= 0) {
            redirectAttributes.addFlashAttribute("erroreCarrello", "Gioco esaurito");
            return "redirect:/"; // o la pagina di dettaglio del gioco
        }

        carrelloService.addCarrelloGioco(id, principal);
        return "redirect:/carrello";
    }

    @PostMapping("/delete/{id}")
    public String cancellaGiocoCarrello(@PathVariable("id") Integer id){
        carrelloService.cancellaGiocoCarrello(id);
        return "redirect:/carrello";
    }

    @GetMapping("/edit/{id}")
    public String showPageEditCarrello(@PathVariable("id") Integer id, Model model){
        Optional<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findById(id);
        model.addAttribute("carrelloGioco",carrelloGioco.get());
        model.addAttribute("formEditCarrello", carrelloGioco.get());
        return "Carrello/editCarrello";
    }

    @PostMapping("/edit/{id}")
    public String carrelloGiocoEdit(@PathVariable("id") Integer id, @Valid
                                    @ModelAttribute("formEditCarrello") CarrelloGioco formEditCarrello,
                                    BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "Carrello/editCarrello";
        }
        try {
            carrelloService.modificaQuantitaCarrello(id, formEditCarrello);
        }catch (QuantitaInsufficenteException ex){
            CarrelloGioco carrelloGioco = carrelloGiocoRepository.findById(id).get();
            bindingResult.rejectValue("quantita", "errorQuantita",
                    "Nel magazzino i prodotti ancora disponibili sono " + ex.getDisponibilitaMagazzino());
            model.addAttribute("carrelloGioco", carrelloGioco);
            model.addAttribute("formEditCarrello", formEditCarrello);
            return "Carrello/editCarrello";
        }
        return "redirect:/carrello";
    }
}
