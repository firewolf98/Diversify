package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * Classe di trasferimento dati (DTO) per la richiesta di registrazione.
 * Questa classe viene utilizzata per ricevere i dati necessari alla registrazione
 * di un nuovo utente, inclusi il nome utente, la password hashata e l'email.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * Il nome utente scelto dall'utente durante la registrazione.
     * Questo campo viene utilizzato per identificare univocamente l'utente nel sistema.
     */

    private String username;


    /**
     * La password hashata scelta dall'utente durante la registrazione.
     * Questo campo contiene la password criptata (tramite SHA-256) per garantire
     * la sicurezza durante la registrazione.
     */

    private String passwordHash;

    /**
     * L'indirizzo email fornito dall'utente durante la registrazione.
     * Questo campo viene utilizzato per comunicazioni future e per verificare l'unicità
     * dell'account.
     */

    private String email;


    private String codiceFiscale;

}
