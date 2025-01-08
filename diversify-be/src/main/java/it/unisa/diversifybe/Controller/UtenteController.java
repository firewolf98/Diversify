package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.ChangePasswordRequest;
import it.unisa.diversifybe.DTO.RecuperaPasswordRequest;
import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Service.UtenteService;
import it.unisa.diversifybe.Utilities.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/utenti")
@CrossOrigin(origins = "http://localhost:4200")  // Permette richieste solo da localhost:4200
public class UtenteController {


    private final UtenteService utenteService;
    private final JwtUtils jwtUtils;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio UtenteService.
     *
     * @param utenteService il servizio che gestisce la logica di autenticazione e registrazione.
     */

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
                return ResponseEntity.ok(Map.of("message", result));
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

            return ResponseEntity.ok(Map.of("message", "Password aggiornata con successo"));

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
     *                                <ul>
     *                                    <li>email: l'indirizzo email dell'utente.</li>
     *                                    <li>codiceFiscale: il codice fiscale dell'utente.</li>
     *                                    <li>personalAnswer: la risposta alla domanda personale dell'utente.</li>
     *                                    <li>newPassword: la nuova password da impostare.</li>
     *                                </ul>
     * @return una {@link ResponseEntity} che rappresenta il risultato dell'operazione:
     * <ul>
     *     <li>HTTP 200 OK: se la password è stata aggiornata con successo.</li>
     *     <li>HTTP 404 NOT FOUND: se non esiste un utente con i dati forniti (email o codice fiscale errati).</li>
     *     <li>HTTP 401 UNAUTHORIZED: se la risposta alla domanda personale è errata.</li>
     *     <li>HTTP 500 INTERNAL SERVER ERROR: se si verifica un errore durante l'hashing della password.</li>
     * </ul>
     */

    @PostMapping("/recupera_password")
    public ResponseEntity<?> recuperaPassword(@RequestBody RecuperaPasswordRequest recuperaPasswordRequest) {
        try {
            // Verifica che i campi obbligatori siano presenti
            if (recuperaPasswordRequest.getEmail() == null || recuperaPasswordRequest.getEmail().isBlank()) {
                throw new IllegalArgumentException("L'email non può essere nulla o vuota.");
            }
            if (recuperaPasswordRequest.getCodiceFiscale() == null || recuperaPasswordRequest.getCodiceFiscale().isBlank()) {
                throw new IllegalArgumentException("Il codice fiscale non può essere nullo o vuoto.");
            }
            if (recuperaPasswordRequest.getNewPassword() == null || recuperaPasswordRequest.getNewPassword().isBlank()) {
                throw new IllegalArgumentException("La nuova password non può essere nulla o vuota.");
            }

            // Passo 1: Verifica email e codice fiscale
            Optional<Utente> utenteOptional = utenteService.findByEmailAndCodiceFiscale(
                    recuperaPasswordRequest.getEmail(),
                    recuperaPasswordRequest.getCodiceFiscale()
            );

            if (utenteOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato con i dati forniti");
            }

            Utente utente = utenteOptional.get();

            // Passo 2: Verifica risposta personale
            if (!recuperaPasswordRequest.getPersonalAnswer().equals(utente.getRispostaHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Risposta alla domanda personale errata");
            }

            // Passo 3: Criptare la nuova password e aggiornarla nel database
            String hashedNewPassword;
            try {
                hashedNewPassword = utenteService.hashPassword(recuperaPasswordRequest.getNewPassword());
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la crittografia dei dati");
            }

            utente.setPasswordHash(hashedNewPassword);
            utenteService.save(utente);

            return ResponseEntity.ok("Password aggiornata con successo");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * Endpoint per recuperare un utente a partire dal token JWT.
     *
     * @param token il token JWT passato come parametro di query o nell'header della richiesta.
     * @return l'utente corrispondente se il token è valido, altrimenti un messaggio di errore.
     */
    @GetMapping("/recupera_utente")
    public ResponseEntity<?> getUserFromToken(@RequestHeader(value = "Authorization", required = false) String token) {
        // Controlla se il token è nullo o vuoto
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o utente non trovato.");
        }

        // Rimuove il prefisso "Bearer " dal token, se presente
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o utente non trovato.");
        }

        // Recupera l'utente dal servizio
        Optional<Utente> utente = utenteService.getUserFromToken(token);

        if (utente.isPresent()) {
            return ResponseEntity.ok(utente.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o utente non trovato.");
        }
    }

    /**
     * Endpoint per eliminare l'account di un utente.
     * <p>
     * Questo metodo consente di eliminare l'account di un utente autenticato.
     * Utilizza un token JWT presente nell'intestazione della richiesta per identificare e autorizzare l'operazione.
     * Se il token è valido e corrisponde a un utente, l'account viene eliminato con successo.
     * </p>
     *
     * @param authorizationHeader l'intestazione HTTP "Authorization" contenente il token JWT.
     *                            Il formato atteso è "Bearer {token}".
     * @return una {@link ResponseEntity} che rappresenta il risultato dell'operazione:
     * <ul>
     *     <li>HTTP 200 OK: se l'account è stato eliminato con successo.</li>
     *     <li>HTTP 401 UNAUTHORIZED: se il token è mancante, malformato o non valido.</li>
     *     <li>HTTP 404 NOT FOUND: se l'utente associato al token non è stato trovato.</li>
     * </ul>
     */
    @PostMapping("/elimina_account")
    public ResponseEntity<?> deleteAccount(
            @RequestBody String password,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        // Controllo sulla presenza dell'header Authorization
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token non valido"); // Restituisce "Token non valido" se l'header è assente o vuoto
        }

        // Verifica che l'header inizi con "Bearer "
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token malformato"); // Restituisce "Token malformato" se non c'è il prefisso "Bearer"
        }

        // Estrae il token dal prefisso "Bearer "
        String token = authorizationHeader.substring(7).trim();

        // Se il token è vuoto dopo il prefisso "Bearer "
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token non valido"); // Restituisce "Token non valido" per token vuoto
        }

        // Chiamata al servizio per eliminare l'utente
        String response = utenteService.deleteUser(token, password);

        // Mappa le risposte del servizio ai rispettivi stati HTTP
        switch (response) {
            case "Utente non trovato":
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utente non trovato"); // Restituisce solo il messaggio
            case "Token non valido":
            case "Password errata":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response); // Restituisce direttamente il messaggio
            case "Account eliminato con successo":
                return ResponseEntity.ok(response); // Restituisce direttamente il messaggio
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Errore durante l'eliminazione dell'account."); // Restituisce solo il messaggio
        }
    }

    /**
     * Recupera un utente in base all'indirizzo email fornito.
     *
     * Questo metodo gestisce una richiesta POST al percorso "/recupera_domanda".
     * Esso accetta una stringa contenente l'email di un utente come corpo della richiesta
     * e restituisce una risposta contenente i dettagli dell'utente se trovato,
     * oppure un messaggio di errore se l'utente non esiste.
     *
     * @param email La stringa contenente l'email dell'utente da cercare.
     * @return Una risposta HTTP con il codice di stato 200 (OK) e i dettagli dell'utente
     *         se l'utente esiste, altrimenti restituisce una risposta con il codice di stato 404
     *         (NOT_FOUND) e un messaggio di errore.
     */
    @PostMapping("/recupera_domanda")
    public ResponseEntity<?> getDomandaUser(@RequestBody String email) {
        Utente utente = utenteService.getUserByEmail(email);
        if (utente != null) {
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
    }
}