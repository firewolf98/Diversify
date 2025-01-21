package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Repository.CommentoRepository;
import it.unisa.diversifybe.Repository.PostRepository;
import it.unisa.diversifybe.Repository.SubcommentoRepository;
import it.unisa.diversifybe.Repository.ForumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentoServiceTest {

    /**
     * **Partizioni Identificate per i Metodi del CommentoService**
     * **Metodo `aggiungiCommentoAPost`**
     * - **ID Post**:
     *   - Valido (esiste nel database).
     *   - Non valido:
     *     - Null.
     *     - Vuoto.
     *     - Non esistente nel database.
     * - **Commento**:
     *   - Valido (tutti i campi obbligatori presenti e corretti).
     *   - Non valido:
     *     - Null.
     *     - Campi mancanti (e.g., contenuto, autore).
     *     - Campi non validi (e.g., contenuto vuoto).
     * **Metodo `aggiungiSubcommentoACommento`**
     * - **ID Commento**:
     *   - Valido (esiste nel database).
     *   - Non valido:
     *     - Null.
     *     - Vuoto.
     *     - Non esistente nel database.
     * - **Subcommento**:
     *   - Valido (tutti i campi obbligatori presenti e corretti).
     *   - Non valido:
     *     - Null.
     *     - Campi mancanti (e.g., contenuto, autore).
     *     - Campi non validi (e.g., contenuto vuoto).
     * **Metodo `trovaCommentiPerPost`**
     * - **ID Post**:
     *   - Valido (esiste nel database).
     *     - Con commenti associati.
     *     - Senza commenti associati.
     *   - Non valido:
     *     - Null.
     *     - Vuoto.
     *     - Non esistente nel database.
     * **Metodo `findById`**
     * - **ID Commento**:
     *   - Valido (esiste nel database).
     *   - Non valido:
     *     - Null.
     *     - Vuoto.
     *     - Non esistente nel database.
     * **Metodo `save`**
     * - **Commento**:
     *   - Valido (tutti i campi obbligatori presenti e corretti).
     *   - Non valido:
     *     - Null.
     *     - Campi mancanti o non validi (e.g., contenuto vuoto, autore null).
     * **Metodo `trovaSubcommentiPerCommento`**
     * - **ID Commento**:
     *   - Valido (esiste nel database).
     *     - Con subcommenti associati.
     *     - Senza subcommenti associati.
     *   - Non valido:
     *     - Null.
     *     - Vuoto.
     *     - Non esistente nel database.
     */


    @InjectMocks
    private CommentoService service;

    @Mock
    private CommentoRepository commentoRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SubcommentoRepository subcommentoRepository;

    @Mock
    private ForumRepository forumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per aggiungiCommentoAPost
     */
    @Test
    void aggiungiCommentoAPost_ShouldAddCommentoToPost() {
        // Inizializza i mock manualmente
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            // Creazione dei dati di test
            String idPost = "1";
            Post post = new Post(idPost, "Titolo", "1", "Contenuto", new Date(), 0, new ArrayList<>(), "forum1");
            Commento commento = new Commento("1", null, "Testo commento", new Date(), 0, null, idPost);

            // Mock del repository
            Forum forum = new Forum();
            forum.setPost(new ArrayList<>());
            forum.getPost().add(post);

            // Mock del comportamento del repository
            when(forumRepository.findAll()).thenReturn(Collections.singletonList(forum));  // Mock del repository Forum
            when(postRepository.findById(idPost)).thenReturn(Optional.of(post));
            when(postRepository.save(post)).thenReturn(post);  // Mock per postRepository.save()
            when(forumRepository.save(forum)).thenReturn(forum);  // Mock per forumRepository.save()

            // Esegui il metodo da testare
            Commento result = service.aggiungiCommentoAPost(idPost, commento);

            // Verifica i risultati
            assertNotNull(result);
            assertEquals("Testo commento", result.getContenuto());
            assertEquals(1, post.getCommenti().size());  // Verifica che il commento sia stato aggiunto

            // Verifica che forumRepository.save sia stato invocato
            verify(forumRepository, times(1)).save(forum);
        } catch (Exception e) {
            fail("Errore durante l'inizializzazione dei mock: " + e.getMessage());
        }
    }


    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForInvalidIdPost() {
        String invalidIdPost = null;
        Commento commento = new Commento("1", null, "Testo commento", new Date(), 0, null, "1");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiCommentoAPost(invalidIdPost, commento));

        assertEquals("L'ID del post non può essere nullo o vuoto.", exception.getMessage());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForCommentoNull() {
        String idPost = "1";
        Commento invalidCommento = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiCommentoAPost(idPost, invalidCommento));

        assertEquals("Il commento non può essere nullo.", exception.getMessage());
        verify(commentoRepository, never()).save(any(Commento.class));
    }

    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForMissingFieldsInCommento() {
        String idPost = "1";
        Post post = new Post(idPost, "Titolo", "1", "Contenuto", new Date(), 0, new ArrayList<>(), "forum1");
        Commento invalidCommento = new Commento(null, null, "", new Date(), 0, null, idPost);

        when(postRepository.findById(idPost)).thenReturn(Optional.of(post));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiCommentoAPost(idPost, invalidCommento));

        assertEquals("Il commento deve avere un autore e un contenuto valido.", exception.getMessage());
        verify(commentoRepository, never()).save(any(Commento.class));
    }

    /**
     * Test per aggiungiSubcommentoACommento
     */
    @Test
    void aggiungiSubcommentoACommento_ShouldAddSubcommentoToCommento() {
        // Arrange
        String idCommento = "123";
        Subcommento subcommento = new Subcommento();
        subcommento.setIdAutore("autore1");
        subcommento.setContenuto("Questo è un subcommento.");

        Forum forum = new Forum();
        Post post = new Post();
        Commento commento = new Commento();
        commento.setIdCommento(idCommento);
        commento.setSubcommenti(new ArrayList<>());

        post.setCommenti(List.of(commento));
        forum.setPost(List.of(post));
        when(forumRepository.findAll()).thenReturn(List.of(forum));

        // Act
        Subcommento result = service.aggiungiSubcommentoACommento(idCommento, subcommento);

        // Assert
        assertNotNull(result);
        assertEquals("autore1", result.getIdAutore());
        assertEquals("Questo è un subcommento.", result.getContenuto());
        assertNotNull(result.getDataCreazione());
        assertEquals(1, commento.getSubcommenti().size());
        assertEquals(subcommento, commento.getSubcommenti().get(0));
        verify(forumRepository, times(1)).save(forum);
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForInvalidIdCommento() {
        String invalidIdCommento = null;
        Subcommento subcommento = new Subcommento("2", "Testo subcommento", new Date());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiSubcommentoACommento(invalidIdCommento, subcommento));

        assertEquals("L'ID del commento non può essere nullo o vuoto.", exception.getMessage());
        verify(subcommentoRepository, never()).save(any(Subcommento.class));
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForSubcommentoNull() {
        String idCommento = "1";
        Subcommento invalidSubcommento = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiSubcommentoACommento(idCommento, invalidSubcommento));

        assertEquals("Il subcommento non può essere nullo.", exception.getMessage());
        verify(subcommentoRepository, never()).save(any(Subcommento.class));
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForMissingFieldsInSubcommento() {
        String idCommento = "1";
        Commento commento = new Commento("1", idCommento, "Testo commento", new Date(), 0, new ArrayList<>(), "1");
        Subcommento invalidSubcommento = new Subcommento(null, "", null);

        when(commentoRepository.findById(idCommento)).thenReturn(Optional.of(commento));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiSubcommentoACommento(idCommento, invalidSubcommento));

        assertEquals("Il subcommento deve avere un autore e un contenuto valido.", exception.getMessage());
        verify(subcommentoRepository, never()).save(any(Subcommento.class));
    }
}
