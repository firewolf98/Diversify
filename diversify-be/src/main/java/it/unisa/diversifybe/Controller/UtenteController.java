package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.ChangePasswordRequest;
import it.unisa.diversifybe.DTO.RecuperaPasswordRequest;
import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.unisa.diversifybe.Utilities.JwtUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/utenti")

public class UtenteController {

    private final UtenteService utenteService;
    private final JwtUtils jwtUtils;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio UtenteService.
     *
     * @param utenteService il servizio che gestisce la logica di autenticazione e registrazione.
     */
@Autowired
    public UtenteController(UtenteService utenteService, JwtUtils jwtUtils) {
        this.utenteService = utenteService;
        this.jwtUtils = jwtUtils;
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
            // Prova ad autenticare l'utente utilizzando il servizio
            JwtResponse jwtResponse = utenteService.authenticateUser(loginRequest);
            if (jwtResponse != null) {
                // Verifica se l'utente è bannato
                String username = jwtUtils.validateToken(jwtResponse.getToken()); // Usa validateToken per estrarre l'username

                Optional<Utente> utenteOptional = utenteService.findByUsername(username);
                if (utenteOptional.isPresent() && utenteOptional.get().isBanned()) {
                    return ResponseEntity.status(403).body("Accesso vietato. Utente bannato.");
                }

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

    /**
     * Modifica la password di un utente esistente nel sistema.
     * <p>
     * Il metodo verifica se l'utente esiste, controlla che la password corrente fornita
     * sia corretta e, se tutte le verifiche passano, aggiorna la password con la nuova
     * versione criptata. La crittografia della password utilizza SHA-256.
     * </p>
     *
     * @param changePasswordRequest l'oggetto che contiene i dati necessari per modificare la password:
     *                              <ul>
     *                                  <li>username: il nome utente dell'utente.</li>
     *                                  <li>currentPassword: la password corrente dell'utente.</li>
     *                                  <li>newPassword: la nuova password da impostare.</li>
     *                              </ul>
     * @return una {@link ResponseEntity} che rappresenta il risultato dell'operazione:
     * <ul>
     *     <li>HTTP 200 OK: se la password è stata aggiornata con successo.</li>
     *     <li>HTTP 404 NOT FOUND: se l'utente con il nome utente fornito non esiste.</li>
     *     <li>HTTP 401 UNAUTHORIZED: se la password corrente fornita è errata.</li>
     *     <li>HTTP 500 INTERNAL SERVER ERROR: se si verifica un errore nell'hashing della password.</li>
     * </ul>
     */

    @PostMapping("/cambia_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Rimuovi "Bearer " dall'intestazione del token
            String username = jwtUtils.validateToken(token); // Utilizza l'oggetto jwtUtils per validare il token

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido");
            }

            Optional<Utente> utenteOptional = utenteService.findByUsername(username);
            if (utenteOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
            }

            Utente utente = utenteOptional.get();

            // Verifica se la password corrente è corretta
            String hashedCurrentPassword = utenteService.hashPassword(changePasswordRequest.getCurrentPassword());
            if (!hashedCurrentPassword.equals(utente.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password corrente errata");
            }

            // Aggiorna la password con quella nuova criptata
            String hashedNewPassword = utenteService.hashPassword(changePasswordRequest.getNewPassword());
            utente.setPasswordHash(hashedNewPassword);

            // Salva l'utente con la nuova password tramite il repository del service
            utenteService.save(utente);

            return ResponseEntity.ok("Password aggiornata con successo");

        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'hashing della password");
        }
    }

    /**
     * Recupera la password di un utente esistente nel sistema.
     * <p>
     * Il metodo verifica i dati forniti dall'utente, tra cui email, codice fiscale e risposta alla domanda personale.
     * Se tutte le verifiche passano, la password viene aggiornata con una nuova versione criptata utilizzando SHA-256.
     * </p>
     *
     * @param recuperaPasswordRequest l'oggetto che contiene i dati necessari per il recupero della password:
     *                                 <ul>
     *                                     <li>email: l'indirizzo email dell'utente.</li>
     *                                     <li>codiceFiscale: il codice fiscale dell'utente.</li>
     *                                     <li>personalAnswer: la risposta alla domanda personale dell'utente.</li>
     *                                     <li>newPassword: la nuova password da impostare.</li>
     *                                 </ul>
     * @return una {@link ResponseEntity} che rappresenta il risultato dell'operazione:
     *         <ul>
     *             <li>HTTP 200 OK: se la password è stata aggiornata con successo.</li>
     *             <li>HTTP 404 NOT FOUND: se non esiste un utente con i dati forniti (email o codice fiscale errati).</li>
     *             <li>HTTP 401 UNAUTHORIZED: se la risposta alla domanda personale è errata.</li>
     *             <li>HTTP 500 INTERNAL SERVER ERROR: se si verifica un errore durante l'hashing della password.</li>
     *         </ul>
     */

    @PostMapping("/recupera_password")
    public ResponseEntity<?> recuperaPassword(@RequestBody RecuperaPasswordRequest recuperaPasswordRequest) {
        try {
            // Passo 1: Verifica email e codice fiscale
            Optional<Utente> utenteOptional = utenteService.findByEmailAndCodiceFiscale(
                    recuperaPasswordRequest.getEmail(),
                    recuperaPasswordRequest.getCodiceFiscale()
            );

            if (utenteOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato con i dati forniti");
            }

            Utente utente = utenteOptional.get();

            // Passo 2: Criptare la risposta fornita e confrontarla con quella salvata
            String hashedAnswer = utenteService.hashPassword(recuperaPasswordRequest.getPersonalAnswer());
            if (!hashedAnswer.equals(utente.getRispostaHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Risposta alla domanda personale errata");
            }

            // Passo 3: Criptare la nuova password e aggiornarla nel database
            String hashedNewPassword = utenteService.hashPassword(recuperaPasswordRequest.getNewPassword());
            utente.setPasswordHash(hashedNewPassword);
            utenteService.save(utente);

            return ResponseEntity.ok("Password aggiornata con successo");

        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la crittografia dei dati");
        }
    }
}