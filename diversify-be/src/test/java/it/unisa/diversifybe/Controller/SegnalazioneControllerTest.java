package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Segnalazione;
import it.unisa.diversifybe.Service.SegnalazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SegnalazioneControllerTest {

    /*
     *   Metodo testGetAllSegnalazioni:
     *   Caso Lista Popolata: Simula la presenza di due segnalazioni nella lista e verifica che la dimensione sia corretta.
     *   Caso Lista Vuota: Non gestito esplicitamente, ma restituirebbe una lista vuota senza errori.
     *
     *   Metodo testGetSegnalazioneById_Found:
     *   Caso Segnalazione Trovata: Simula il recupero di una segnalazione con un ID specifico, verifica che i dati siano corretti
     *   e che lo stato restituito sia OK.
     *
     *   Metodo testGetSegnalazioneById_NotFound:
     *   Caso Segnalazione Non Trovata: Simula un ID inesistente e verifica che lo stato restituito sia NOT_FOUND e il body sia null.
     *
     *   Metodo testCreateSegnalazione_Success:
     *   Caso Creazione Riuscita: Simula la creazione di una nuova segnalazione, verifica che lo stato restituito sia CREATED
     *   e il body contenga la segnalazione.
     *
     *   Metodo testCreateSegnalazione_Failure:
     *   Caso Creazione Fallita: Simula un errore durante la creazione della segnalazione, verifica che lo stato restituito sia BAD_REQUEST
     *   e il body contenga un messaggio di errore appropriato.
     *
     *   Metodo testUpdateSegnalazione_Found:
     *   Caso Aggiornamento Riuscito: Simula l'aggiornamento di una segnalazione esistente, verifica che lo stato restituito sia OK
     *   e il body contenga la segnalazione aggiornata.
     *
     *   Metodo testUpdateSegnalazione_NotFound:
     *   Caso Aggiornamento Fallito: Simula un ID inesistente e verifica che lo stato restituito sia NOT_FOUND e il body contenga un messaggio di errore.
     *
     *   Metodo testDeleteSegnalazione_Found:
     *   Caso Eliminazione Riuscita: Simula l'eliminazione di una segnalazione esistente, verifica che lo stato restituito sia NO_CONTENT
     *   e che il servizio sia chiamato una volta.
     *
     *   Metodo testDeleteSegnalazione_NotFound:
     *   Caso Eliminazione Fallita: Simula un ID inesistente e verifica che lo stato restituito sia NOT_FOUND e il body contenga un messaggio di errore.
     *
     *   Metodo testGetSegnalazioniByUtente:
     *   Caso Utente con Segnalazioni: Simula un utente con due segnalazioni associate, verifica che la dimensione della lista sia corretta
     *   e che i dati delle segnalazioni corrispondano.
     *   Caso Utente senza Segnalazioni: Non gestito esplicitamente, ma restituirebbe una lista vuota senza errori.
     */

    @Mock
    private SegnalazioneService segnalazioneService;

    @InjectMocks
    private SegnalazioneController segnalazioneController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Categoria: Recupero di tutte le segnalazioni
    @Test
    void testGetAllSegnalazioni() {
        // Configurazione delle segnalazioni con valori di prova
        Segnalazione segnalazione1 = new Segnalazione();
        segnalazione1.setIdSegnalazione("1");
        segnalazione1.setIdSegnalante("User123");
        segnalazione1.setIdSegnalato("User456");
        segnalazione1.setMotivazione("Contenuto inappropriato");
        segnalazione1.setDataSegnalazione(new Date());
        segnalazione1.setTipoSegnalazione("Abuso");

        Segnalazione segnalazione2 = new Segnalazione();
        segnalazione2.setIdSegnalazione("2");
        segnalazione2.setIdSegnalante("User321");
        segnalazione2.setIdSegnalato("User654");
        segnalazione2.setMotivazione("Urto alla sensibilità");
        segnalazione2.setDataSegnalazione(new Date());
        segnalazione2.setTipoSegnalazione("Abuso");

        // Lista delle segnalazioni
        List<Segnalazione> segnalazioni = Arrays.asList(segnalazione1, segnalazione2);
        when(segnalazioneService.getAllSegnalazioni()).thenReturn(segnalazioni);

        // Test
        List<Segnalazione> result = segnalazioneController.getAllSegnalazioni();

        // Debugging output
        System.out.println("Response body: " + result);

        // Asserzioni
        assertEquals(2, result.size(), "The result should contain exactly 2 segnalazioni");
        assertEquals(segnalazione1, result.get(0), "The first segnalazione should match segnalazione1");
        assertEquals(segnalazione2, result.get(1), "The second segnalazione should match segnalazione2");

        // Verifica interazioni
        verify(segnalazioneService, times(1)).getAllSegnalazioni();
    }


    // Categoria: Recupero segnalazione per ID
    @Test
    void testGetSegnalazioneById_Found() {
        // Partizioni
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setIdSegnalazione("1");
        segnalazione.setIdSegnalante("User123");
        segnalazione.setIdSegnalato("User456");
        segnalazione.setMotivazione("Contenuto inappropriato");
        segnalazione.setDataSegnalazione(new Date());
        segnalazione.setTipoSegnalazione("Abuso");

        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.of(segnalazione));

        // Test
        ResponseEntity<Segnalazione> response = segnalazioneController.getSegnalazioneById("1");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "response body should not be NULL");
        assertEquals("1", response.getBody().getIdSegnalazione());
        verify(segnalazioneService, times(1)).getSegnalazioneById("1");
    }

    @Test
    void testGetSegnalazioneById_NotFound() {
        // Partizioni
        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.empty());

        // Test
        ResponseEntity<Segnalazione> response = segnalazioneController.getSegnalazioneById("1");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(segnalazioneService, times(1)).getSegnalazioneById("1");
    }

    // Categoria: Creazione di una segnalazione
    @Test
    void testCreateSegnalazione_Success() {
        // Partizioni
        Segnalazione segnalazione = new Segnalazione();
        when(segnalazioneService.createSegnalazione(segnalazione)).thenReturn(segnalazione);

        // Configurazione della segnalazione con valori di prova
        segnalazione.setIdSegnalazione("1");
        segnalazione.setIdSegnalante("User123");
        segnalazione.setIdSegnalato("User456");
        segnalazione.setMotivazione("Contenuto inappropriato");
        segnalazione.setDataSegnalazione(new Date());
        segnalazione.setTipoSegnalazione("Abuso");

        // Test
        ResponseEntity<?> response = segnalazioneController.createSegnalazione(segnalazione);
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(segnalazione, response.getBody());
        verify(segnalazioneService, times(1)).createSegnalazione(segnalazione);
    }

    @Test
    void testCreateSegnalazione_Failure() {
        // Partizioni
        Segnalazione segnalazione = new Segnalazione();
        when(segnalazioneService.createSegnalazione(segnalazione)).thenThrow(new RuntimeException());

        // Test
        ResponseEntity<?> response = segnalazioneController.createSegnalazione(segnalazione);
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Errore durante la creazione della segnalazione", response.getBody());
        verify(segnalazioneService, times(1)).createSegnalazione(segnalazione);
    }

    // Categoria: Aggiornamento di una segnalazione
    @Test
    void testUpdateSegnalazione_Found() {
        // Configurazione della segnalazione con valori di prova
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setIdSegnalazione("1");
        segnalazione.setIdSegnalante("User123");
        segnalazione.setIdSegnalato("User456");
        segnalazione.setMotivazione("Contenuto inappropriato");
        segnalazione.setDataSegnalazione(new Date());
        segnalazione.setTipoSegnalazione("Abuso");

        // Mock dei metodi del servizio
        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.of(segnalazione));
        when(segnalazioneService.updateSegnalazione(segnalazione)).thenReturn(segnalazione);

        // Test
        ResponseEntity<?> response = segnalazioneController.updateSegnalazione("1", segnalazione);

        // Debugging output
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        // Asserzioni
        assertEquals(HttpStatus.OK, response.getStatusCode(), "The status code should be 200 OK");
        assertNotNull(response.getBody(), "The response body should not be null");
        assertEquals(segnalazione, response.getBody(), "The response body should match the updated segnalazione");

        // Verifica interazioni
        verify(segnalazioneService, times(1)).updateSegnalazione(segnalazione);
    }

    @Test
    void testUpdateSegnalazione_NotFound() {
        // Partizioni
        Segnalazione segnalazione = new Segnalazione();
        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.empty());

        // Test
        ResponseEntity<?> response = segnalazioneController.updateSegnalazione("1", segnalazione);
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Segnalazione non trovata", response.getBody());
        verify(segnalazioneService, times(0)).updateSegnalazione(segnalazione);
    }

    // Categoria: Eliminazione di una segnalazione
    @Test
    void testDeleteSegnalazione_Found() {
        // Preparazione
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setIdSegnalato("2"); // Supponiamo che "2" sia l'ID del segnalato
        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.of(segnalazione));

        // Test
        ResponseEntity<?> response = segnalazioneController.deleteSegnalazione("1");

        // Output di debug
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        // Asserzioni
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(segnalazioneService, times(1)).deleteSegnalazione("1", "2"); // Passa entrambi i parametri
    }


    @Test
    void testDeleteSegnalazione_NotFound() {
        // Preparazione
        when(segnalazioneService.getSegnalazioneById("1")).thenReturn(Optional.empty());

        // Test
        ResponseEntity<?> response = segnalazioneController.deleteSegnalazione("1");

        // Output di debug
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        // Asserzioni
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Segnalazione non trovata", response.getBody());
        verify(segnalazioneService, times(0)).deleteSegnalazione(anyString(), anyString()); // Nessuna chiamata al metodo con due parametri
    }


    // Categoria: Recupero segnalazioni per utente
    @Test
    void testGetSegnalazioniByUtente() {
        // Configurazione delle segnalazioni con valori di prova
        Segnalazione segnalazione1 = new Segnalazione();
        segnalazione1.setIdSegnalazione("1");
        segnalazione1.setIdSegnalante("User1");
        segnalazione1.setIdSegnalato("User456");
        segnalazione1.setMotivazione("Contenuto inappropriato");
        segnalazione1.setDataSegnalazione(new Date());
        segnalazione1.setTipoSegnalazione("Abuso");

        Segnalazione segnalazione2 = new Segnalazione();
        segnalazione2.setIdSegnalazione("2");
        segnalazione2.setIdSegnalante("User2");
        segnalazione2.setIdSegnalato("User654");
        segnalazione2.setMotivazione("Urto alla sensibilità");
        segnalazione2.setDataSegnalazione(new Date());
        segnalazione2.setTipoSegnalazione("Abuso");

        // Lista delle segnalazioni
        List<Segnalazione> segnalazioni = Arrays.asList(segnalazione1, segnalazione2);
        when(segnalazioneService.getSegnalazioniByIdSegnalante("user1")).thenReturn(segnalazioni);

        // Test
        List<Segnalazione> result = segnalazioneController.getSegnalazioniByUtente("user1");

        // Debugging output
        System.out.println("L'utente che ha segnalato è user1");
        System.out.println("Response body: " + result);

        // Asserzioni
        assertEquals(2, result.size(), "The result should contain exactly 2 segnalazioni");
        assertEquals(segnalazione1, result.get(0), "The first segnalazione should match segnalazione1");
        assertEquals(segnalazione2, result.get(1), "The second segnalazione should match segnalazione2");

        // Verifica interazioni
        verify(segnalazioneService, times(1)).getSegnalazioniByIdSegnalante("user1");
    }
}