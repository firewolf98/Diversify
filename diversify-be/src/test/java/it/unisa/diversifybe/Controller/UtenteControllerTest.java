package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.*;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Service.UtenteService;
import it.unisa.diversifybe.Utilities.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtenteControllerTest {
    /**
     * **Partizioni dei metodi del controller per il testing (Category Partition):**
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
     *     - Credenziali errate.
     * **Metodo `registerUser`**:
     * - **RegisterRequest**:
     *   - Valido:
     *     - Dati completi e corretti.
     *   - Non valido:
     *     - Username mancante o vuoto.
     *     - Password mancante o vuota.
     *     - Email mancante o vuota.
     *     - Dati già esistenti nel sistema (username o email duplicati).
     * - **Esito della registrazione**:
     *   - Successo (utente registrato).
     *   - Fallimento:
     *     - Algoritmo di hashing non supportato.
     *     - Dati non validi o duplicati.
     * **Metodo `changePassword`**:
     * - **AuthorizationHeader**:
     *   - Valido:
     *     - Token corretto con utente corrispondente.
     *   - Non valido:
     *     - Token mancante.
     *     - Token vuoto o malformato.
     *     - Token scaduto o non valido.
     * - **ChangePasswordRequest**:
     *   - Valido:
     *     - Username corretto, password corrente valida, e nuova password valida.
     *   - Non valido:
     *     - Username mancante o vuoto.
     *     - Password corrente mancante o errata.
     *     - Nuova password mancante o vuota.
     * - **Esito del cambio password**:
     *   - Successo.
     *   - Fallimento:
     *     - Utente non trovato.
     *     - Password corrente errata.
     *     - Algoritmo di hashing non supportato.
     * **Metodo `recuperaPassword`**:
     * - **RecuperaPasswordRequest**:
     *   - Valido:
     *     - Email corretta, codice fiscale valido, risposta personale corretta e nuova password valida.
     *   - Non valido:
     *     - Email mancante o vuota.
     *     - Codice fiscale mancante o vuoto.
     *     - Risposta personale mancante, vuota o errata.
     *     - Nuova password mancante o vuota.
     * - **Esito del recupero password**:
     *   - Successo.
     *   - Fallimento:
     *     - Utente non trovato.
     *     - Risposta personale errata.
     *     - Algoritmo di hashing non supportato.
     * **Metodo `getUserFromToken`**:
     * - **Token JWT**:
     *   - Valido:
     *     - Token corretto con utente corrispondente.
     *   - Non valido:
     *     - Token mancante.
     *     - Token vuoto o malformato.
     *     - Token scaduto o non valido.
     * - **Esito del recupero utente**:
     *   - Successo.
     *   - Fallimento:
     *     - Token non valido.
     *     - Utente non trovato.
     * **Metodo `deleteAccount`**:
     * - **AuthorizationHeader**:
     *   - Valido:
     *     - Token corretto con utente corrispondente.
     *   - Non valido:
     *     - Token mancante.
     *     - Token vuoto o malformato.
     *     - Token scaduto o non valido.
     * - **Esito dell'eliminazione**:
     *   - Successo.
     *   - Fallimento:
     *     - Utente non trovato.
     *     - Token non valido.
     */


    @InjectMocks
    private UtenteController controller;

    @Mock
    private UtenteService utenteService;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per il metodo authenticateUser
     */

    @Test
    void authenticateUser_ShouldReturnJwtOnSuccessfulAuthentication() throws NoSuchAlgorithmException {
        LoginRequest loginRequest = new LoginRequest("testuser", "testpassword");
        JwtResponse jwtResponse = new JwtResponse("jwt-token");

        when(utenteService.authenticateUser(loginRequest)).thenReturn(jwtResponse);

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponse, response.getBody());
    }

    @Test
    void authenticateUser_ShouldReturnUnauthorizedForInvalidCredentials() throws NoSuchAlgorithmException {
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        when(utenteService.authenticateUser(loginRequest)).thenReturn(null);

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Username o password errati", response.getBody());
    }

    @Test
    void authenticateUser_ShouldReturnInternalServerErrorOnHashingError() throws NoSuchAlgorithmException {
        LoginRequest loginRequest = new LoginRequest("testuser", "testpassword");

        when(utenteService.authenticateUser(loginRequest)).thenThrow(new NoSuchAlgorithmException("Hashing error"));

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Errore interno durante la verifica della password", response.getBody());
    }

    @Test
    void authenticateUser_ShouldReturnBadRequestForMissingUsername() {
        LoginRequest loginRequest = new LoginRequest(null, "testpassword");

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Username o password errati", response.getBody());
    }

    @Test
    void authenticateUser_ShouldReturnBadRequestForEmptyPassword() {
        LoginRequest loginRequest = new LoginRequest("testuser", "");

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Username o password errati", response.getBody());
    }
    /**
     * Test per il metodo registerUser - Registrazione con dati validi.
     */
    @Test
    void registerUser_ShouldReturnSuccessMessageForValidData() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "ValidPass1", "testuser@example.com", "CF12345", "Domanda", "Risposta");
        String successMessage = "Utente registrato con successo!";

        when(utenteService.registerUser(registerRequest)).thenReturn(successMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("message", successMessage), response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }


    /**
     * Test per il metodo registerUser - Username mancante.
     */
    @Test
    void registerUser_ShouldReturnBadRequestForMissingUsername() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", null, "ValidPass1", "testuser@example.com", "CF12345", "Domanda", "Risposta");
        String errorMessage = "Il campo username è obbligatorio.";

        when(utenteService.registerUser(registerRequest)).thenReturn(errorMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo registerUser - Password vuota.
     */
    @Test
    void registerUser_ShouldReturnBadRequestForEmptyPassword() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "", "testuser@example.com", "CF12345", "Domanda", "Risposta");
        String errorMessage = "Il campo password è obbligatorio.";

        when(utenteService.registerUser(registerRequest)).thenReturn(errorMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo registerUser - Email mancante.
     */
    @Test
    void registerUser_ShouldReturnBadRequestForMissingEmail() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "ValidPass1", null, "CF12345", "Domanda", "Risposta");
        String errorMessage = "Il campo email è obbligatorio.";

        when(utenteService.registerUser(registerRequest)).thenReturn(errorMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo registerUser - Codice fiscale mancante.
     */
    @Test
    void registerUser_ShouldReturnBadRequestForMissingCodiceFiscale() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "ValidPass1", "testuser@example.com", null, "Domanda", "Risposta");
        String errorMessage = "Il campo codice fiscale è obbligatorio.";

        when(utenteService.registerUser(registerRequest)).thenReturn(errorMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo registerUser - Dati duplicati.
     */
    @Test
    void registerUser_ShouldReturnBadRequestForDuplicateData() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "ValidPass1", "testuser@example.com", "CF12345", "Domanda", "Risposta");
        String errorMessage = "Username o email già in uso.";

        when(utenteService.registerUser(registerRequest)).thenReturn(errorMessage);

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo registerUser - Errore nell'algoritmo di hashing.
     */
    @Test
    void registerUser_ShouldReturnInternalServerErrorForHashingError() throws NoSuchAlgorithmException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "testuser", "ValidPass1", "testuser@example.com", "CF12345", "Domanda", "Risposta");

        when(utenteService.registerUser(registerRequest)).thenThrow(new NoSuchAlgorithmException("Hashing error"));

        ResponseEntity<?> response = controller.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Errore durante la registrazione", response.getBody());
        verify(utenteService, times(1)).registerUser(registerRequest);
    }

    /**
     * Test per il metodo changePassword
     */

    @Test
    void changePassword_ShouldReturnSuccessForValidData() throws NoSuchAlgorithmException {
        String token = "Bearer validToken";
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword");
        String username = "testuser";
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash("hashedCurrentPassword");

        when(jwtUtils.validateToken("validToken")).thenReturn(username);
        when(utenteService.findByUsername(username)).thenReturn(Optional.of(utente));
        when(utenteService.hashPassword("currentPassword")).thenReturn("hashedCurrentPassword");
        when(utenteService.hashPassword("newPassword")).thenReturn("hashedNewPassword");

        ResponseEntity<?> response = controller.changePassword(request, token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("message", "Password aggiornata con successo"), response.getBody());  // Modifica qui per confrontare con la mappa
        verify(utenteService).save(utente);
    }

    @Test
    void changePassword_ShouldReturnUnauthorizedForInvalidToken() {
        String token = "Bearer invalidToken";
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword");

        when(jwtUtils.validateToken("invalidToken")).thenReturn(null);

        ResponseEntity<?> response = controller.changePassword(request, token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido", response.getBody());
        verifyNoInteractions(utenteService);
    }

    @Test
    void changePassword_ShouldReturnNotFoundForNonExistentUser() {
        String token = "Bearer validToken";
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword");
        String username = "testuser";

        // Simula il token valido che restituisce un username
        when(jwtUtils.validateToken("validToken")).thenReturn(username);

        // Simula il caso in cui l'utente non viene trovato
        when(utenteService.findByUsername(username)).thenReturn(Optional.empty());

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.changePassword(request, token);

        // Verifica il risultato
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Utente non trovato", response.getBody());

        // Assicura che non ci siano ulteriori interazioni con utenteService
        verify(utenteService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(utenteService);
    }


    @Test
    void changePassword_ShouldReturnUnauthorizedForIncorrectCurrentPassword() throws NoSuchAlgorithmException {
        String token = "Bearer validToken";
        ChangePasswordRequest request = new ChangePasswordRequest("wrongPassword", "newPassword");
        String username = "testuser";
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash("hashedCurrentPassword");

        // Simula il token valido che restituisce un username
        when(jwtUtils.validateToken("validToken")).thenReturn(username);

        // Simula che l'utente esiste nel database
        when(utenteService.findByUsername(username)).thenReturn(Optional.of(utente));

        // Simula l'hashing della password corrente fornita dall'utente
        when(utenteService.hashPassword("wrongPassword")).thenReturn("hashedWrongPassword");

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.changePassword(request, token);

        // Verifica il risultato
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Password corrente errata", response.getBody());

        // Assicura che non ci siano ulteriori interazioni con utenteService dopo il controllo della password
        verify(utenteService, times(1)).findByUsername(username);
        verify(utenteService, times(1)).hashPassword("wrongPassword");
        verifyNoMoreInteractions(utenteService);
    }


    @Test
    void changePassword_ShouldReturnInternalServerErrorForHashingError() throws NoSuchAlgorithmException {
        String token = "Bearer validToken";
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword");
        String username = "testuser";
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash("hashedCurrentPassword");

        // Simula il comportamento dei metodi del mock
        when(jwtUtils.validateToken("validToken")).thenReturn(username);
        when(utenteService.findByUsername(username)).thenReturn(Optional.of(utente));

        // Simula un errore nell'hashing della password
        doThrow(new NoSuchAlgorithmException("Hashing error"))
                .when(utenteService)
                .hashPassword("currentPassword");

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.changePassword(request, token);

        // Verifica il risultato
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Errore nell'hashing della password", response.getBody());

        // Verifica le interazioni previste con i mock
        verify(jwtUtils, times(1)).validateToken("validToken");
        verify(utenteService, times(1)).findByUsername(username);
        verify(utenteService, times(1)).hashPassword("currentPassword");
        verifyNoMoreInteractions(jwtUtils, utenteService);
    }

    /**
     * Test per recuperaPassword con dati validi.
     */
    @Test
    void recuperaPassword_ShouldReturnSuccessForValidData() throws NoSuchAlgorithmException {
        // Dati di input
        RecuperaPasswordRequest request = new RecuperaPasswordRequest("user@example.com", "1234567890", "correctAnswer", "newPassword");

        // Utente simulato
        Utente utente = new Utente();
        utente.setEmail("user@example.com");
        utente.setCodiceFiscale("1234567890");
        utente.setRispostaHash("hashedCorrectAnswer");  // Risposta criptata

        // Simula l'hashing della risposta alla domanda personale (dovrebbe essere la stessa risposta criptata nel database)
        when(utenteService.hashPassword("correctAnswer")).thenReturn("hashedCorrectAnswer");  // Simula l'hashing della risposta
        when(utenteService.hashPassword("newPassword")).thenReturn("hashedNewPassword");  // Simula l'hashing della nuova password

        // Simula il comportamento dei metodi
        when(utenteService.findByEmailAndCodiceFiscale("user@example.com", "1234567890"))
                .thenReturn(Optional.of(utente));

        // Esegui il metodo
        ResponseEntity<?> response = controller.recuperaPassword(request);

        // Verifica la risposta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password aggiornata con successo", response.getBody());
        verify(utenteService).save(utente);
    }



    /**
     * Test per recuperaPassword con utente non trovato.
     */
    @Test
    void recuperaPassword_ShouldReturnNotFoundForNonExistentUser() {
        RecuperaPasswordRequest request = new RecuperaPasswordRequest("user@example.com", "1234567890", "correctAnswer", "newPassword");

        // Configura il comportamento del mock
        when(utenteService.findByEmailAndCodiceFiscale("user@example.com", "1234567890"))
                .thenReturn(Optional.empty());

        // Esegui il metodo
        ResponseEntity<?> response = controller.recuperaPassword(request);

        // Verifica il risultato
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Utente non trovato con i dati forniti", response.getBody());

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(utenteService).findByEmailAndCodiceFiscale("user@example.com", "1234567890");

        // Verifica che non ci siano altre interazioni
        verifyNoMoreInteractions(utenteService);
    }

    /**
     * Test per `recuperaPassword` con risposta personale errata.
     */
    @Test
    void recuperaPassword_ShouldReturnUnauthorizedForIncorrectAnswer() throws NoSuchAlgorithmException {
        RecuperaPasswordRequest request = new RecuperaPasswordRequest("user@example.com", "1234567890", "wrongAnswer", "newPassword");
        Utente utente = new Utente();
        utente.setEmail("user@example.com");
        utente.setCodiceFiscale("1234567890");
        utente.setRispostaHash("hashedCorrectAnswer");

        // Configura il comportamento dei mock
        when(utenteService.findByEmailAndCodiceFiscale("user@example.com", "1234567890"))
                .thenReturn(Optional.of(utente));
        when(utenteService.hashPassword("wrongAnswer")).thenReturn("hashedWrongAnswer");

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.recuperaPassword(request);

        // Verifica il risultato della risposta
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Risposta alla domanda personale errata", response.getBody());

        // Verifica che siano state eseguite solo le chiamate previste
        verify(utenteService).findByEmailAndCodiceFiscale("user@example.com", "1234567890");
        verify(utenteService).hashPassword("wrongAnswer");
        verifyNoMoreInteractions(utenteService);
    }

    /**
     * Test per `recuperaPassword` con email mancante.
     */
    @Test
    void recuperaPassword_ShouldReturnBadRequestForMissingEmail() {
        // Configura la richiesta con un'email mancante
        RecuperaPasswordRequest request = new RecuperaPasswordRequest(null, "1234567890", "correctAnswer", "newPassword");

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.recuperaPassword(request);

        // Verifica il risultato della risposta
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("L'email non può essere nulla o vuota.", response.getBody());

        // Verifica che il servizio non venga mai chiamato
        verifyNoInteractions(utenteService);
    }

    /**
     * Test per `recuperaPassword` con codice fiscale mancante.
     */
    @Test
    void recuperaPassword_ShouldReturnBadRequestForMissingCodiceFiscale() {
        // Configura la richiesta con un codice fiscale mancante
        RecuperaPasswordRequest request = new RecuperaPasswordRequest("user@example.com", null, "correctAnswer", "newPassword");

        // Esegui il metodo da testare
        ResponseEntity<?> response = controller.recuperaPassword(request);

        // Verifica il risultato della risposta
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Il codice fiscale non può essere nullo o vuoto.", response.getBody());

        // Verifica che il servizio non venga mai chiamato
        verifyNoInteractions(utenteService);
    }


    /**
     * Test per getUserFromToken
     * Test per token valido e utente corrispondente.
     */
    @Test
    void getUserFromToken_ShouldReturnUserForValidToken() {
        String token = "Bearer validToken";
        Utente utente = new Utente();
        utente.setUsername("testuser");

        when(utenteService.getUserFromToken("validToken")).thenReturn(Optional.of(utente));

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utente, response.getBody());
        verify(utenteService).getUserFromToken("validToken");
    }

    /**
     * Test per token mancante.
     */
    @Test
    void getUserFromToken_ShouldReturnUnauthorizedForMissingToken() {
        String token = null;

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido o utente non trovato.", response.getBody());
        verifyNoInteractions(utenteService);
    }

    /**
     * Test per token vuoto.
     */
    @Test
    void getUserFromToken_ShouldReturnUnauthorizedForEmptyToken() {
        String token = "";

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido o utente non trovato.", response.getBody());
        verifyNoInteractions(utenteService);
    }

    /**
     * Test per token malformato.
     */
    @Test
    void getUserFromToken_ShouldReturnUnauthorizedForMalformedToken() {
        String token = "InvalidTokenFormat";

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido o utente non trovato.", response.getBody());
        verifyNoInteractions(utenteService);
    }

    /**
     * Test per token scaduto o non valido.
     */
    @Test
    void getUserFromToken_ShouldReturnUnauthorizedForInvalidToken() {
        String token = "Bearer expiredToken";

        when(utenteService.getUserFromToken("expiredToken")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido o utente non trovato.", response.getBody());
        verify(utenteService).getUserFromToken("expiredToken");
    }

    /**
     * Test per utente non trovato.
     */
    @Test
    void getUserFromToken_ShouldReturnUnauthorizedForUserNotFound() {
        String token = "Bearer validToken";

        when(utenteService.getUserFromToken("validToken")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getUserFromToken(token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido o utente non trovato.", response.getBody());
        verify(utenteService).getUserFromToken("validToken");
    }

    /**
     * Test per token corretto con utente corrispondente.
     */
    @Test
    void deleteAccount_ShouldReturnSuccessForValidToken() {
        String token = "Bearer validToken";
        String expectedResponse = "Account eliminato con successo";
        String password = "validPass1";

        // Configura il comportamento del mock del servizio
        when(utenteService.deleteUser("validToken", password)).thenReturn(expectedResponse);

        // Esegui il metodo deleteAccount
        ResponseEntity<?> response = controller.deleteAccount(password, token);

        // Verifica la risposta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        // Verifica che il metodo deleteUser sia stato chiamato con i parametri corretti
        verify(utenteService, times(1)).deleteUser("validToken", password);
    }

    /**
     * Test per token mancante.
     */
    @Test
    void deleteAccount_ShouldReturnUnauthorizedForMissingToken() {
        String token = null;
        String password = "validPassword";
        ResponseEntity<?> response = controller.deleteAccount(token,password);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token malformato", response.getBody());
        verifyNoInteractions(utenteService);
    }

    /**
     * Test per token vuoto.
     */
    @Test
    void deleteAccount_ShouldReturnUnauthorizedForEmptyToken() {
        String token = "";
        String password = "validPassword";
        ResponseEntity<?> response = controller.deleteAccount(token, password);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token malformato", response.getBody());  // Ora il messaggio sarà "Token non valido"
        verifyNoInteractions(utenteService);
    }


    /**
     * Test per token malformato.
     */
    @Test
    void deleteAccount_ShouldReturnUnauthorizedForMalformedToken() {
        String token = "InvalidTokenFormat";  // Token malformato
        String password = "validPassword";
        ResponseEntity<?> response = controller.deleteAccount(token,password);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token malformato", response.getBody());
        verifyNoInteractions(utenteService);  // Non dovrebbe essere chiamato il servizio
    }


    /**
     * Test per token scaduto o non valido.
     */
    @Test
    void deleteAccount_ShouldReturnUnauthorizedForExpiredOrInvalidToken() {
        String token = "Bearer invalidToken";
        String password = "validPassword";
        when(utenteService.deleteUser("invalidToken", "validPassword")).thenReturn("Token non valido");

        ResponseEntity<?> response = controller.deleteAccount(password, token);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token non valido", response.getBody());
        verify(utenteService, times(1)).deleteUser("invalidToken", "validPassword");
    }

    /**
     * Test per utente non trovato.
     */
    @Test
    void deleteAccount_ShouldReturnNotFoundForNonExistentUser() {
        String token = "validToken";
        String password = "validPassword";

        when(utenteService.deleteUser("validToken", "validPassword")).thenReturn("Utente non trovato");

        ResponseEntity<?> response = controller.deleteAccount(password, "Bearer " + token);  // Aggiungi il prefisso qui

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Utente non trovato", response.getBody());
        verify(utenteService, times(1)).deleteUser("validToken", "validPassword");
    }
}
