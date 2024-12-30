package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Repository.ForumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForumServiceTest {

    /**
     * **Partizioni dei metodi del servizio ForumService**
     * **Metodo `getAllForums`:**
     * - **Esito della richiesta:**
     *   - Successo (lista di forum restituita).
     *   - Fallimento (nessun forum trovato, lista vuota).
     * **Metodo `getForumById`:**
     * - **ID Forum:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Esito della ricerca:**
     *   - Forum trovato (Optional contenente il forum).
     *   - Forum non trovato (Optional vuoto).
     * **Metodo `addForum`:**
     * - **Ruolo dell'utente:**
     *   - Amministratore (autorizzato).
     *   - Non amministratore (non autorizzato).
     * - **Forum da aggiungere:**
     *   - Valido: Tutti i campi richiesti presenti e validi.
     *   - Non valido: Forum mancante, campi mancanti o non validi.
     * - **Esito della richiesta:**
     *   - Successo (forum aggiunto).
     *   - Fallimento:
     *     - Non autorizzato (SecurityException).
     *     - Campi non validi (errore generico).
     * **Metodo `updateForum`:**
     * - **ID Forum:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Ruolo dell'utente:**
     *   - Amministratore (autorizzato).
     *   - Non amministratore (non autorizzato).
     * - **Forum aggiornato:**
     *   - Valido: Campi aggiornati correttamente.
     *   - Non valido: Campi mancanti o non validi.
     * - **Esito della richiesta:**
     *   - Successo (forum aggiornato).
     *   - Fallimento:
     *     - Non autorizzato (SecurityException).
     *     - Forum non trovato (IllegalArgumentException).
     * **Metodo `deleteForum`:**
     * - **ID Forum:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Ruolo dell'utente:**
     *   - Amministratore (autorizzato).
     *   - Non amministratore (non autorizzato).
     * - **Esito della richiesta:**
     *   - Successo (forum eliminato).
     *   - Fallimento:
     *     - Non autorizzato (SecurityException).
     *     - Forum inesistente (errore generico).
     * **Metodo `findForumsByPaese`:**
     * - **Paese:**
     *   - Valido: Esistono forum associati al paese.
     *   - Non valido:
     *     - Null o vuoto.
     *     - Nessun forum associato al paese.
     * - **Esito della richiesta:**
     *   - Successo (lista di forum restituita).
     *   - Fallimento:
     *     - Nessun forum trovato (lista vuota).
     */


    @InjectMocks
    private ForumService forumService;

    @Mock
    private ForumRepository forumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per `getAllForums` con lista di forum valida.
     */
    @Test
    void getAllForums_ShouldReturnListOfForums() {
        // Simulazione di una lista di forum
        List<Forum> forums = new ArrayList<>();
        forums.add(new Forum("1", "Forum 1", "Descrizione 1", new ArrayList<>(), "Italia"));
        forums.add(new Forum("2", "Forum 2", "Descrizione 2", new ArrayList<>(), "Spagna"));

        // Configurazione del mock
        when(forumRepository.findAll()).thenReturn(forums);

        // Esecuzione del metodo
        List<Forum> result = forumService.getAllForums();

        // Verifica degli esiti
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(forums, result);

        verify(forumRepository, times(1)).findAll();
    }

    /**
     * Test per `getAllForums` con lista vuota.
     */
    @Test
    void getAllForums_ShouldReturnEmptyList() {
        // Simulazione di una lista vuota
        List<Forum> forums = new ArrayList<>();

        // Configurazione del mock
        when(forumRepository.findAll()).thenReturn(forums);

        // Esecuzione del metodo
        List<Forum> result = forumService.getAllForums();

        // Verifica degli esiti
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(forumRepository, times(1)).findAll();
    }

    /**
     * Test per `getForumById` con ID valido.
     */
    @Test
    void getForumById_ShouldReturnForumForValidId() {
        String idForum = "1";
        Forum forum = new Forum(idForum, "Forum 1", "Descrizione 1", new ArrayList<>(), "Italia");

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.of(forum));

        // Esecuzione del metodo
        Optional<Forum> result = forumService.getForumById(idForum);

        // Verifica degli esiti
        assertTrue(result.isPresent());
        assertEquals(forum, result.get());

        verify(forumRepository, times(1)).findById(idForum);
    }

    /**
     * Test per `getForumById` con ID inesistente.
     */
    @Test
    void getForumById_ShouldReturnEmptyForNonExistentId() {
        String idForum = "99";

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Esecuzione del metodo
        Optional<Forum> result = forumService.getForumById(idForum);

        // Verifica degli esiti
        assertFalse(result.isPresent());

        verify(forumRepository, times(1)).findById(idForum);
    }

    /**
     * Test per `getForumById` con ID nullo.
     */
    @Test
    void getForumById_ShouldReturnEmptyForNullId() {
        String idForum = null;

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Esecuzione del metodo
        Optional<Forum> result = forumService.getForumById(idForum);

        // Verifica degli esiti
        assertFalse(result.isPresent());

        verify(forumRepository, times(1)).findById(idForum);
    }

    /**
     * Test per `getForumById` con ID vuoto.
     */
    @Test
    void getForumById_ShouldReturnEmptyForEmptyId() {
        String idForum = "";

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Esecuzione del metodo
        Optional<Forum> result = forumService.getForumById(idForum);

        // Verifica degli esiti
        assertFalse(result.isPresent());

        verify(forumRepository, times(1)).findById(idForum);
    }

    /**
     * Test per `addForum` con ruolo amministratore e forum valido.
     */
    @Test
    void addForum_ShouldAddForumForAdmin() {
        Forum forum = new Forum("1", "Forum 1", "Descrizione", new ArrayList<>(), "Italia");

        // Configurazione del mock
        when(forumRepository.save(forum)).thenReturn(forum);

        // Esecuzione del metodo
        Forum result = forumService.addForum(forum, true);

        // Verifica degli esiti
        assertNotNull(result);
        assertEquals(forum, result);

        verify(forumRepository, times(1)).save(forum);
    }

    /**
     * Test per `addForum` con ruolo non amministratore.
     */
    @Test
    void addForum_ShouldThrowSecurityExceptionForNonAdmin() {
        Forum forum = new Forum("1", "Forum 1", "Descrizione", new ArrayList<>(), "Italia");

        SecurityException exception = assertThrows(SecurityException.class, () -> forumService.addForum(forum, false));

        assertEquals("Solo gli amministratori possono aggiungere un forum.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `addForum` con forum nullo.
     */
    @Test
    void addForum_ShouldThrowNullPointerExceptionForNullForum() {
        assertThrows(NullPointerException.class, () -> forumService.addForum(null, true));
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `updateForum` con ID valido, ruolo amministratore e forum aggiornato valido.
     */
    @Test
    void updateForum_ShouldUpdateForumForValidIdAndAdmin() {
        String idForum = "1";
        Forum existingForum = new Forum(idForum, "Forum 1", "Descrizione", new ArrayList<>(), "Italia");
        Forum updatedForum = new Forum(idForum, "Updated Forum", "Updated Description", new ArrayList<>(), "Italia");

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.of(existingForum));
        when(forumRepository.save(existingForum)).thenReturn(updatedForum);

        // Esecuzione del metodo
        Forum result = forumService.updateForum(idForum, updatedForum, true);

        // Verifica degli esiti
        assertNotNull(result);
        assertEquals(updatedForum.getTitolo(), result.getTitolo());
        assertEquals(updatedForum.getDescrizione(), result.getDescrizione());

        verify(forumRepository, times(1)).findById(idForum);
        verify(forumRepository, times(1)).save(existingForum);
    }

    /**
     * Test per `updateForum` con ID inesistente.
     */
    @Test
    void updateForum_ShouldThrowIllegalArgumentExceptionForNonExistentId() {
        String idForum = "99";
        Forum updatedForum = new Forum(idForum, "Updated Forum", "Updated Description", new ArrayList<>(), "Italia");

        // Configurazione del mock
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> forumService.updateForum(idForum, updatedForum, true));

        assertEquals("Forum non trovato con ID: " + idForum, exception.getMessage());
        verify(forumRepository, times(1)).findById(idForum);
    }

    /**
     * Test per `updateForum` con ruolo non amministratore.
     */
    @Test
    void updateForum_ShouldThrowSecurityExceptionForNonAdmin() {
        String idForum = "1";
        Forum updatedForum = new Forum(idForum, "Updated Forum", "Updated Description", new ArrayList<>(), "Italia");

        SecurityException exception = assertThrows(SecurityException.class,
                () -> forumService.updateForum(idForum, updatedForum, false));

        assertEquals("Solo gli amministratori possono modificare un forum.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `updateForum` con ID nullo.
     */
    @Test
    void updateForum_ShouldThrowIllegalArgumentExceptionForNullId() {
        Forum updatedForum = new Forum("1", "Updated Forum", "Updated Description", new ArrayList<>(), "Italia");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> forumService.updateForum(null, updatedForum, true));

        assertEquals("Forum non trovato con ID: null", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `deleteForum` con ID valido e ruolo amministratore.
     */
    @Test
    void deleteForum_ShouldSucceedForValidIdAndAdminRole() {
        String idForum = "validId";
        boolean ruolo = true;

        // Esecuzione del metodo
        assertDoesNotThrow(() -> forumService.deleteForum(idForum, ruolo));

        // Verifica interazioni
        verify(forumRepository, times(1)).deleteById(idForum);
    }

    /**
     * Test per `deleteForum` con ruolo non amministratore.
     */
    @Test
    void deleteForum_ShouldThrowSecurityExceptionForNonAdminRole() {
        String idForum = "validId";
        boolean ruolo = false;

        SecurityException exception = assertThrows(SecurityException.class,
                () -> forumService.deleteForum(idForum, ruolo));

        assertEquals("Solo gli amministratori possono eliminare un forum.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `deleteForum` con ID nullo.
     */
    @Test
    void deleteForum_ShouldThrowIllegalArgumentExceptionForNullId() {
        String idForum = null;
        boolean ruolo = true;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> forumService.deleteForum(idForum, ruolo));

        assertEquals("Il parametro 'idForum' non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }


    /**
     * Test per `deleteForum` con ID inesistente (simulato come fallimento silenzioso).
     */
    @Test
    void deleteForum_ShouldNotThrowForNonExistentId() {
        String idForum = "nonExistentId";
        boolean ruolo = true;

        // Nessuna eccezione viene sollevata per ID inesistente
        assertDoesNotThrow(() -> forumService.deleteForum(idForum, ruolo));

        // Verifica che il repository sia stato chiamato
        verify(forumRepository, times(1)).deleteById(idForum);
    }

    /**
     * Test per `findForumsByPaese` con paese valido.
     */
    @Test
    void findForumsByPaese_ShouldReturnForumsForValidPaese() {
        String paese = "Italia";
        List<Forum> forums = List.of(
                new Forum("1", "Forum 1", "Descrizione 1", new ArrayList<>(), paese),
                new Forum("2", "Forum 2", "Descrizione 2", new ArrayList<>(), paese)
        );

        when(forumRepository.findByPaese(paese)).thenReturn(forums);

        List<Forum> result = forumService.findForumsByPaese(paese);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(forums, result);

        verify(forumRepository, times(1)).findByPaese(paese);
    }

    /**
     * Test per `findForumsByPaese` con paese nullo.
     */
    @Test
    void findForumsByPaese_ShouldThrowIllegalArgumentExceptionForNullPaese() {
        String paese = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> forumService.findForumsByPaese(paese));

        assertEquals("Il parametro 'paese' non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }

    /**
     * Test per `findForumsByPaese` con paese vuoto.
     */
    @Test
    void findForumsByPaese_ShouldThrowIllegalArgumentExceptionForEmptyPaese() {
        String paese = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> forumService.findForumsByPaese(paese));

        assertEquals("Il parametro 'paese' non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(forumRepository);
    }


    /**
     * Test per `findForumsByPaese` con paese valido ma nessun forum trovato.
     */
    @Test
    void findForumsByPaese_ShouldReturnEmptyListForNoForumsFound() {
        String paese = "NonEsistente";
        List<Forum> forums = new ArrayList<>();

        when(forumRepository.findByPaese(paese)).thenReturn(forums);

        List<Forum> result = forumService.findForumsByPaese(paese);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(forumRepository, times(1)).findByPaese(paese);
    }
}
