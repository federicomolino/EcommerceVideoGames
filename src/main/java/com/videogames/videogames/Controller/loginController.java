package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.utenteRepository;
import com.videogames.videogames.Service.loginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/login")
public class loginController {

    @Autowired
    private utenteRepository utenteRepository;

    @Autowired
    private loginService loginService;

    @GetMapping("register")
    public String RegistraUtente(Model model){
        model.addAttribute("UtenteForm", new Utente());
        return "Login/registerUtente";
    }

    @PostMapping("register")
    public String newUtente(@Valid @ModelAttribute("UtenteForm") Utente utenteForm, BindingResult bindingResult,
                            @RequestParam(required = false, name = "ruoli")List<String> ruoli){

        List<Utente> utenteUsername = utenteRepository.findByUsername(utenteForm.getUsername());
        List<Utente> utenteEmail = utenteRepository.findByEmail(utenteForm.getEmail());
        if (!utenteUsername.isEmpty()){
            bindingResult.rejectValue("username","errorUsername",
                    "Username inserito già usato");
            return "Login/registerUtente";
        }else if (!utenteEmail.isEmpty()){
            bindingResult.rejectValue("email","errorEmail",
                    "Email inserita già presente nel sistema");
            return "Login/registerUtente";
        }

        if (bindingResult.hasErrors()){
            return "Login/registerUtente";
        }


        loginService.newUtente(utenteForm, ruoli);
        return "redirect:/";
    }

    @GetMapping()
    public String login(Model model){
        model.addAttribute("UtenteFormLogin", new Utente());
        return "Login/login";
    }
}
