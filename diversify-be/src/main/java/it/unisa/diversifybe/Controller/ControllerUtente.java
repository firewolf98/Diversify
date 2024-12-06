package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Controller REST per la gestione degli utenti.
 * Fornisce endpoint per autenticazione, registrazione e aggiornamento della password.
 */

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    /**
     * Autentica un utente esistente basandosi sulle credenziali fornite.
     * Genera un token JWT se le credenziali sono corrette.
     *
     * @param loginRequest l'oggetto {@link LoginRequest} contenente username e password.
     * @return {@link ResponseEntity} contenente il token JWT in caso di successo, oppure un errore HTTP in caso contrario.
     */

    // Endpoint per ottenere un utente
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Verifica se l'utente esiste e se la password è corretta
        Optional<Utente> utente = utenteService.findByUsername(loginRequest.getUsername());
        if (!utente.isPresent() || !passwordEncoder.matches(loginRequest.getPassword(), utente.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username o password errati");
        }

        // Autenticazione riuscita, genera il JWT
        String jwt = authService.generateJwtToken(utente.get());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * Registra un nuovo utente se l username e l'email non sono già in uso.
     *
     * @param registerRequest l'oggetto {@link RegisterRequest} contenente i dati del nuovo utente.
     * @return {@link ResponseEntity} contenente un messaggio di successo in caso di registrazione completata,
     *         oppure un errore HTTP se lo username o l'email sono già in uso.
     */

    //Logica registrazione utente
    @PostMapping("/registrazione")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (utenteService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username già in uso!");
        }

        if (utenteService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email già in uso!");
        }

        // Logica di registrazione
        utenteService.registraUtente(registerRequest);
        return ResponseEntity.ok("Utente registrato con successo!");
    }

    /**
     * Cambia la password di un utente esistente dopo aver verificato la password corrente.
     *
     * @param changePasswordRequest l'oggetto {@link ChangePasswordRequest} contenente username, password corrente
     *                              e nuova password.
     * @return {@link ResponseEntity} con un messaggio di successo in caso di aggiornamento completato,
     *         oppure un errore HTTP se l'utente non esiste o la password corrente è errata.
     */

    @PostMapping("/cambia_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        // Verifica se l'utente esiste
        Optional<Utente> utente = utenteService.findByUsername(changePasswordRequest.getUsername());
        if (!utente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }

        // Verifica se la password corrente è corretta
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), utente.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password corrente errata");
        }

        // Aggiorna la password con quella nuova criptata
        String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        utente.get().setPassword(encodedNewPassword);
        utenteService.save(utente.get());

        return ResponseEntity.ok("Password aggiornata con successo");
    }
}