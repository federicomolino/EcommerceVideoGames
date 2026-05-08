package com.videogames.videogames.Security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;

    // Inietti JwtFilter via costruttore
    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 1) Security per le API (JWT)
    @Bean
    @Order(1) // va valutata prima
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http .securityMatcher(request ->
                request.getRequestURI().startsWith("/api/v1/")
                        && !request.getRequestURI().equals("/api/v1/token")
        )// questa è fondamentale
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .requestCache(cache -> cache.disable())
                .authorizeHttpRequests(auth -> auth .requestMatchers("/api/v1/token").permitAll() .anyRequest().authenticated() )
                .exceptionHandling(exceptions -> exceptions .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN)) )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/api/v1/token").permitAll()
                .requestMatchers("/guest-login").permitAll()

                .requestMatchers("/gioco/newGioco","/gioco/editGioco/**","/gioco/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/piattaforma").hasAuthority("ADMIN")

                .requestMatchers("/","/**").authenticated()
                // 👤 USER e GUEST
                .requestMatchers("/", "/**").hasAnyAuthority("USER", "GUEST")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler()) // usa handler personalizzato
                //.failureUrl("/login?error=true") // Usa il parametro `error=true` per mostrare l'errore
                .permitAll()
                .and()
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService(){
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
;        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
