package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * Classe di trasferimento dati (DTO) per la richiesta di login.
 *
 * Questa classe viene utilizzata per ricevere i dati di login da un client, inclusi
 * il nome utente e la password hashata, durante il processo di autenticazione.
 */

@Data
public class LoginRequest {

    /**
     * Il nome utente dell'utente che sta cercando di effettuare il login.
     * Questo campo viene utilizzato per identificare univocamente l'utente nel sistema.
     */

    private String username;

    /**
     * La password hashata dell'utente che sta cercando di effettuare il login.
     * Questo campo contiene la password criptata (tramite SHA-256) per garantire
     * la sicurezza durante l'autenticazione.
     */

    private String passwordHash;
}