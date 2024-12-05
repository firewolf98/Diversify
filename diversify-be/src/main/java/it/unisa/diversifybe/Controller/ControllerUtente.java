package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

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