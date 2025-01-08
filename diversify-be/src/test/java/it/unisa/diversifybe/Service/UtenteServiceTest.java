package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Repository.UtenteRepository;
import it.unisa.diversifybe.Utilities.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtenteServiceTest {
    /**
     * **Partizioni individuate per i metodi di `UtenteService`**:
     * **Metodo `registerUser`**:
     * - **RegisterRequest**:
     *   - Valido:
     *     - Tutti i campi presenti e corretti.
     *   - Non valido:
     *     - Username mancante o vuoto.
     *     - Email mancante o vuota.
     *     - Password mancante o vuota.
     *     - Codice fiscale mancante o non valido.
     *     - Email non valida.
     *     - Password non valida (e.g., meno di 8 caratteri, mancanza di caratteri richiesti).
     * - **Conflitti nel database**:
     *   - Username già esistente.
     *   - Email già esistente.
     * - **Esito della registrazione**:
     *   - Successo (utente creato correttamente).
     *   - Fallimento:
     *     - Algoritmo di hashing non supportato.
     *     - Campi non validi o duplicati.
     * **Metodo `authenticateUser`**:
     * - **LoginRequest**:
     *   - Valido:
     *     - Username e password corretti.
     *   - Non valido:
     *     - Username mancante o vuoto.
     *     - Password mancante o vuota.
     *     - Credenziali errate.
     * - **Esito dell'autenticazione**:
     *   - Successo (JWT generato correttamente).
     *   - Fallimento:
     *     - Algoritmo di hashing non supportato.
     *     - Credenziali errate o utente inesistente.
     * **Metodo `hashPassword`**:
     * - **Input della password**:
     *   - Valido: Stringa non vuota.
     *   - Non valido:
     *     - Null.
     *     - Stringa vuota.
     * - **Esito dell'hashing**:
     *   - Successo.
     *   - Fallimento:
     *     - Algoritmo di hashing non supportato.
     * **Metodo `findByUsername`**:
     * - **Username**:
     *   - Valido: Stringa esistente nel database.
     *   - Non valido:
     *     - Null.
     *     - Stringa vuota.
     * - **Esito della ricerca**:
     *   - Utente trovato.
     *   - Nessun utente trovato.
     * **Metodo `findByEmailAndCodiceFiscale`**:
     * - **Email e codice fiscale**:
     *   - Valido: Email e codice fiscale corretti.
     *   - Non valido:
     *     - Email mancante, vuota o non valida.
     *     - Codice fiscale mancante, vuoto o non valido.
     * - **Esito della ricerca**:
     *   - Utente trovato.
     *   - Nessun utente trovato.
     * **Metodo `getUserFromToken`**:
     * - **Token JWT**:
     *   - Valido: Token valido e decodificabile.
     *   - Non valido:
     *     - Token null, vuoto, malformato o scaduto.
     * - **Esito del recupero utente**:
     *   - Utente trovato.
     *   - Nessun utente trovato o token non valido.
     * **Metodo `deleteUser`**:
     * - **Token JWT**:
     *   - Valido: Token valido con utente corrispondente.
     *   - Non valido:
     *     - Token mancante, vuoto, malformato o scaduto.
     * - **Esito dell'eliminazione**:
     *   - Successo (utente eliminato).
     *   - Fallimento:
     *     - Token non valido.
     *     - Utente non trovato.
     *     - Errore generico.
     */


    @InjectMocks
    private UtenteService utenteService;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per RegisterRequest con username mancante.
     */
    @Test
    void registerUser_ShouldReturnErrorForMissingUsername() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", null, "ValidPass1", "valid@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Il campo username è obbligatorio.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per RegisterRequest con email mancante.
     */
    @Test
    void registerUser_ShouldReturnErrorForMissingEmail() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "ValidPass1", null, "RSSMRA85M01H501Z", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Il campo email è obbligatorio.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per RegisterRequest con password mancante.
     */
    @Test
    void registerUser_ShouldReturnErrorForMissingPassword() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", null, "valid@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Il campo password è obbligatorio.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per RegisterRequest con codice fiscale non valido.
     */
    @Test
    void registerUser_ShouldReturnErrorForInvalidCodiceFiscale() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "ValidPass1", "valid@example.com", "INVALID", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Codice fiscale non valido.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per RegisterRequest con email non valida.
     */
    @Test
    void registerUser_ShouldReturnErrorForInvalidEmail() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "ValidPass1", "invalid-email", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Email non valida.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per RegisterRequest con password non valida.
     */
    @Test
    void registerUser_ShouldReturnErrorForInvalidPassword() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "short", "valid@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        String response = utenteService.registerUser(request);

        assertEquals("Password non valida. Deve contenere almeno 8 caratteri, una lettera maiuscola, una lettera minuscola e un numero.", response);
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per conflitto su username.
     */
    @Test
    void registerUser_ShouldReturnErrorForExistingUsername() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "existingUser", "ValidPass1", "valid@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        when(utenteRepository.findByUsername("existingUser")).thenReturn(Optional.of(new Utente()));

        String response = utenteService.registerUser(request);

        assertEquals("Username già in uso!", response);
        verify(utenteRepository, times(1)).findByUsername("existingUser");
        verifyNoMoreInteractions(utenteRepository);
    }


    /**
     * Test per conflitto su email.
     */
    @Test
    void registerUser_ShouldReturnErrorForExistingEmail() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "ValidPass1", "existing@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        when(utenteRepository.findByUsername("validUser")).thenReturn(Optional.empty());
        when(utenteRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new Utente()));

        String response = utenteService.registerUser(request);

        assertEquals("Email già in uso!", response);
        verify(utenteRepository, times(1)).findByEmail("existing@example.com");
        verify(utenteRepository, times(1)).findByUsername("validUser");
        verifyNoMoreInteractions(utenteRepository);
    }

    /**
     * Test per errore nell'algoritmo di hashing.
     */
    @Test
    void registerUser_ShouldReturnErrorForHashingError() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "validUser", "ValidPass1", "valid@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        when(utenteRepository.findByUsername("validUser")).thenReturn(Optional.empty());
        when(utenteRepository.findByEmail("valid@example.com")).thenReturn(Optional.empty());

        // Simula l'errore di hashing con uno spy
        UtenteService utenteServiceSpy = spy(utenteService);
        doThrow(new NoSuchAlgorithmException("Hashing error"))
                .when(utenteServiceSpy)
                .hashPassword("ValidPass1");

        // Verifica che l'eccezione venga lanciata
        Exception exception = assertThrows(NoSuchAlgorithmException.class, () -> utenteServiceSpy.registerUser(request));

        assertEquals("Hashing error", exception.getMessage());

        // Verifica che i metodi del repository siano stati chiamati
        verify(utenteRepository, times(1)).findByUsername("validUser");
        verify(utenteRepository, times(1)).findByEmail("valid@example.com");

        // Assicurati che il metodo save non venga mai chiamato
        verify(utenteRepository, never()).save(any(Utente.class));
    }


    /**
     * Test per registrazione utente valida.
     */
    @Test
    void registerUser_ShouldReturnSuccessForValidRequest() throws NoSuchAlgorithmException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "newUser", "ValidPass1", "new@example.com", "RSSMRA85M01H501Z", "Domanda", "Risposta");

        when(utenteRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(utenteRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        // Mock del metodo di hashing
        UtenteService utenteServiceSpy = spy(utenteService);
        doReturn("hashedPassword").when(utenteServiceSpy).hashPassword("ValidPass1");

        String response = utenteServiceSpy.registerUser(request);

        assertEquals("Utente registrato con successo!", response);
        verify(utenteRepository, times(1)).findByUsername("newUser");
        verify(utenteRepository, times(1)).findByEmail("new@example.com");
        verify(utenteRepository, times(1)).save(any(Utente.class));
    }


    /**
     * Test per LoginRequest valido (username e password corretti).
     */
    @Test
    void authenticateUser_ShouldReturnJwtForValidCredentials() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest("validEmail@example.com", "ValidPass1"); // Usa un'email valida
        Utente utente = new Utente();
        utente.setUsername("validUser");
        utente.setPasswordHash("hashedPassword");

        when(utenteRepository.findByEmail("validEmail@example.com")).thenReturn(Optional.of(utente)); // Mock findByEmail

        // Usa uno spy per mockare il metodo hashPassword
        UtenteService utenteServiceSpy = spy(utenteService);
        doReturn("hashedPassword").when(utenteServiceSpy).hashPassword("ValidPass1");

        when(jwtUtils.generateToken("validUser")).thenReturn("generatedJwtToken");

        JwtResponse response = utenteServiceSpy.authenticateUser(request);

        assertNotNull(response);
        assertEquals("generatedJwtToken", response.getToken());
        verify(jwtUtils, times(1)).generateToken("validUser");
    }



    /**
     * Test per LoginRequest con username mancante.
     */
    @Test
    void authenticateUser_ShouldReturnNullForMissingUsername() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest(null, "ValidPass1");

        JwtResponse response = utenteService.authenticateUser(request);

        assertNull(response);
        verifyNoInteractions(utenteRepository, jwtUtils);
    }

    /**
     * Test per LoginRequest con password mancante.
     */
    @Test
    void authenticateUser_ShouldReturnNullForMissingPassword() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest("validUser", null);

        JwtResponse response = utenteService.authenticateUser(request);

        assertNull(response);
        verifyNoInteractions(utenteRepository, jwtUtils);
    }

    /**
     * Test per LoginRequest con credenziali errate.
     */
    @Test
    void authenticateUser_ShouldReturnNullForInvalidCredentials() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest("validUser", "WrongPass");
        Utente utente = new Utente();
        utente.setUsername("validUser");
        utente.setPasswordHash("hashedPassword");

        when(utenteRepository.findByUsername("validUser")).thenReturn(Optional.of(utente));

        // Mock del metodo hashPassword
        UtenteService utenteServiceSpy = spy(utenteService);
        doReturn("hashedWrongPassword").when(utenteServiceSpy).hashPassword("WrongPass");

        JwtResponse response = utenteServiceSpy.authenticateUser(request);

        assertNull(response);
        verify(jwtUtils, never()).generateToken(anyString());
    }

    /**
     * Test per LoginRequest con utente inesistente.
     */
    @Test
    void authenticateUser_ShouldReturnNullForNonExistentUser() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest("nonExistentUser", "ValidPass1");

        when(utenteRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        JwtResponse response = utenteService.authenticateUser(request);

        assertNull(response);
        verifyNoInteractions(jwtUtils);
    }

    /**
     * Test per errore nell'algoritmo di hashing.
     */
    @Test
    void authenticateUser_ShouldThrowExceptionForHashingError() throws NoSuchAlgorithmException {
        LoginRequest request = new LoginRequest("validEmail@example.com", "ValidPass1");
        Utente utente = new Utente();
        utente.setUsername("validUser");
        utente.setPasswordHash("hashedPassword");

        when(utenteRepository.findByEmail("validEmail@example.com")).thenReturn(Optional.of(utente));

        // Usa uno spy per simulare l'errore di hashing
        UtenteService utenteServiceSpy = spy(utenteService);
        doThrow(new NoSuchAlgorithmException("Hashing error"))
                .when(utenteServiceSpy)
                .hashPassword("ValidPass1");

        // Verifica che l'eccezione venga lanciata
        Exception exception = assertThrows(NoSuchAlgorithmException.class, () -> utenteServiceSpy.authenticateUser(request));

        assertEquals("Hashing error", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyString());
    }

    /**
     * Test per input valido: stringa non vuota.
     */
    @Test
    void hashPassword_ShouldReturnHashedPasswordForValidInput() throws NoSuchAlgorithmException {
        String password = "ValidPassword123";

        String hashedPassword = utenteService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        // Verifica che l'output sia una stringa di caratteri esadecimali
        assertTrue(hashedPassword.matches("^[a-fA-F0-9]+$"));
    }

    /**
     * Test per input nullo.
     */
    @Test
    void hashPassword_ShouldThrowExceptionForNullInput() {
        String password = null;

        Exception exception = assertThrows(NullPointerException.class, () -> utenteService.hashPassword(password));

        assertEquals("Cannot invoke \"String.getBytes()\" because \"password\" is null", exception.getMessage());
    }

    /**
     * Test per input vuoto.
     */
    @Test
    void hashPassword_ShouldReturnHashedPasswordForEmptyInput() throws NoSuchAlgorithmException {
        String password = "";

        String hashedPassword = utenteService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        // Verifica che l'output sia una stringa di caratteri esadecimali
        assertTrue(hashedPassword.matches("^[a-fA-F0-9]+$"));
    }

    /**
     * Test per errore nell'algoritmo di hashing.
     */
    @Test
    void hashPassword_ShouldThrowExceptionForUnsupportedAlgorithm() throws NoSuchAlgorithmException {
        // Crea un'istanza di UtenteService con un algoritmo non supportato
        UtenteService brokenService = new UtenteService() {
            @Override
            public String hashPassword(String password) throws NoSuchAlgorithmException {
                MessageDigest digest = MessageDigest.getInstance("InvalidAlgorithm");
                byte[] encodedhash = digest.digest(password.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : encodedhash) {
                    hexString.append(String.format("%02x", b));
                }
                return hexString.toString();
            }
        };

        Exception exception = assertThrows(NoSuchAlgorithmException.class, () -> brokenService.hashPassword("Password123"));

        assertEquals("InvalidAlgorithm MessageDigest not available", exception.getMessage());
    }

    /**
     * Test per username valido: utente trovato.
     */
    @Test
    void findByUsername_ShouldReturnUserForValidUsername() {
        String username = "validUser";
        Utente utente = new Utente();
        utente.setUsername(username);

        when(utenteRepository.findByUsername(username)).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(utenteRepository, times(1)).findByUsername(username);
    }

    /**
     * Test per username valido: nessun utente trovato.
     */
    @Test
    void findByUsername_ShouldReturnEmptyForNonExistentUsername() {
        String username = "nonExistentUser";

        when(utenteRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Utente> result = utenteService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(utenteRepository, times(1)).findByUsername(username);
    }

    /**
     * Test per username nullo.
     */
    @Test
    void findByUsername_ShouldThrowExceptionForNullUsername() {
        String username = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> utenteService.findByUsername(username));

        assertEquals("Username cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per username vuoto.
     */
    @Test
    void findByUsername_ShouldThrowExceptionForEmptyUsername() {
        String username = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> utenteService.findByUsername(username));

        assertEquals("Username cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per email e codice fiscale validi: utente trovato.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldReturnUserForValidEmailAndCodiceFiscale() {
        String email = "valid@example.com";
        String codiceFiscale = "RSSMRA85M01H501Z";
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setCodiceFiscale(codiceFiscale);

        when(utenteRepository.findByEmailAndCodiceFiscale(email, codiceFiscale)).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals(codiceFiscale, result.get().getCodiceFiscale());
        verify(utenteRepository, times(1)).findByEmailAndCodiceFiscale(email, codiceFiscale);
    }

    /**
     * Test per email e codice fiscale validi: nessun utente trovato.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldReturnEmptyForNonExistentUser() {
        String email = "nonexistent@example.com";
        String codiceFiscale = "RSSMRA85M01H501Z";

        when(utenteRepository.findByEmailAndCodiceFiscale(email, codiceFiscale)).thenReturn(Optional.empty());

        Optional<Utente> result = utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale);

        assertFalse(result.isPresent());
        verify(utenteRepository, times(1)).findByEmailAndCodiceFiscale(email, codiceFiscale);
    }

    /**
     * Test per email mancante.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldThrowExceptionForMissingEmail() {
        String email = null;
        String codiceFiscale = "RSSMRA85M01H501Z";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale));

        assertEquals("Email and codice fiscale cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per codice fiscale mancante.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldThrowExceptionForMissingCodiceFiscale() {
        String email = "valid@example.com";
        String codiceFiscale = null;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale));

        assertEquals("Email and codice fiscale cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per email vuota.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldThrowExceptionForEmptyEmail() {
        String email = "";
        String codiceFiscale = "RSSMRA85M01H501Z";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale));

        assertEquals("Email and codice fiscale cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per codice fiscale vuoto.
     */
    @Test
    void findByEmailAndCodiceFiscale_ShouldThrowExceptionForEmptyCodiceFiscale() {
        String email = "valid@example.com";
        String codiceFiscale = "";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> utenteService.findByEmailAndCodiceFiscale(email, codiceFiscale));

        assertEquals("Email and codice fiscale cannot be null or empty", exception.getMessage());
        verifyNoInteractions(utenteRepository);
    }

    /**
     * Test per token valido con utente corrispondente.
     */
    @Test
    void getUserFromToken_ShouldReturnUserForValidToken() {
        String token = "validToken";
        String username = "validUser";
        Utente utente = new Utente();
        utente.setUsername(username);

        when(jwtUtils.validateToken(token)).thenReturn(username);
        when(utenteRepository.findByUsername(username)).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, times(1)).findByUsername(username);
    }

    /**
     * Test per token valido senza utente corrispondente.
     */
    @Test
    void getUserFromToken_ShouldReturnEmptyForValidTokenButNoUser() {
        String token = "validToken";
        String username = "validUser";

        when(jwtUtils.validateToken(token)).thenReturn(username);
        when(utenteRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertFalse(result.isPresent());
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, times(1)).findByUsername(username);
    }

    /**
     * Test per token null.
     */
    @Test
    void getUserFromToken_ShouldReturnEmptyForNullToken() {
        String token = null;

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertFalse(result.isPresent());
        verify(jwtUtils, never()).validateToken(anyString());
        verify(utenteRepository, never()).findByUsername(anyString());
    }

    /**
     * Test per token vuoto.
     */
    @Test
    void getUserFromToken_ShouldReturnEmptyForEmptyToken() {
        String token = "";

        when(jwtUtils.validateToken(token)).thenReturn(null);

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertFalse(result.isPresent());
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, never()).findByUsername(anyString());
    }

    /**
     * Test per token malformato.
     */
    @Test
    void getUserFromToken_ShouldReturnEmptyForMalformedToken() {
        String token = "malformedToken";

        when(jwtUtils.validateToken(token)).thenReturn(null);

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertFalse(result.isPresent());
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, never()).findByUsername(anyString());
    }

    /**
     * Test per token scaduto.
     */
    @Test
    void getUserFromToken_ShouldReturnEmptyForExpiredToken() {
        String token = "expiredToken";

        when(jwtUtils.validateToken(token)).thenReturn(null);

        Optional<Utente> result = utenteService.getUserFromToken(token);

        assertFalse(result.isPresent());
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, never()).findByUsername(anyString());
    }

    /**
     * Test per token valido con utente corrispondente.
     */
    @Test
    void deleteUser_ShouldReturnSuccessForValidToken() throws NoSuchAlgorithmException {
        String token = "validToken";
        String username = "validUser";
        String password = "validPass1";
        String hashedPassword = "hashedValidPass1"; // Simula l'hash della password
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash(hashedPassword);

        when(jwtUtils.validateToken(token)).thenReturn(username);
        when(utenteRepository.findByUsername(username)).thenReturn(Optional.of(utente));

        // Mock del metodo hashPassword per restituire l'hash corretto
        UtenteService utenteServiceSpy = spy(utenteService);
        doReturn(hashedPassword).when(utenteServiceSpy).hashPassword(password);

        String result = utenteServiceSpy.deleteUser(token, password);

        assertEquals("Account eliminato con successo.", result);
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, times(1)).findByUsername(username);
        verify(utenteRepository, times(1)).delete(utente);
    }


    /**
     * Test per token valido ma utente non trovato.
     */
    @Test
    void deleteUser_ShouldReturnErrorForUserNotFound() {
        String token = "validToken";
        String username = "nonExistentUser";
        String password = "validPass1";

        when(jwtUtils.validateToken(token)).thenReturn(username);
        when(utenteRepository.findByUsername(username)).thenReturn(Optional.empty());

        String result = utenteService.deleteUser(token,password);

        assertEquals("Utente non trovato.", result);
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, times(1)).findByUsername(username);
        verify(utenteRepository, never()).delete(any(Utente.class));
    }

    /**
     * Test per token non valido.
     */
    @Test
    void deleteUser_ShouldReturnErrorForInvalidToken() {
        String token = "invalidToken";
        String password = "validPass1";
        when(jwtUtils.validateToken(token)).thenReturn(null);

        String result = utenteService.deleteUser(token,password);

        assertEquals("Token non valido.", result);
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, never()).findByUsername(anyString());
        verify(utenteRepository, never()).delete(any(Utente.class));
    }

    /**
     * Test per token null.
     */
    @Test
    void deleteUser_ShouldReturnErrorForNullToken() {
        String token = null;
        String password = "validPass1";
        String result = utenteService.deleteUser(token,password);

        assertEquals("Token non valido.", result);
        verify(jwtUtils, never()).validateToken(anyString());
        verify(utenteRepository, never()).findByUsername(anyString());
        verify(utenteRepository, never()).delete(any(Utente.class));
    }

    /**
     * Test per errore generico durante l'eliminazione.
     */
    @Test
    void deleteUser_ShouldReturnErrorForGenericException() throws NoSuchAlgorithmException {
        String token = "validToken";
        String username = "validUser";
        String password = "validPass1"; // Password in chiaro
        String hashedPassword = utenteService.hashPassword(password); // Genera l'hash della password

        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash(hashedPassword); // Imposta l'hash corretto

        // Simula il comportamento dei metodi
        when(jwtUtils.validateToken(token)).thenReturn(username);
        when(utenteRepository.findByUsername(username)).thenReturn(Optional.of(utente));
        doThrow(new RuntimeException("Generic error")).when(utenteRepository).delete(utente);

        String result = utenteService.deleteUser(token, password);

        assertEquals("Errore durante l'eliminazione dell'account.", result);
        verify(jwtUtils, times(1)).validateToken(token);
        verify(utenteRepository, times(1)).findByUsername(username);
        verify(utenteRepository, times(1)).delete(utente);
    }

}
