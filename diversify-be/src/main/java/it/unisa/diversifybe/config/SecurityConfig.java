package it.unisa.diversifybe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabilita CSRF (solo per sviluppo)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Rotte pubbliche
                        .anyRequest().authenticated() // Tutte le altre rotte richiedono autenticazione
                )
                .formLogin(AbstractHttpConfigurer::disable) // Disabilita il login basato su form
                .httpBasic(AbstractHttpConfigurer::disable); // Disabilita l'autenticazione HTTP Basic

        return http.build();
    }
}
