package com.videogames.videogames.Security;

import com.videogames.videogames.Entity.Role;
import com.videogames.videogames.Entity.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataBaseUserDetails implements UserDetails {

    private final Integer id_utente;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public DataBaseUserDetails(Utente utente) {
        this.id_utente = utente.getId_utente();
        this.username = utente.getUsername();
        this.password = utente.getPassword();

        authorities = new ArrayList<>();
        for(Role role : utente.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
