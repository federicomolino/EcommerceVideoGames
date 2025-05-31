package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.utenteRepository;
import com.videogames.videogames.Service.loginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/login")
public class loginController {

    @Autowired
    private utenteRepository utenteRepository;

    @Autowired
    private loginService loginService;

    @GetMapping("register")
    public String RegistraUtente(Model model, Principal principal){
        if (principal != null){
            String username = principal.getName();
            List<Utente> utente = utenteRepository.findByUsername(username);
            model.addAttribute("utente",utente.get(0));
        }

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

    @GetMapping("/editUser")
    public String showEditUtente(Model model,Principal principal){
        String username = principal.getName();
        List<Utente> utente = utenteRepository.findByUsername(username);
        Utente utenteLoggato = utente.get(0);
        model.addAttribute("editUtente", utenteLoggato);
        return "Login/editutente";
    }

    @PostMapping("/editUser")
    public String editUtente(@ModelAttribute("editUtente") Utente editUtente, Principal principal, BindingResult bindingResult,
                             Model model, HttpServletRequest request){
        String username = principal.getName();
        List<Utente> utente = utenteRepository.findByUsername(username);
        Utente utenteLoggato = utente.get(0);

        //Verifico se username e password esistono e sono associati ad altri utenti
        List<Utente> usernameEsistente = utenteRepository.findByUsername(editUtente.getUsername());
        if (!usernameEsistente.isEmpty() && !usernameEsistente.get(0).getId_utente().equals(utenteLoggato.getId_utente())){
            bindingResult.rejectValue("username", "ErrorUsername",
                    "Username già presente nel sitema");
            return "Login/editutente";
        }
        List<Utente> emailEsistente = utenteRepository.findByEmail(editUtente.getEmail());
        if (!emailEsistente.isEmpty() && !emailEsistente.get(0).getId_utente().equals(utenteLoggato.getId_utente())){
            bindingResult.rejectValue("email", "ErrorEmail",
                    "Email già presente nel sitema");
            return "Login/editutente";
        }

        loginService.editUtente(editUtente, principal);
        if (bindingResult.hasErrors()){
            model.addAttribute("editUtente", editUtente);
            return "Login/editutente";
        }
        //Invalidiamo la sessione
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate(); //Invalidiamo la sessione
        }
        SecurityContextHolder.clearContext();//puliamo il contesto di sicurezza

        return "redirect:/login?logout";
    }
}
