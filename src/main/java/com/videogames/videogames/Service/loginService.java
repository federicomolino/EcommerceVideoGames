package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Carrello;
import com.videogames.videogames.Entity.Role;
import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Repository.CarrelloRepository;
import com.videogames.videogames.Repository.RoleRepository;
import com.videogames.videogames.Repository.utenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class loginService {

    @Autowired
    private utenteRepository utenteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    public Utente newUtente(Utente formUtente, List<String> ruoli){

        //Assegno i ruoli all'utente
        List<Role> ruoliAssengati = new ArrayList<>();
        if (ruoli != null && !ruoli.isEmpty()){
            for (String r : ruoli){
                Role rNome = roleRepository.findByName(r);
                ruoliAssengati.add(rNome);
            }
        }else{
            //Se nessun ruolo, in automatico user
            Role defaultRuolo = roleRepository.findByName("USER");
            ruoliAssengati.add(defaultRuolo);
        }
        formUtente.setRoles(ruoliAssengati);

        //setto la password e poi salvo
        formUtente.setPassword("{noop}" + formUtente.getPassword());

        Utente utente = utenteRepository.save(formUtente);
        //Associo utente ad un carrello
        Carrello carrello = new Carrello();
        carrello.setUtente(formUtente);

        carrelloRepository.save(carrello);
        return utente;
    }

    public Utente editUtente(Utente utente, Principal principal){
        String username = principal.getName();
        List<Utente> utenteEdit = utenteRepository.findByUsername(username);
        Utente utenteDaModificare = utenteEdit.get(0);
        //Modifia Utente
        utenteDaModificare.setUsername(utente.getUsername());
        utenteDaModificare.setName(utente.getName());
        utenteDaModificare.setSurname(utente.getSurname());
        utenteDaModificare.setEmail(utente.getEmail());
        utenteDaModificare.setPassword("{noop}" + utente.getPassword());
        return utenteRepository.save(utenteDaModificare);
    }

    public void disabilitaUtenti(List<Integer> utentiSelezionati){
        List<Utente> tuttiUtenti = utenteRepository.findAll();
        if (utentiSelezionati == null){
            for (Utente u : tuttiUtenti){
                u.setDisabilitaUtente(false);
            }
        }else {
            for (Utente utente : tuttiUtenti){
                // Se l'utente è selezionato, lo disabilito (flag a true)
                if (utentiSelezionati.contains(utente.getId_utente())){
                    utente.setDisabilitaUtente(true);
                } else {
                    // Altrimenti lo abilito (flag a false)
                    utente.setDisabilitaUtente(false);
                }
            }
        }
        utenteRepository.saveAll(tuttiUtenti);
    }
}
