package com.videogames.videogames.Service;

import com.videogames.videogames.Dto.CarrelloDto;
import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Exception.NessunaPiattaformaPresente;
import com.videogames.videogames.Exception.QuantitaInsufficenteException;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private ListaDesideriGiocoRepository listaDesideriGiocoRepository;

    @Autowired
    private ListaDesideriRepository listaDesideriRepository;
    //Recupero utente
    public Optional<Utente> recuperoUtente(Principal principal){
//      Recupero Utente
//      Quando un utente effettua il login, Spring Security salva il suo nome utente in un oggetto Principal
//      puoi riceverlo automaticamente nel controller semplicemente aggiungendolo come parametro
        String username = principal.getName();
        Optional<Utente> utente = userRepository.findByUsername(username);

        return utente;
    }

    public double prezzoFinaleCarrello(double codicePromozionale, Utente utente){

        Carrello carr = carrelloRepository.findByUtente(utente);
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

    public double recuperoScontoApplicato(Utente utente){
        // Recupero carrello
        Carrello carr = carrelloRepository.findByUtente(utente);
        double scontoApplicato = 0;
        if (carr.getPrezzoFinaleSconto() != null &&
                carr.getPrezzoFinaleSconto().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal sconto = carr.getPrezzoFinale().subtract(carr.getPrezzoFinaleSconto());
            scontoApplicato = sconto.doubleValue();
        }
        return scontoApplicato;
    }

    //Aggiunta al carrello
    public void AddCarrelloGioco(Integer id, int piattaformaId, Utente u){

        //Mi recupero il gioco
        Gioco optGioco = giocoRepository.findById(id).get();
        if (optGioco.getQuantita() <= 0)
            throw new NessunGiocoTrovato("Gioco Non disponibile");

        //Se per il gioco sono associate più piattaforme è necessario selezionarla
        Optional<Piattaforma> p = null;
        if (piattaformaId > 0){
            Optional<Piattaforma> verificaPiattaformaExist =piattaformaRepository.findById(piattaformaId);
            if (!verificaPiattaformaExist.isPresent()){
                throw new NessunaPiattaformaPresente("Piattaforma inserita non valida");
            }
        }

        if(piattaformaId <= 0){
            if(optGioco.getPiattaforma().size() > 1) {
                throw new NessunaPiattaformaPresente("Nessuna piattaforma selezionata");
            }
        }
        //Mi recupero il carrello associato all'utente
        Carrello carrelloPrincipale = carrelloRepository.findByUtente(u);
        p = piattaformaRepository.findById(piattaformaId);

        //Se il gioco è già presente nel carrello
        Optional<CarrelloGioco> UtenteAndGioco;
        if (p.isPresent()){
            UtenteAndGioco = carrelloGiocoRepository.findByUtenteAndGiocoAndPiattaforma(u,optGioco,p.get());
        }else {
            UtenteAndGioco = carrelloGiocoRepository.findByUtenteAndGioco(u,optGioco);
        }
        if (UtenteAndGioco.isPresent()){
            //Diminuisco la quantià del gioco
            optGioco.setQuantita(optGioco.getQuantita() -1);
            giocoRepository.save(optGioco);

            CarrelloGioco carrelloGioco = UtenteAndGioco.get();
            carrelloGioco.setQuantita(carrelloGioco.getQuantita() +1);
            carrelloGioco.setCarrello(carrelloPrincipale);
            if (p.isPresent()){
                carrelloGioco.setPiattaforma(p.get());
            }
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
            newCarello.setCarrello(carrelloPrincipale);
            if (p.isPresent()){
                newCarello.setPiattaforma(p.get());
            }
            carrelloGiocoRepository.save(newCarello);
        }
        prezzoFinaleCarrello(recuperoScontoApplicato(u), u);
    }

    public void SpostaNelCarrello(Integer id, int idListaDesideri, int piattaformaId, Utente utente){
        AddCarrelloGioco(id, piattaformaId, utente);
        List <ListaDesideriGioco> list =
                listaDesideriGiocoRepository.findByUtenteIdAndGioco(utente.getId_utente(), id);
        if (!list.isEmpty()){
            listaDesideriGiocoRepository.deleteById(idListaDesideri);
        }
    }

    public void cancellaGiocoCarrello(Integer id, Utente utente){
        CarrelloGioco carrelloGioco = carrelloGiocoRepository.findById(id).get();
        Gioco gioco = carrelloGioco.getGioco();
        //Aggiungio la quantità eliminata al magazzino
        int addQuantitaMagazzino = gioco.getQuantita() + carrelloGioco.getQuantita();
        gioco.setQuantita(addQuantitaMagazzino);
        giocoRepository.save(gioco);

        //Elimino il gioco dal carrello
        carrelloGiocoRepository.deleteById(id);
        //Recupero lo sconto precedentemente applicato
        double sconto = recuperoScontoApplicato(utente);
        prezzoFinaleCarrello(sconto, utente);
    }

    public CarrelloGioco modificaQuantitaCarrello(Integer quantita, Integer idCarrello, Utente utente){
        if(utente == null)  {
            return null;
        }
        CarrelloGioco carrelloGioco = carrelloGiocoRepository.findById(idCarrello).get();
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
        prezzoFinaleCarrello(recuperoScontoApplicato(utente), utente);
        return carrelloGiocoRepository.save(carrelloGioco);
    }

    public boolean VerificaPrezzoConSconto(int idCarrelloPrincipale, double scontoApplicabile){
        if (scontoApplicabile > 0 ){
            Optional<Carrello> c = carrelloRepository.findById(idCarrelloPrincipale);
            if (c.isPresent()){
                if (c.get().getPrezzoFinaleSconto() != null && c.get().getPrezzoFinaleSconto().doubleValue() > 0){
                    return c.get().getPrezzoFinaleSconto().doubleValue() > scontoApplicabile;
                } else {
                    return c.get().getPrezzoFinale().doubleValue() > scontoApplicabile;
                }
            }
        }
        return false;
    }

    public void VerificaAggiungiAlCarrello(Integer id,
                                           CarrelloDto.AggiungiAlCarrelloRequest aggiungiAlCarrelloRequest){

        if (aggiungiAlCarrelloRequest != null){
            Optional<Utente> utente = userRepository.findByUsername(aggiungiAlCarrelloRequest.getUsername());
            if (utente.isPresent()){
                try{
                    AddCarrelloGioco(id, aggiungiAlCarrelloRequest.getPiattaformaId(), utente.get());
                    return;
                }catch (NessunGiocoTrovato e){
                    throw new NessunGiocoTrovato(e.getMessage());
                }catch (NessunaPiattaformaPresente e){
                    throw new NessunaPiattaformaPresente(e.getMessage());
                }
            }
            throw new UsernameNotFoundException("Utente non valido");
        }
    }
}
