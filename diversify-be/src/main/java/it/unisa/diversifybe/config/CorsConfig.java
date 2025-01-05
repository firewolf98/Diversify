package it.unisa.diversifybe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applica a tutti gli endpoint
                .allowedOrigins("http://localhost:4200") // Consenti richieste dal frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Metodi consentiti
                .allowedHeaders("*") // Consenti tutti gli header
                .exposedHeaders("Authorization") // Esponi header specifici (opzionale)
                .allowCredentials(true); // Consenti l'invio di cookie/credenziali
    }
}
