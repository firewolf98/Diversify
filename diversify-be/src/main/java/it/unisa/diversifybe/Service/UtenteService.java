package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Repository.UtenteRepository;
import it.unisa.diversifybe.Utilities.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * La classe {@code UtenteService} gestisce la logica di business relativa agli utenti, inclusi la registrazione,
 * l'autenticazione e l'hashing delle password.
 * Utilizza {@link UtenteRepository} per l'accesso ai dati degli utenti e {@link JwtUtils} per la generazione dei token JWT.
 */

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JwtUtils jwtUtils; // Utils per la generazione del JWT

    /**
     * Registra un nuovo utente nel sistema.
     * <p>
     * Verifica che lo username e l'email non siano già in uso e, in caso contrario, registra l'utente con la password
     * hashed tramite SHA-256.
     * </p>
     *
     * @param registerRequest l'oggetto che contiene i dati per la registrazione dell'utente.
     * @return un messaggio di successo o errore.
     * @throws NoSuchAlgorithmException se si verifica un errore nell'hashing della password.
     */

    public String registerUser(RegisterRequest registerRequest) throws NoSuchAlgorithmException {
        // Controllo che i campi obbligatori non siano nulli o vuoti

        if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
            return "Il campo nome è obbligatorio";
        }

        if (registerRequest.getLastName() == null || registerRequest.getLastName().trim().isEmpty()) {
            return "Il campo cognome è obbligatorio";
        }

        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            return "Il campo username è obbligatorio.";
        }

        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            return "Il campo email è obbligatorio.";
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return "Il campo password è obbligatorio.";
        }

        if (registerRequest.getCodiceFiscale() == null || !isValidCodiceFiscale(registerRequest.getCodiceFiscale())) {
            return "Codice fiscale non valido.";
        }

        if (registerRequest.getDomanda() == null || registerRequest.getDomanda().trim().isEmpty()) {
            return "Domanda non valida.";
        }

        if (registerRequest.getRisposta() == null || registerRequest.getRisposta().trim().isEmpty()) {
            return "Risposta non valida.";
        }

        // Validazione email
        if (!isValidEmail(registerRequest.getEmail())) {
            return "Email non valida.";
        }

        // Validazione password
        if (!isValidPassword(registerRequest.getPassword())) {
            return "Password non valida. Deve contenere almeno 8 caratteri, una lettera maiuscola, una lettera minuscola e un numero.";
        }

        // Verifica che l'username non sia già in uso
        Optional<Utente> existingUserByUsername = utenteRepository.findByUsername(registerRequest.getUsername());
        if (existingUserByUsername.isPresent()) {
            return "Username già in uso!";
        }

        // Verifica che l'email non sia già in uso
        Optional<Utente> existingUserByEmail = utenteRepository.findByEmail(registerRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            return "Email già in uso!";
        }

        Optional<Utente> existingUserByCodiceFiscale = utenteRepository.findByCodiceFiscale(registerRequest.getCodiceFiscale());
        if (existingUserByCodiceFiscale.isPresent()) {
            return "Codice fiscale già in uso!";
        }

        // Hash della password utilizzando il metodo esistente
        String hashedPassword = hashPassword(registerRequest.getPassword());

        // Creazione del nuovo utente
        Utente utente = new Utente();
        utente.setNome(registerRequest.getName().trim());
        utente.setCognome(registerRequest.getLastName().trim());
        utente.setUsername(registerRequest.getUsername().trim());
        utente.setEmail(registerRequest.getEmail().trim());
        utente.setCodiceFiscale(registerRequest.getCodiceFiscale().trim());
        utente.setPasswordHash(hashedPassword);
        utente.setTipoDomanda(registerRequest.getDomanda());
        utente.setRispostaHash(registerRequest.getRisposta());

        // Salvataggio dell'utente nel database
        utenteRepository.save(utente);

        return "Utente registrato con successo!";
    }

    // Metodo per validare il codice fiscale
    private boolean isValidCodiceFiscale(String codiceFiscale) {
        String codiceFiscaleRegex = "^[A-Z0-9]{16}$";
        return codiceFiscale != null && codiceFiscale.matches(codiceFiscaleRegex);
    }

    // Metodo per validare l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    // Metodo per validare la password
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    /**
     * Autentica un utente nel sistema.
     * <p>
     * Verifica che l'utente esista e che la password inserita corrisponda a quella memorizzata nel database. Se la
     * verifica ha successo, viene generato e restituito un token JWT.
     * </p>
     *
     * @param loginRequest l'oggetto che contiene i dati di login dell'utente.
     * @return un oggetto {@link JwtResponse} contenente il token JWT, o {@code null} se le credenziali sono errate.
     * @throws NoSuchAlgorithmException se si verifica un errore nell'hashing della password.
     */

    public JwtResponse authenticateUser(LoginRequest loginRequest) throws NoSuchAlgorithmException {
        // Controlla che username e password non siano nulli o vuoti
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return null; // Manca username o password
        }

        Optional<Utente> utenteOptional = utenteRepository.findByEmail(loginRequest.getEmail());

        if (utenteOptional.isEmpty()) {
            return null; // L'utente non esiste
        }

        Utente utente = utenteOptional.get();

        // Controlla se la password hashata corrisponde
        if (!utente.getPasswordHash().equals(hashPassword(loginRequest.getPassword()))) {
            return null; // Credenziali non valide
        }

        String jwt = jwtUtils.generateToken(utente.getUsername());
        return new JwtResponse(jwt);
    }

    /**
     * Esegue l'hashing di una password utilizzando l'algoritmo SHA-256.
     * <p>
     * La password viene convertita in una rappresentazione di hash sicura per essere memorizzata nel database.
     * </p>
     *
     * @param password la password da hashare.
     * @return la password hashata in formato esadecimale.
     * @throws NoSuchAlgorithmException se l'algoritmo SHA-256 non è disponibile.
     */

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * Trova un utente nel database in base al suo username.
     *
     * @param username il nome utente dell'utente da cercare
     * @return un {@link Optional} contenente l'utente se trovato, altrimenti un {@link Optional#empty()}
     */
    public Optional<Utente> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return utenteRepository.findByUsername(username);
    }


    /**
     * Recupera un utente dal database utilizzando l'email e il codice fiscale forniti.
     * <p>
     * Questo metodo utilizza il repository per effettuare una query al database e
     * verificare l'esistenza di un utente che corrisponda ai criteri specificati.
     * Restituisce un oggetto {@link Optional} che può contenere l'utente trovato
     * o essere vuoto se non esiste alcun utente con i dati forniti.
     *
     * @param email         l'indirizzo email dell'utente da cercare.
     * @param codiceFiscale il codice fiscale dell'utente da cercare.
     * @return un oggetto {@link Optional} contenente l'utente corrispondente,
     * oppure {@code Optional.empty()} se nessun utente è stato trovato.
     */

    public Optional<Utente> findByEmailAndCodiceFiscale(String email, String codiceFiscale) {
        if (email == null || email.trim().isEmpty() || codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new IllegalArgumentException("Email and codice fiscale cannot be null or empty");
        }
        return utenteRepository.findByEmailAndCodiceFiscale(email, codiceFiscale);
    }


    /**
     * Salva un utente nel database.
     *
     * @param utente l'entità utente da salvare.
     */
    public void save(Utente utente) {
        utenteRepository.save(utente);
    }

    /**
     * Recupera un utente dal database utilizzando un token JWT.
     *
     * @param token il token JWT da cui estrarre il nome utente.
     * @return un oggetto {@link Optional} contenente l'utente corrispondente,
     * oppure {@code Optional.empty()} se il token non è valido o l'utente non è trovato.
     */
    public Optional<Utente> getUserFromToken(String token) {
        // Decodifica e valida il token per ottenere il nome utente
        String username = jwtUtils.validateToken(token);

        if (username == null) {
            // Il token non è valido
            return Optional.empty();
        }

        // Cerca l'utente nel database usando il nome utente estratto
        return utenteRepository.findByUsername(username);
    }

    /**
     * Elimina un utente dal sistema utilizzando un token JWT per identificarlo.
     * <p>
     * Questo metodo verifica la validità del token JWT fornito, estrae il nome utente dal token,
     * cerca l'utente corrispondente nel database e lo elimina. Restituisce un messaggio
     * che indica l'esito dell'operazione.
     * </p>
     *
     * @param token il token JWT utilizzato per autenticare l'utente e ottenere il suo nome utente.
     * @return una stringa che rappresenta l'esito dell'operazione:
     * <ul>
     *     <li>"Account eliminato con successo." se l'utente è stato eliminato correttamente.</li>
     *     <li>"Token non valido." se il token fornito è null, vuoto, malformato o non valido.</li>
     *     <li>"Utente non trovato." se non esiste alcun utente corrispondente al nome utente estratto dal token.</li>
     *     <li>"Errore durante l'eliminazione dell'account." in caso di errore generico durante l'operazione.</li>
     * </ul>
     */
    public String deleteUser(String token, String password) {
        try {
            String username = jwtUtils.validateToken(token); // Ottieni il nome utente dal token
            if (username == null) {
                return "Token non valido.";
            }

            Optional<Utente> utenteOptional = utenteRepository.findByUsername(username);
            if (utenteOptional.isEmpty()) {
                return "Utente non trovato.";
            }

            Utente utente = utenteOptional.get();

            String hashedPassword = hashPassword(password);

            if (hashedPassword.equals(utente.getPasswordHash())) {
                utenteRepository.delete(utente);
                return "Account eliminato con successo.";
            }
            return "Password errata";

        } catch (Exception e) {
            return "Errore durante l'eliminazione dell'account.";
        }
    }

    /**
     * Recupera l'ID di un utente dato il suo username.
     *
     * @param username L'username dell'utente.
     * @return L'ID dell'utente.
     */
    public String getIdByUsername(String username) {
        Optional<Utente> utente = utenteRepository.findByUsername(username);
        if (utente.isEmpty()) {
            return "Utente non trovato.";
        }
        return utente.get().getIdUtente();
    }

    /**
     * Recupera un utente in base all'indirizzo email fornito.
     *
     * Questo metodo cerca un utente nel repository utilizzando l'indirizzo email come criterio.
     * Se un utente corrispondente viene trovato, esso viene restituito.
     * Se l'utente non esiste, viene generata un'eccezione {@link java.util.NoSuchElementException}.
     *
     * @param email L'indirizzo email dell'utente da cercare.
     * @return L'oggetto {@link Utente} associato all'indirizzo email fornito.
     */
    public Utente getUserByEmail(String email) {
        Optional<Utente> utenteOptional = utenteRepository.findByEmail(email);
        return utenteOptional.get();
    }

    /**
     * Trova un utente in base al suo ID.
     *
     * @param id l'ID dell'utente da cercare.
     * @return un {@link Optional} contenente l'utente se trovato, altrimenti vuoto.
     */
    public Optional<Utente> findById(String id) {
        try {
            return utenteRepository.findById(id);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}