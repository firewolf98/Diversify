package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Service.CommentoService;
import it.unisa.diversifybe.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per `addLikeToPost` con ID post valido.
     */
    @Test
    void addLikeToPost_ShouldAddLikeForValidId() {
        String postId = "validPostId";
        Post post = new Post(postId, "Titolo", "Autore1", "Contenuto", new Date(), 10, null, "Forum1");

        when(postService.findById(postId)).thenReturn(Optional.of(post));

        ResponseEntity<?> response = likeController.addLikeToPost(postId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like aggiunto al post con successo", response.getBody());
        assertEquals(11, post.getLike());

        verify(postService, times(1)).findById(postId);
        verify(postService, times(1)).save(post);
    }

    /**
     * Test per `addLikeToPost` con ID post inesistente.
     */
    @Test
    void addLikeToPost_ShouldReturnNotFoundForNonExistentId() {
        String postId = "nonExistentPostId";

        when(postService.findById(postId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = likeController.addLikeToPost(postId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Post non trovato", response.getBody());

        verify(postService, times(1)).findById(postId);
        verify(postService, never()).save(any());
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
        String commentoId = "validCommentoId";
        Commento commento = new Commento("autoreId", commentoId, "Contenuto del commento", new Date(), 5, null, "postId");

        when(commentoService.findById(commentoId)).thenReturn(Optional.of(commento));

        ResponseEntity<?> response = likeController.addLikeToCommento(commentoId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Like aggiunto al commento con successo", response.getBody());
        assertEquals(6, commento.getLike());

        verify(commentoService, times(1)).findById(commentoId);
        verify(commentoService, times(1)).save(commento);
    }

    /**
     * Test per `addLikeToCommento` con ID commento inesistente.
     */
    @Test
    void addLikeToCommento_ShouldReturnNotFoundForNonExistentId() {
        String commentoId = "nonExistentCommentoId";

        when(commentoService.findById(commentoId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = likeController.addLikeToCommento(commentoId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Commento non trovato", response.getBody());

        verify(commentoService, times(1)).findById(commentoId);
        verify(commentoService, never()).save(any());
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
