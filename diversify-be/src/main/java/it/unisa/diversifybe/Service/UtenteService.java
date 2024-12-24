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

        Optional<Utente> existingUserByUsername = utenteRepository.findByUsername(registerRequest.getUsername());
        if (existingUserByUsername.isPresent()) {
            return "Username già in uso!";
        }

        Optional<Utente> existingUserByEmail = utenteRepository.findByEmail(registerRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            return "Email già in uso!";
        }

        String hashedPassword = hashPassword(registerRequest.getPasswordHash());

        Utente utente = new Utente();
        utente.setUsername(registerRequest.getUsername());
        utente.setEmail(registerRequest.getEmail());
        utente.setPasswordHash(hashedPassword);

        utenteRepository.save(utente);

        return "Utente registrato con successo!";
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
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(loginRequest.getUsername());

        if (utenteOptional.isEmpty()) {
            return null;
        }

        Utente utente = utenteOptional.get();

        if (utente.getPasswordHash().equals(hashPassword(loginRequest.getPasswordHash()))) {
            String jwt = jwtUtils.generateToken(utente.getUsername());
            return new JwtResponse(jwt);
        }
        return null;
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

}