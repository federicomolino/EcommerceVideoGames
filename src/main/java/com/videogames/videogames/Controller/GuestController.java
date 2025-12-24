package com.videogames.videogames.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GuestController {
    @GetMapping("guest-login")
    public String guestLogin(HttpServletRequest request){

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("GUEST"));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                "guest", // username fittizio
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return "redirect:/";
    }
}
