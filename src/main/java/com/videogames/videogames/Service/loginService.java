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

    public boolean VerificaIban(String iban){

        if(iban == null || iban.isEmpty()){
            throw new IllegalArgumentException();
        }

        boolean isValido = false;
        String formatIban = iban.trim().toUpperCase();
        //Lunghezza minima di 15 e massima di 34
        int checkIban = formatIban.length();
        if(checkIban < 15 || checkIban > 34 ){
            return !isValido;
        }
        //Caratteri ammessi solo A-Z e 0-9
        for (int i = 0; i < formatIban.length(); i++){
            char carattere = formatIban.charAt(i);
            if (!((carattere >= 'A' && carattere <= 'Z')
                    || (carattere >= '0' && carattere <= '9'))) {
                //trovato carattere non corretto
                return !isValido;
            }
        }

        //Se mi torna 1 l'iban è valido
        isValido = verificaIbanMod9710(formatIban) == 1;
        return !isValido;
    }

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
        utenteDaModificare.setIban(utente.getIban());
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

    public void cancellaUtente(Integer id_utente){
        Utente utente = utenteRepository.findById(id_utente).get();
        Carrello carrelloUtente = carrelloRepository.findById(id_utente).get();
        carrelloRepository.delete(carrelloUtente);
        utenteRepository.delete(utente);
    }

    private static int verificaIbanMod9710(String iban){
        /*
        Algoritmo di verifica MOD 97-10 (standard internazionale)
        È la verifica più importante.
        Funziona così:
        Sposta le prime 4 cifre dell’IBAN alla fine
        Trasforma le lettere in numeri (A=10, B=11, ..., Z=35)
        Ottieni un numero molto lungo
        Calcola numero % 97
        Un IBAN è valido se e solo se:
        iban_convertito % 97 == 1
        Questo è il check ufficiale della UE.*/
        String ibanConCifreAllaFine = iban.substring(4) + iban.substring(0,4);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i<ibanConCifreAllaFine.length(); i++){
            char c = ibanConCifreAllaFine.charAt(i);
            if(c >= 'A' && c <='Z'){
                int val = c - 'A' + 10; //A=10 B=11 ecc..
                result.append(val);
            }else{
                result.append(c); //Se numero il valore sarà sempre quello
            }
        }

        String numericiIban = result.toString();

        //Calcolo MOD97 a blocchi
        int remaider = 0;
        for (int i = 0; i < numericiIban.length(); i +=7){
            int end = Math.min(i + 7, numericiIban.length());
            String block = numericiIban.substring(i,end);
            remaider = (remaider * (int)Math.pow(10, block.length()) + Integer.parseInt(block)) % 97;
        }

        return remaider;
    }
}
