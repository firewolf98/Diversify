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
        // Trova l'utente in base allo username
        Optional<Utente> utenteOptional = utenteService.findByUsername(changePasswordRequest.getUsername());
        if (!utenteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }

        Utente utente = utenteOptional.get();

        // Verifica che la password attuale sia corretta
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), utente.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password attuale errata");
        }

        // Controlla che la nuova password soddisfi i criteri di validazione
        String newPassword = changePasswordRequest.getNewPassword();
        if (!isPasswordValid(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La nuova password non soddisfa i criteri di sicurezza");
        }

        // Aggiorna la password con la nuova codificata
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        utente.setPassword(encodedNewPassword);
        utenteService.updateUtente(utente); // Metodo per aggiornare l'utente nel database

        return ResponseEntity.ok("Password cambiata con successo");
    }

    private boolean isPasswordValid(String password) {
        // Regex per validare la password: almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$";
        return password != null && password.matches(passwordRegex);
    }
}