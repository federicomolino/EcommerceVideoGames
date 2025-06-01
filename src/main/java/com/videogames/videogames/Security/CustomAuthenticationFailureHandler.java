package com.videogames.videogames.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException
                                        exception) throws IOException, ServletException{
        if (exception instanceof DisabledException){
            System.out.println("Authentication failed: utente disabilitato!");
            getRedirectStrategy().sendRedirect(request,response,"/login?error=disabled");
        }else{
            System.out.println("Authentication failed: errore generico");
            getRedirectStrategy().sendRedirect(request,response,"/login?error=true");
        }
    }
}
