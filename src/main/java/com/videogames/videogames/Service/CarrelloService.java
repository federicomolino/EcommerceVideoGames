package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.QuantitaInsufficenteException;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService extends HelpUtente {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    //Recupero utente
    public Optional<Utente> recuperoUtente(Principal principal){
//      Recupero Utente
//      Quando un utente effettua il login, Spring Security salva il suo nome utente in un oggetto Principal
//      puoi riceverlo automaticamente nel controller semplicemente aggiungendolo come parametro
        String username = principal.getName();
        Optional<Utente> utente = userRepository.findByUsername(username);

        return utente;
    }

    public double prezzoFinaleCarrello(Principal principal, double codicePromozionale){
        Optional<Utente> utente = recuperoUtente(principal);
        Utente user = utente.get();
        Carrello carr = carrelloRepository.findByUtente(user);
        List<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findByUtente(utente);
        double price = 0;

        if (carrelloGioco.isEmpty()){
            carr.setPrezzoFinale(BigDecimal.valueOf(price));
            carrelloRepository.save(carr);
        }
        if (!carrelloGioco.isEmpty()){
            for (CarrelloGioco carrello : carrelloGioco){
                Gioco g = carrello.getGioco();
                price += g.getPrezzo() * carrello.getQuantita();
                carr.setPrezzoFinale(BigDecimal.valueOf(price));
                carrelloRepository.save(carr);
            }
        }
        if (codicePromozionale != 0){
            price -= codicePromozionale;
            carr.setPrezzoFinaleSconto(BigDecimal.valueOf(price));
            carrelloRepository.save(carr);
        }
        if (price < 0){
            price = 0;
            carr.setPrezzoFinaleSconto(BigDecimal.valueOf(price));
            carrelloRepository.save(carr);
        }
        return price;
    }

    public double recuperoScontoApplicato(Principal principal){
        // Recupero utente
        Utente u = recuperoUtente(principal).get();
        // Recupero carrello
        Carrello carr = carrelloRepository.findByUtente(u);
        double scontoApplicato = 0;
        if (carr.getPrezzoFinaleSconto() != null &&
                carr.getPrezzoFinaleSconto().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal sconto = carr.getPrezzoFinale().subtract(carr.getPrezzoFinaleSconto());
            scontoApplicato = sconto.doubleValue();
        }
        return scontoApplicato;
    }

    //Aggiunta al carrello
    public String addCarrelloGioco(Integer id, Principal principal){

        //Mi recupero il gioco
        Gioco optGioco = giocoRepository.findById(id).get();
        //recupero utente
        Utente u = recuperoUtente(principal).get();


        //Se il gioco è già presente nel carrello
        Optional<CarrelloGioco> UtenteAndGioco = carrelloGiocoRepository.findByUtenteAndGioco(u,optGioco);
        if (UtenteAndGioco.isPresent()){
            //Diminuisco la quantià del gioco
            optGioco.setQuantita(optGioco.getQuantita() -1);
            giocoRepository.save(optGioco);

            CarrelloGioco carrelloGioco = UtenteAndGioco.get();
            carrelloGioco.setQuantita(carrelloGioco.getQuantita() +1);
            carrelloGiocoRepository.save(carrelloGioco);

        } else {
            //Diminuisco la quantià del gioco
            optGioco.setQuantita(optGioco.getQuantita() -1);
            giocoRepository.save(optGioco);

            //Aggiungo il gioco
            CarrelloGioco newCarello = new CarrelloGioco();
            newCarello.setUtente(u);
            newCarello.setGioco(optGioco);
            newCarello.setQuantita(1);
            carrelloGiocoRepository.save(newCarello);
        }
        prezzoFinaleCarrello(principal,recuperoScontoApplicato(principal));
        return "redirect:/carrello";
    }

    public void cancellaGiocoCarrello(Integer id, Principal principal){
        CarrelloGioco carrelloGioco = carrelloGiocoRepository.findById(id).get();
        Gioco gioco = carrelloGioco.getGioco();
        //Aggiungio la quantità eliminata al magazzino
        int addQuantitaMagazzino = gioco.getQuantita() + carrelloGioco.getQuantita();
        gioco.setQuantita(addQuantitaMagazzino);
        giocoRepository.save(gioco);

        //Elimino il gioco dal carrello
        carrelloGiocoRepository.deleteById(id);
        //Recupero lo sconto precedentemente applicato
        double sconto = recuperoScontoApplicato(principal);
        prezzoFinaleCarrello(principal,sconto);
    }

    public CarrelloGioco modificaQuantitaCarrello(Integer quantita, Principal principal){
        Utente utente = GetUtente();
        if(utente == null)  {
            return null;
        }
        CarrelloGioco carrelloGioco = carrelloGiocoRepository.findByIdUtenteCarrello(utente.getId_utente());
        Gioco idGioco = carrelloGioco.getGioco();

        if (carrelloGioco.getQuantita() < quantita){
            //Diminusco dal magazzino
            int quantitaRemoveMagazzino = quantita - carrelloGioco.getQuantita();

            //Verifico se con la modifica nel magazzino i prodotti sono meno di 0
            int totMagazzino = idGioco.getQuantita() - quantitaRemoveMagazzino;

            if (totMagazzino < 0){
                throw new QuantitaInsufficenteException(idGioco.getQuantita());
            }

            idGioco.setQuantita(idGioco.getQuantita() - quantitaRemoveMagazzino);
        }

        if (carrelloGioco.getQuantita() > quantita){
            //Aggiungo al magazzino
            int quantitaAddMagazzino = carrelloGioco.getQuantita() - quantita;
            idGioco.setQuantita(idGioco.getQuantita() + quantitaAddMagazzino);
        }

        carrelloGioco.setQuantita(quantita);
        prezzoFinaleCarrello(principal,recuperoScontoApplicato(principal));
        return carrelloGiocoRepository.save(carrelloGioco);
    }

}
