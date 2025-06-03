package com.videogames.videogames.Controller;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.CarrelloRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/login")
public class loginController {

    @Autowired
    private utenteRepository utenteRepository;

    @Autowired
    private loginService loginService;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @GetMapping()
    public String login(Model model){
        model.addAttribute("UtenteFormLogin", new Utente());
        return "Login/login";
    }

    @GetMapping("register")
    public String RegistraUtente(Model model, Principal principal){
        if (principal != null){
            String username = principal.getName();
            List<Utente> utente = utenteRepository.findByUsername(username);
            model.addAttribute("utente",utente.get(0));
            List<Utente> utentiPresenti = utenteRepository.findAll();
            model.addAttribute("utentiPresenti", utentiPresenti);
        }

        model.addAttribute("UtenteForm", new Utente());
        return "Login/registerUtente";
    }

    @PostMapping("register")
    public String newUtente(@Valid @ModelAttribute("UtenteForm") Utente utenteForm, BindingResult bindingResult,
                            @RequestParam(required = false, name = "ruoli")List<String> ruoli,
                            RedirectAttributes redirectAttributes){

        List<Utente> utenteUsername = utenteRepository.findByUsername(utenteForm.getUsername());
        List<Utente> utenteEmail = utenteRepository.findByEmail(utenteForm.getEmail());
        if (!utenteUsername.isEmpty()){
            redirectAttributes.addFlashAttribute("errorUsername","Username inserito già usato");
            return "redirect:/login/register";
        }else if (!utenteEmail.isEmpty()){
            redirectAttributes.addFlashAttribute("errorEmail",
                    "Email inserita già presente nel sistema");
            return "redirect:/login/register";
        }

        if (bindingResult.hasErrors()){
            return "redirect:/login/register";
        }


        loginService.newUtente(utenteForm, ruoli);
        return "redirect:/login/register";
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

    @PostMapping("disabilitaUtenti")
    public String disabilitaUtente(@RequestParam(name = "utentiSelezionati", required = false) List<Integer> utentiSelezionati,
                                   RedirectAttributes redirectAttributes){
        loginService.disabilitaUtenti(utentiSelezionati);
        redirectAttributes.addFlashAttribute("salvataggioConSuccesso", true);
        return "redirect:/login/register";
    }

    @PostMapping("/register/delete/{id_utente}")
    public String cancellaUtente(@PathVariable("id_utente") Integer id_utente){
        loginService.cancellaUtente(id_utente);
        return "redirect:/login/register";
    }
}
