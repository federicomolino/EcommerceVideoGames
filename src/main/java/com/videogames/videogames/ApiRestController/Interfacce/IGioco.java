package com.videogames.videogames.ApiRestController.Interfacce;

import com.videogames.videogames.ApiRestController.Gioco.CreaGioco;
import com.videogames.videogames.ApiRestController.Gioco.EditGioco;
import com.videogames.videogames.ApiRestController.Gioco.RecuperaGiochi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IGioco {

    ResponseEntity<CreaGioco.Response> AddGioco(CreaGioco.Command gioco, HttpServletRequest request);
    ResponseEntity<EditGioco.Response> EditGioco(Integer idGioco, EditGioco.Command gioco, HttpServletRequest request);
    ResponseEntity<RecuperaGiochi.Response> GetGiochiPerUtente(String username);
    ResponseEntity<?> DeleteGioco(Integer id, HttpServletRequest request, Principal principal);
}
