package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.*;
import com.videogames.videogames.Exception.ExceptionAddGioco;
import com.videogames.videogames.Exception.NessunGiocoTrovato;
import com.videogames.videogames.Helpers.HelpUtente;
import com.videogames.videogames.Repository.CarrelloGiocoRepository;
import com.videogames.videogames.Repository.CodicePromozionaleRepository;
import com.videogames.videogames.Repository.PiattaformaRepository;
import com.videogames.videogames.Repository.GiocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiocoService extends HelpUtente {

    @Autowired
    private GiocoRepository giocoRepository;

    @Autowired
    private CarrelloGiocoRepository carrelloGiocoRepository;

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private PiattaformaRepository piattaformaRepository;

    @Autowired
    private CodicePromozionaleRepository codicePromozionaleRepository;

    public List<Gioco> showGiochi(String titolo, Principal principal){
        List<Gioco> giochi;
        boolean isUser = false;
        Utente utente = GetUtente();
        if (utente != null){
             isUser = utente.getRoles()
                    .stream()
                    .anyMatch(role -> "USER".equalsIgnoreCase(role.getName()));
        }
        if(principal.getName().equalsIgnoreCase("guest") || isUser){
            giochi = giocoRepository.findAll();
        }else {
            Utente user = GetUtente();
            giochi = giocoRepository.findGiochiByUtenteId(user.getId_utente());
        }
        if (titolo != null && titolo.isEmpty()){
            giochi = giocoRepository.findByTitoloContainingIgnoreCase(titolo);
        }

        return giochi;
    }

    public Gioco addGioco(Gioco giocoForm, List<Integer> piattaformaSelezionataId){
        try{
            //Salvo la/e piattaforma selezionata e l'utente a cui appartiene il gioco
            List<Piattaforma> piattaformaSelezionata = piattaformaRepository.findAllById(piattaformaSelezionataId);
            giocoForm.setPiattaforma(piattaformaSelezionata);
            if(GetUtente() != null){
                giocoForm.setUtente(GetUtente());
            }else {
                throw new Exception("Utente non presente");
            }
            return giocoRepository.save(giocoForm);
        }catch (Exception ex) {
            throw new ExceptionAddGioco("CG_500_DATI_INSERITI_PRESENTI_NEL_SISTEMA");
        }
    }

    public void cancellaGioco(Integer id, Principal principal){
        Optional<Gioco> gioco = giocoRepository.findById(id);
        if (!gioco.isPresent()){
            throw new NessunGiocoTrovato("CG400_ID_PASSATO_NON_VALIDO");
        }
        //Cerchiamo se il gioco è in qualche carrello
        Optional<CarrelloGioco> carrelloGioco = carrelloGiocoRepository.findByIdGiocoCarrello(id);
        if (carrelloGioco.isPresent()){
            carrelloService.cancellaGiocoCarrello(carrelloGioco.get().getId_carrelloGioco(),principal);
        }
        giocoRepository.deleteById(id);
    }

    //Modifica Gioco
    public Gioco editGioco(Gioco editFormGioco, List<Integer> selezionePiattaformaID) {
        //Salvo la/e piattaforma selezionata
        List<Piattaforma> piattaformaSelezionata = piattaformaRepository.findAllById(selezionePiattaformaID);
        editFormGioco.setPiattaforma(piattaformaSelezionata);
        editFormGioco.setTitolo(editFormGioco.getTitolo());
        editFormGioco.setDescrizione(editFormGioco.getDescrizione());
        editFormGioco.setPrezzo(editFormGioco.getPrezzo());
        editFormGioco.setCodiceProdotto(editFormGioco.getCodiceProdotto());
        editFormGioco.setQuantita(editFormGioco.getQuantita());
        editFormGioco.setSoftwareHouse(editFormGioco.getSoftwareHouse());
        return giocoRepository.save(editFormGioco);
    }

    //Aggiungo giochi tramite file
    public List<Gioco> parseFile(MultipartFile file) throws IOException {
        List<Gioco> giocoList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        //salta la prima riga
        reader.readLine();

        //legge ogni riga fermandosi quando sarà null
        while ((line = reader.readLine()) != null){
            String[] parts = line.split(",");
            //Almeno 5 elementi nel file
            if (parts.length >= 5){
                Gioco gioco = new Gioco();
                gioco.setCodiceProdotto(Long.valueOf(parts[0].trim()));
                gioco.setDescrizione(parts[1].trim());
                gioco.setKeyAttivazione(parts[2].trim());
                gioco.setPrezzo(Double.valueOf(parts[3]));
                gioco.setSoftwareHouse(parts[4].trim());
                gioco.setTitolo(parts[5].trim());
                //Verifico la quantità inserita
                if (Double.parseDouble(parts[6]) < 0){
                    return null;
                }
                gioco.setQuantita(Integer.valueOf(parts[6]));

                //Verifico il caso in cui il formato e la data non sia ok
                try {
                    gioco.setDataUscitaGioco(LocalDate.parse(parts[7]));
                }catch (DateTimeParseException e){
                    return null;
                }
                giocoList.add(gioco);
            }
        }

        return giocoList;
    }

    public List<Gioco> RicercaGiochi(String rangePrezzo, String dataUscita, List<String> piattaforma)throws Exception{
        //controllo i parametri da passare alla query
        LocalDate inizioParse =  dataUscita.isEmpty() ? null : LocalDate.parse(dataUscita + "-01");
        LocalDate fineParse = inizioParse == null ? null : inizioParse.withDayOfMonth(inizioParse.lengthOfMonth());
        Double prezzo = rangePrezzo.isEmpty() ? null : Double.valueOf(rangePrezzo);
        List<Gioco> query = giocoRepository.searchGiochi(piattaforma, prezzo, inizioParse,fineParse);
        if (query.isEmpty()){
            throw new Exception();
        }
        return query;
    }
}
