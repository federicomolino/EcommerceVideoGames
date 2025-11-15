package com.videogames.videogames.Controller;

import com.videogames.videogames.GestioneDipendentiInt.AutenticazioneToken;
import com.videogames.videogames.GestioneDipendentiInt.CreaDipendenteClient;
import com.videogames.videogames.GestioneDipendentiInt.CreaDipendeteUtenteDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.security.auth.login.LoginException;
import java.time.LocalDate;

@Controller
@RequestMapping("/login/register")
public class RegistrazioneDipendeteInt {

    private AutenticazioneToken autenticazioneToken;
    private CreaDipendenteClient creaDipendenteClient;

    @Autowired
    public RegistrazioneDipendeteInt(AutenticazioneToken autenticazioneToken, CreaDipendenteClient creaDipendenteClient){
        this.autenticazioneToken = autenticazioneToken;
        this.creaDipendenteClient = creaDipendenteClient;
    }


    @GetMapping("/CreaDipendente")
    public String GetShowCreaNuovoDipendente(Model model){
        model.addAttribute("DipendenteForm", new CreaDipendeteUtenteDTO());
        return "Login/CreaDipendente";
    }

    @PostMapping("/CreaDipendente")
    public String CreaNuovoDipendente(@Valid @ModelAttribute("DipendenteForm") CreaDipendeteUtenteDTO creaDipendeteUtenteDTO,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes){
        //Modello non conforme
        if(bindingResult.hasErrors()){
            return "Login/CreaDipendente";
        }

        int anno = LocalDate.now().getYear();
        int annoPassato = creaDipendeteUtenteDTO.getDataDiNascita().getYear();
        int maggiorenni = anno - annoPassato;
        //Verifica ruoli e data
        if(creaDipendeteUtenteDTO.getDataDiNascita() == null || maggiorenni < 18
                && creaDipendeteUtenteDTO.getRuoli() == null){
            redirectAttributes.addFlashAttribute("errorMessage","Ruoli o Data di Nascita " +
                    "non valida!");
        }
        //Proviamo l'autenticazione
        String token ="";
        try{
            token = autenticazioneToken.autenticazione();
        }catch (LoginException ex){
            redirectAttributes.addFlashAttribute("errorMessage","Impossibile Procedere con " +
                    "la creazione del Dipendente, Richiedere Assistenza");
            return "redirect:/login/register/CreaDipendente";
        }
        //proviamo la crazione del dipendente
        try {
            creaDipendenteClient.creaDipendente(creaDipendeteUtenteDTO, token);
            redirectAttributes.addFlashAttribute("successMessage","Creazione Completata");
            return "redirect:/login/register/CreaDipendente";
        }catch (HttpClientErrorException.Unauthorized e ){
            redirectAttributes.addFlashAttribute("errorMessage","Impossibile Procedere con " +
                    "la creazione del Dipendente, Richiedere Assistenza");
            return "redirect:/login/register/CreaDipendente";
        }catch (HttpClientErrorException.BadRequest ex){
            redirectAttributes.addFlashAttribute("errorMessage","Utente già " +
                    "presente con i dati inseriti");
            return "redirect:/login/register/CreaDipendente";
        }catch (Exception genericEx){
            redirectAttributes.addFlashAttribute("errorMessage","Richiedere Assistenza");
            return "redirect:/login/register/CreaDipendente";
        }
    }
}
