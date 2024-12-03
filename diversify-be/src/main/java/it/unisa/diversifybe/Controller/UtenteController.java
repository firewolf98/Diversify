package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/utenti")

/**
 * Controller che gestisce le operazioni di autenticazione e registrazione degli utenti.
 *
 * Questo controller espone i seguenti endpoint:
 * - /login: per l'autenticazione dell'utente tramite username e password.
 * - /registrazione: per la registrazione di un nuovo utente.
 */

public class UtenteController {

    private final UtenteService utenteService;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio UtenteService.
     *
     * @param utenteService il servizio che gestisce la logica di autenticazione e registrazione.
     */

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    /**
     * Endpoint per l'autenticazione dell'utente.
     * Questo metodo verifica le credenziali di login dell'utente e genera un token JWT se la autenticazione ha successo.
     *
     * @param loginRequest i dati di login inviati dal client, contenenti username e password.
     * @return una risposta HTTP con il token JWT se l'autenticazione è corretta, altrimenti un messaggio di errore.
     */

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Prova a autenticare l'utente utilizzando il servizio
            JwtResponse jwtResponse = utenteService.authenticateUser(loginRequest);
            if (jwtResponse != null) {
                // Se l'autenticazione è riuscita, restituisci il JWT token
                return ResponseEntity.ok(jwtResponse);
            }
            // Se username o password sono errati, restituisci un errore
            return ResponseEntity.status(401).body("Username o password errati");
        } catch (NoSuchAlgorithmException e) {
            // Se c'è un errore nell'algoritmo di hashing della password, restituisci un errore generico
            return ResponseEntity.status(500).body("Errore interno durante la verifica della password");
        }
    }

    /**
     * Endpoint per la registrazione di un nuovo utente.
     * Questo metodo crea un nuovo utente, validando i dati di registrazione e inserendoli nel sistema.
     *
     * @param registerRequest i dati di registrazione inviati dal client, contenenti username, password ed email.
     * @return una risposta HTTP che indica se la registrazione è riuscita o se ci sono errori nei dati inviati.
     */

    @PostMapping("/registrazione")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Prova a registrare il nuovo utente
            String result = utenteService.registerUser(registerRequest);
            if (result.equals("Utente registrato con successo!")) {
                // Se la registrazione è riuscita, restituisci un messaggio di successo
                return ResponseEntity.ok(result);
            } else {
                // Se ci sono errori (ad esempio, l'username o l'email sono già in uso), restituisci un errore
                return ResponseEntity.status(400).body(result);
            }
        } catch (NoSuchAlgorithmException e) {
            // Se c'è un errore nell'algoritmo di hashing della password, restituisci un errore
            return ResponseEntity.status(500).body("Errore durante la registrazione");
        }
    }
}