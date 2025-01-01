package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForumControllerTest {

    /*
**Partizioni dei metodi del `ForumController`:**

**Metodo `getAllForums`:**
- **Esito della richiesta:**
  - Successo (la lista di forum viene restituita).
  - Fallimento (lista vuota).

**Metodo `getForumById`:**
- **ID Forum:**
  - Valido (ID esistente nel database).
  - Non valido:
    - Null o vuoto.
    - ID inesistente.
- **Esito della richiesta:**
  - Successo (forum trovato).
  - Fallimento (forum non trovato).

**Metodo `addForum`:**
- **Ruolo dell'utente:**
  - Amministratore (autorizzato).
  - Non amministratore (non autorizzato).
- **Forum da aggiungere:**
  - Valido:
    - Tutti i campi richiesti presenti e validi.
  - Non valido:
    - Forum mancante.
    - Campi mancanti o non validi.
- **Esito della richiesta:**
  - Successo (forum aggiunto).
  - Fallimento:
    - Non autorizzato (errore 403).
    - Campi non validi (errore generico).

**Metodo `updateForum`:**
- **ID Forum:**
  - Valido (ID esistente nel database).
  - Non valido:
    - Null o vuoto.
    - ID inesistente.
- **Ruolo dell'utente:**
  - Amministratore (autorizzato).
  - Non amministratore (non autorizzato).
- **Forum aggiornato:**
  - Valido (campi aggiornati correttamente).
  - Non valido (campi mancanti o non validi).
- **Esito della richiesta:**
  - Successo (forum aggiornato).
  - Fallimento:
    - Non autorizzato (errore 403).
    - Forum non trovato (errore 404).
    - Campi non validi.

**Metodo `deleteForumAlternative`:**
- **ID Forum:**
  - Valido (ID esistente nel database).
  - Non valido:
    - Null o vuoto.
    - ID inesistente.
- **Ruolo dell'utente:**
  - Amministratore (autorizzato).
  - Non amministratore (non autorizzato).
- **Esito della richiesta:**
  - Successo (forum eliminato).
  - Fallimento:
    - Non autorizzato (errore 403).
    - Forum non trovato (errore 404).
    - Errore generico.

**Metodo `getForumsByPaese`:**
- **Paese:**
  - Valido (esistono forum associati al paese).
  - Non valido:
    - Null o vuoto.
    - Nessun forum associato al paese.
- **Esito della richiesta:**
  - Successo (lista di forum restituita).
  - Fallimento:
    - Nessun forum trovato (errore 404).
*/


    @InjectMocks
    private ForumController forumController;

    @Mock
    private ForumService forumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per `getAllForums` quando la lista di forum viene restituita correttamente.
     */
    @Test
    void getAllForums_ShouldReturnListOfForums() {
        // Simulazione di una lista di forum valida
        List<Forum> forums = new ArrayList<>();
        forums.add(new Forum("1", "Forum 1", "Descrizione 1", new ArrayList<>(), "Italia"));
        forums.add(new Forum("2", "Forum 2", "Descrizione 2", new ArrayList<>(), "Spagna"));

        // Configurazione del mock
        when(forumService.getAllForums()).thenReturn(forums);

        // Esecuzione del metodo
        ResponseEntity<List<Forum>> response = forumController.getAllForums();

        // Verifica degli esiti
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        assertEquals(forums, response.getBody());

        verify(forumService, times(1)).getAllForums();
    }

    /**
     * Test per `getAllForums` quando la lista è vuota.
     */
    @Test
    void getAllForums_ShouldReturnEmptyList() {
        // Simulazione di una lista vuota
        List<Forum> forums = new ArrayList<>();

        // Configurazione del mock
        when(forumService.getAllForums()).thenReturn(forums);

        // Esecuzione del metodo
        ResponseEntity<List<Forum>> response = forumController.getAllForums();

        // Verifica degli esiti
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());

        verify(forumService, times(1)).getAllForums();
    }

    /**
     * Test per `getForumById` con ID forum valido e forum trovato.
     */
    @Test
    void getForumById_ShouldReturnForumForValidId() {
        // Simulazione di un forum valido
        Forum forum = new Forum("1", "Forum 1", "Descrizione 1", null, "Italia");

        // Configurazione del mock
        when(forumService.getForumById("1")).thenReturn(Optional.of(forum));

        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.getForumById("1");

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forum, response.getBody());

        verify(forumService, times(1)).getForumById("1");
    }

    /**
     * Test per `getForumById` con ID forum valido ma forum non trovato.
     */
    @Test
    void getForumById_ShouldReturnNotFoundForNonExistentId() {
        // Configurazione del mock
        when(forumService.getForumById("999")).thenReturn(Optional.empty());

        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.getForumById("999");

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, times(1)).getForumById("999");
    }

    /**
     * Test per `getForumById` con ID forum null.
     */
    @Test
    void getForumById_ShouldHandleNullId() {
        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.getForumById(null);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, never()).getForumById(anyString());
    }

    /**
     * Test per `getForumById` con ID forum vuoto.
     */
    @Test
    void getForumById_ShouldHandleEmptyId() {
        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.getForumById("");

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, never()).getForumById(anyString());
    }


    /**
     * Test per `addForum` con utente amministratore e forum valido.
     */
    @Test
    void addForum_ShouldAddForumForAdminRole() {
        // Simulazione di un forum valido
        Forum forum = new Forum("1", "Forum 1", "Descrizione 1", null, "Italia");

        // Configurazione del mock
        when(forumService.addForum(forum, true)).thenReturn(forum);

        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.addForum(forum, true);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forum, response.getBody());

        verify(forumService, times(1)).addForum(forum, true);
    }

    /**
     * Test per `addForum` con utente non amministratore.
     */
    @Test
    void addForum_ShouldReturnForbiddenForNonAdminRole() {
        // Simulazione di un forum valido
        Forum forum = new Forum("1", "Forum 1", "Descrizione 1", null, "Italia");

        // Configurazione del mock per lanciare SecurityException
        doThrow(new SecurityException("Utente non autorizzato"))
                .when(forumService).addForum(forum, false);

        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.addForum(forum, false);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, times(1)).addForum(forum, false);
    }

    /**
     * Test per `addForum` con forum mancante.
     */
    @Test
    void addForum_ShouldHandleMissingForum() {
        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.addForum(null, true);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, never()).addForum(any(), anyBoolean());
    }

    /**
     * Test per `addForum` con campi mancanti nel forum.
     */
    @Test
    void addForum_ShouldHandleInvalidForumFields() {
        // Simulazione di un forum con campi mancanti
        Forum forum = new Forum(null, "", "", null, "");

        // Configurazione del mock per restituire un errore generico
        doThrow(new IllegalArgumentException("Campi del forum non validi"))
                .when(forumService).addForum(forum, true);

        // Esecuzione del metodo
        ResponseEntity<Forum> response = forumController.addForum(forum, true);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(forumService, times(1)).addForum(forum, true);
    }

    /**
     * Test per `updateForum` con ID valido, utente amministratore, e aggiornamento corretto.
     */
    @Test
    void updateForum_ShouldReturnUpdatedForumForValidIdAndAdminRole() {
        String idForum = "validId";
        boolean ruolo = true;
        Forum updatedForum = new Forum("validId", "Updated Title", "Updated Description", null, "Italy");
        Forum resultForum = new Forum("validId", "Updated Title", "Updated Description", null, "Italy");

        when(forumService.updateForum(idForum, updatedForum, ruolo)).thenReturn(resultForum);

        ResponseEntity<Forum> response = forumController.updateForum(idForum, updatedForum, ruolo);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(resultForum, response.getBody());
        verify(forumService, times(1)).updateForum(idForum, updatedForum, ruolo);
    }

    /**
     * Test per `updateForum` con ID inesistente.
     */
    @Test
    void updateForum_ShouldReturnNotFoundForNonexistentId() {
        String idForum = "nonexistentId";
        boolean ruolo = true;
        Forum updatedForum = new Forum("nonexistentId", "Updated Title", "Updated Description", null, "Italy");

        when(forumService.updateForum(idForum, updatedForum, ruolo)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Forum> response = forumController.updateForum(idForum, updatedForum, ruolo);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(forumService, times(1)).updateForum(idForum, updatedForum, ruolo);
    }

    /**
     * Test per `updateForum` con ruolo non autorizzato.
     */
    @Test
    void updateForum_ShouldReturnForbiddenForNonAdminRole() {
        String idForum = "validId";
        boolean ruolo = false; // Non amministratore
        Forum updatedForum = new Forum("validId", "Updated Title", "Updated Description", null, "Italy");

        when(forumService.updateForum(idForum, updatedForum, ruolo)).thenThrow(new SecurityException());

        ResponseEntity<Forum> response = forumController.updateForum(idForum, updatedForum, ruolo);

        assertNotNull(response);
        assertEquals(403, response.getStatusCode().value());
        verify(forumService, times(1)).updateForum(idForum, updatedForum, ruolo);
    }

    /**
     * Test per `updateForum` con ID nullo.
     */
    @Test
    void updateForum_ShouldThrowExceptionForNullId() {
        String idForum = null;
        boolean ruolo = true;
        Forum updatedForum = new Forum("validId", "Updated Title", "Updated Description", null, "Italy");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> forumController.updateForum(idForum, updatedForum, ruolo));

        assertEquals("ID forum non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(forumService);
    }


    /**
     * Test per `updateForum` con campi mancanti.
     */
    @Test
    void updateForum_ShouldThrowExceptionForInvalidFields() {
        String idForum = "validId";
        boolean ruolo = true;
        Forum updatedForum = new Forum(null, null, null, null, "Italy"); // Campi mancanti

        when(forumService.updateForum(idForum, updatedForum, ruolo)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Forum> response = forumController.updateForum(idForum, updatedForum, ruolo);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(forumService, times(1)).updateForum(idForum, updatedForum, ruolo);
    }
    /**
     * Test per `deleteForumAlternative` con ID valido e ruolo amministratore.
     */
    @Test
    void deleteForumAlternative_ShouldReturnNoContentForValidIdAndAdminRole() {
        String idForum = "validId";
        boolean ruolo = true;

        // Configurazione del mock
        doNothing().when(forumService).deleteForum(idForum, ruolo);

        // Esecuzione del metodo
        ResponseEntity<Void> response = forumController.deleteForumAlternative(idForum, ruolo);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(forumService, times(1)).deleteForum(idForum, ruolo);
    }

    /**
     * Test per `deleteForumAlternative` con ruolo non amministratore.
     */
    @Test
    void deleteForumAlternative_ShouldReturnForbiddenForNonAdminRole() {
        String idForum = "validId";
        boolean ruolo = false;

        // Configurazione del mock
        doThrow(new SecurityException()).when(forumService).deleteForum(idForum, ruolo);

        // Esecuzione del metodo
        ResponseEntity<Void> response = forumController.deleteForumAlternative(idForum, ruolo);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(403, response.getStatusCode().value());
        verify(forumService, times(1)).deleteForum(idForum, ruolo);
    }

    /**
     * Test per `deleteForumAlternative` con ID nullo.
     */
    @Test
    void deleteForumAlternative_ShouldThrowExceptionForNullId() {
        String idForum = null;
        boolean ruolo = true;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> forumController.deleteForumAlternative(idForum, ruolo));

        assertEquals("ID forum non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(forumService);
    }


    /**
     * Test per `deleteForumAlternative` con ID inesistente.
     */
    @Test
    void deleteForumAlternative_ShouldHandleNotFoundForNonexistentId() {
        String idForum = "nonexistentId";
        boolean ruolo = true;

        // Configurazione del mock per non generare un'eccezione
        doNothing().when(forumService).deleteForum(idForum, ruolo);

        // Esecuzione del metodo
        ResponseEntity<Void> response = forumController.deleteForumAlternative(idForum, ruolo);

        // Verifica degli esiti
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(forumService, times(1)).deleteForum(idForum, ruolo);
    }

    /**
     * Test per `deleteForumAlternative` con errore generico durante l'eliminazione.
     */
    @Test
    void deleteForumAlternative_ShouldHandleGenericError() {
        String idForum = "validId";
        boolean ruolo = true;

        // Configurazione del mock per simulare un'eccezione generica
        doThrow(new RuntimeException()).when(forumService).deleteForum(idForum, ruolo);

        // Esecuzione del metodo
        assertThrows(RuntimeException.class, () -> forumController.deleteForumAlternative(idForum, ruolo));
        verify(forumService, times(1)).deleteForum(idForum, ruolo);
    }

    /**
     * Test per `getForumsByPaese` con paese valido e forum associati.
     */
    @Test
    void getForumsByPaese_ShouldReturnListOfForumsForValidCountry() {
        String paese = "Italia";
        List<Forum> forums = new ArrayList<>();
        forums.add(new Forum("1", "Forum 1", "Descrizione 1", new ArrayList<>(), "Italia"));
        forums.add(new Forum("2", "Forum 2", "Descrizione 2", new ArrayList<>(), "Italia"));

        when(forumService.findForumsByPaese(paese)).thenReturn(forums);

        ResponseEntity<List<Forum>> response = forumController.getForumsByPaese(paese);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(forums, response.getBody());
        verify(forumService, times(1)).findForumsByPaese(paese);
    }

    /**
     * Test per `getForumsByPaese` con paese valido ma nessun forum associato.
     */
    @Test
    void getForumsByPaese_ShouldReturnNotFoundForValidCountryWithoutForums() {
        String paese = "Francia";
        List<Forum> forums = new ArrayList<>();

        when(forumService.findForumsByPaese(paese)).thenReturn(forums);

        ResponseEntity<List<Forum>> response = forumController.getForumsByPaese(paese);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(forumService, times(1)).findForumsByPaese(paese);
    }

    /**
     * Test per `getForumsByPaese` con paese nullo.
     */
    @Test
    void getForumsByPaese_ShouldThrowExceptionForNullCountry() {
        String paese = null;

        assertThrows(IllegalArgumentException.class, () -> forumController.getForumsByPaese(paese));
        verifyNoInteractions(forumService);
    }

    /**
     * Test per `getForumsByPaese` con paese vuoto.
     */
    @Test
    void getForumsByPaese_ShouldThrowExceptionForEmptyCountry() {
        String paese = "";

        assertThrows(IllegalArgumentException.class, () -> forumController.getForumsByPaese(paese));
        verifyNoInteractions(forumService);
    }

}
