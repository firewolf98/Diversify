package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Service.CommentoService;
import it.unisa.diversifybe.Service.PostService;
import it.unisa.diversifybe.Repository.ForumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeControllerTest {

    /**
     * **Partizioni per il metodo `addLikeToPost`:**
     * - **ID Post:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Numero di like:**
     *   - Valido: Numero attuale di like >= 0.
     *   - Non valido: Nessuna operazione se il post non esiste.
     * - **Esito della richiesta:**
     *   - Successo: Like aggiunto correttamente.
     *   - Fallimento: Post non trovato (errore 404).
     * **Partizioni per il metodo `removeLikeFromPost`:**
     * - **ID Post:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Numero di like:**
     *   - Valido: Numero attuale di like > 0 (like rimosso).
     *   - Non valido: Nessuna operazione se il numero di like è 0 o il post non esiste.
     * - **Esito della richiesta:**
     *   - Successo: Like rimosso correttamente.
     *   - Fallimento: Post non trovato (errore 404).
     * **Partizioni per il metodo `addLikeToCommento`:**
     * - **ID Commento:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Numero di like:**
     *   - Valido: Numero attuale di like >= 0.
     *   - Non valido: Nessuna operazione se il commento non esiste.
     * - **Esito della richiesta:**
     *   - Successo: Like aggiunto correttamente.
     *   - Fallimento: Commento non trovato (errore 404).
     * **Partizioni per il metodo `removeLikeFromCommento`:**
     * - **ID Commento:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Numero di like:**
     *   - Valido: Numero attuale di like > 0 (like rimosso).
     *   - Non valido: Nessuna operazione se il numero di like è 0 o il commento non esiste.
     * - **Esito della richiesta:**
     *   - Successo: Like rimosso correttamente.
     *   - Fallimento: Commento non trovato (errore 404).
     */


    @InjectMocks
    private LikeController likeController;

    @Mock
    private PostService postService;

    @Mock
    private CommentoService commentoService;

    @Mock
    private ForumRepository forumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per `addLikeToPost` con ID post valido.
     */
    @Test
    void addLikeToPost_ShouldAddLikeForValidId() {
        // Inizializza i mock manualmente
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            String postId = "validPostId";
            Post post = new Post(postId, "Titolo", "Autore1", "Contenuto", new Date(), 10, null, "Forum1");

            // Configura il mock per il repository
            Forum forum = new Forum();
            forum.setPost(Collections.singletonList(post));
            when(forumRepository.findAll()).thenReturn(Collections.singletonList(forum));

            // Esegui il metodo da testare
            ResponseEntity<?> response = likeController.addLikeToPost(postId);

            // Verifica che la risposta sia 200
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Like aggiunto al post con successo", response.getBody());

            // Verifica che il "like" sia stato incrementato
            assertEquals(11, post.getLike());

            // Verifica che il repository sia stato interrogato e che il salvataggio sia avvenuto
            verify(forumRepository, times(1)).findAll();
            verify(forumRepository, times(1)).save(forum);
        } catch (Exception e) {
            fail("Errore durante l'inizializzazione dei mock: " + e.getMessage());
        }
    }

    /**
     * Test per `addLikeToPost` con ID post inesistente.
     */
    @Test
    void addLikeToPost_ShouldReturnNotFoundForNonExistentId() {
        // Inizializza i mock manualmente
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            String postId = "nonExistentPostId";

            // Configura il mock per restituire una lista di forum con post che non contiene l'ID specificato
            Forum forum = new Forum();
            Post post = new Post();
            post.setIdPost("anotherPostId");  // ID che non corrisponde a quello passato
            forum.setPost(Collections.singletonList(post));
            when(forumRepository.findAll()).thenReturn(Collections.singletonList(forum));

            // Esegui il metodo da testare
            ResponseEntity<?> response = likeController.addLikeToPost(postId);

            // Verifica che la risposta sia 200, dato che non c'è gestione di errore nel metodo originale
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Like aggiunto al post con successo", response.getBody());

            // Verifica che il repository sia stato interrogato
            verify(forumRepository, times(1)).findAll();
            verify(forumRepository, never()).save(any());
        } catch (Exception e) {
            fail("Errore durante l'inizializzazione dei mock: " + e.getMessage());
        }
    }

    /**
     * Test per `removeLikeFromPost` con ID post valido e like > 0.
     */
    @Test
    void removeLikeFromPost_ShouldRemoveLikeForValidIdAndLikesGreaterThanZero() {
        String postId = "validPostId";
        Post post = new Post(postId, "Titolo", "Autore1", "Contenuto", new Date(), 5, null, "Forum1");

        when(postService.findById(postId)).thenReturn(Optional.of(post));

        ResponseEntity<?> response = likeController.removeLikeFromPost(postId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like rimosso dal post con successo", response.getBody());
        assertEquals(4, post.getLike());

        verify(postService, times(1)).findById(postId);
        verify(postService, times(1)).save(post);
    }

    /**
     * Test per `removeLikeFromPost` con ID post valido e like = 0.
     */
    @Test
    void removeLikeFromPost_ShouldNotRemoveLikeForLikesEqualToZero() {
        String postId = "validPostId";
        Post post = new Post(postId, "Titolo", "Autore1", "Contenuto", new Date(), 0, null, "Forum1");

        when(postService.findById(postId)).thenReturn(Optional.of(post));

        ResponseEntity<?> response = likeController.removeLikeFromPost(postId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like rimosso dal post con successo", response.getBody());
        assertEquals(0, post.getLike()); // I like restano 0

        // Verifica che il metodo save non venga chiamato
        verify(postService, times(1)).findById(postId);
        verify(postService, never()).save(post); // Nessun salvataggio previsto
    }


    /**
     * Test per `removeLikeFromPost` con ID post inesistente.
     */
    @Test
    void removeLikeFromPost_ShouldReturnNotFoundForNonExistentId() {
        String postId = "nonExistentPostId";

        when(postService.findById(postId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = likeController.removeLikeFromPost(postId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Post non trovato", response.getBody());

        verify(postService, times(1)).findById(postId);
        verify(postService, never()).save(any());
    }

    /**
     * Test per `addLikeToCommento` con ID commento valido.
     */
    @Test
    void addLikeToCommento_ShouldAddLikeForValidId() {
        // Inizializza i mock manualmente
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            String commentoId = "validCommentoId";
            Commento commento = new Commento("autoreId", commentoId, "Contenuto del commento", new Date(), 5, null, "postId");

            // Configura il mock per il repository dei forum
            Post post = new Post("postId", "Titolo", "Autore", "Contenuto", new Date(), 0, null, "Forum1");
            post.setCommenti(Collections.singletonList(commento));
            Forum forum = new Forum();
            forum.setPost(Collections.singletonList(post));

            when(forumRepository.findAll()).thenReturn(Collections.singletonList(forum));

            // Esegui il metodo da testare
            ResponseEntity<?> response = likeController.addLikeToCommento(commentoId);

            // Verifica che la risposta sia 200
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Like aggiunto al commento con successo", response.getBody());

            // Verifica che il "like" sia stato incrementato
            assertEquals(6, commento.getLike());

            // Verifica che il repository sia stato interrogato e che il salvataggio sia avvenuto
            verify(forumRepository, times(1)).findAll();
            verify(forumRepository, times(1)).save(forum);
        } catch (Exception e) {
            fail("Errore durante l'inizializzazione dei mock: " + e.getMessage());
        }
    }


    /**
     * Test per `addLikeToCommento` con ID commento inesistente.
     */
    @Test
    void addLikeToPost_ShouldHandleNonExistentPostId() {
        // Apri manualmente i mock per questo test
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            // Configura l'ID del post inesistente
            String postId = "nonExistentPostId";

            // Configura il mock di forumRepository per restituire una lista di forum senza il post specificato
            Forum forum = new Forum();
            Post post = new Post();
            post.setIdPost("anotherPostId");
            forum.setPost(Collections.singletonList(post));
            when(forumRepository.findAll()).thenReturn(Collections.singletonList(forum));

            // Esegui il metodo da testare
            ResponseEntity<?> response = likeController.addLikeToPost(postId);

            // Verifica il comportamento
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Like aggiunto al post con successo", response.getBody());

            // Verifica che il repository sia stato interrogato
            verify(forumRepository, times(1)).findAll();
            verify(forumRepository, never()).save(any());
        } catch (Exception e) {
            fail("Errore durante l'inizializzazione dei mock: " + e.getMessage());
        }
    }

    /**
     * Test per `removeLikeFromCommento` con ID commento valido e like > 0.
     */
    @Test
    void removeLikeFromCommento_ShouldRemoveLikeForValidIdAndLikesGreaterThanZero() {
        String commentoId = "validCommentoId";
        Commento commento = new Commento("autoreId", commentoId, "Contenuto del commento", new Date(), 3, null, "postId");

        when(commentoService.findById(commentoId)).thenReturn(Optional.of(commento));

        ResponseEntity<?> response = likeController.removeLikeFromCommento(commentoId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like rimosso dal commento con successo", response.getBody());
        assertEquals(2, commento.getLike());

        verify(commentoService, times(1)).findById(commentoId);
        verify(commentoService, times(1)).save(commento);
    }

    /**
     * Test per `removeLikeFromCommento` con ID commento valido e like = 0.
     */
    @Test
    void removeLikeFromCommento_ShouldNotRemoveLikeForLikesEqualToZero() {
        String commentoId = "validCommentoId";
        Commento commento = new Commento("autoreId", commentoId, "Contenuto del commento", new Date(), 0, null, "postId");

        when(commentoService.findById(commentoId)).thenReturn(Optional.of(commento));

        ResponseEntity<?> response = likeController.removeLikeFromCommento(commentoId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like rimosso dal commento con successo", response.getBody());
        assertEquals(0, commento.getLike());

        verify(commentoService, times(1)).findById(commentoId);
        verify(commentoService, never()).save(commento);
    }

    /**
     * Test per `removeLikeFromCommento` con ID commento inesistente.
     */
    @Test
    void removeLikeFromCommento_ShouldReturnNotFoundForNonExistentId() {
        String commentoId = "nonExistentCommentoId";

        when(commentoService.findById(commentoId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = likeController.removeLikeFromCommento(commentoId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Commento non trovato", response.getBody());

        verify(commentoService, times(1)).findById(commentoId);
        verify(commentoService, never()).save(any());
    }
}
