package com.videogames.videogames.Service;

import com.videogames.videogames.Entity.Utente;
import com.videogames.videogames.Helpers.HelpUtente;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

@Service
public class TokenService extends HelpUtente {

    //La chiave segreta utilizzando HS256 dovrà essere di 32byte (32 caratteri)
    private final String secretKey="questaLaChiaveSegretaPerRichiestaToken";

    public String generaToken(String username) throws UsernameNotFoundException, IllegalAccessError{

        Utente utente = GetUtenteUsername(username);
        if (utente == null){
            throw new UsernameNotFoundException("Utente non presente a db");
        }

        //Token generato è composto da base64()
        String token = Jwts.builder()
                .setSubject(username) //L'utente che viene codificato nel token
                .setIssuedAt(new Date()) //Data di emissione token
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) //Validità del toje (1h)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // firma token
                .compact(); //Restituiamo come stringa
        return token;
    }

    public String validationToken(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)//Decodifichiamo il token
                    .getBody()
                    .getSubject();//Ritorniamo l'utente
        }catch (JwtException e){
            return null;
        }
    }
}
