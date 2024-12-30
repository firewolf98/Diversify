package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Service.CommentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test per CommentoController.
 */
class CommentoControllerTest {

    /**
     * **Partizioni individuate per il testing dei metodi in CommentoController**
     * Metodo `aggiungiCommentoAPost`:
     * - ID post valido con commento valido.
     * - ID post valido con commento non valido (e.g., dati mancanti o null).
     * - ID post non valido (e.g., null o vuoto) con commento valido.
     * - ID post non valido con commento non valido.
     * Metodo `aggiungiSubcommentoACommento`:
     * - ID commento valido con subcommento valido.
     * - ID commento valido con subcommento non valido (e.g., dati mancanti o null).
     * - ID commento non valido (e.g., null o vuoto) con subcommento valido.
     * - ID commento non valido con subcommento non valido.
     * Metodo `trovaCommentiPerPost`:
     * - ID post valido con commenti associati.
     * - ID post valido senza commenti associati.
     * - ID post non valido (e.g., null o vuoto).
     * Metodo `trovaSubcommentiPerCommento`:
     * - ID commento valido con subcommenti associati.
     * - ID commento valido senza subcommenti associati.
     * - ID commento non valido (e.g., null o vuoto).
     */


    @InjectMocks
    private CommentoController controller;

    @Mock
    private CommentoService commentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per aggiungiCommentoAPost.
     */
    @Test
    void aggiungiCommentoAPost_ShouldReturnCreatedCommento() {
        String idPost = "1";
        Commento commento = new Commento("1", "123", "Testo del commento", new Date(), 0, null, idPost);

        when(commentoService.aggiungiCommentoAPost(eq(idPost), any(Commento.class))).thenReturn(commento);

        System.out.println("Testing aggiungiCommentoAPost with valid post ID and valid comment...");
        ResponseEntity<Commento> response = controller.aggiungiCommentoAPost(idPost, commento);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(commento, response.getBody());
        verify(commentoService, times(1)).aggiungiCommentoAPost(eq(idPost), any(Commento.class));
    }

    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForInvalidCommento() {
        String idPost = "1";
        Commento invalidCommento = new Commento(); // Commento non valido

        System.out.println("Testing aggiungiCommentoAPost with valid post ID but invalid comment...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiCommentoAPost(idPost, invalidCommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiCommentoAPost(anyString(), any(Commento.class));
    }

    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForInvalidPostId() {
        String invalidIdPost = null;
        Commento commento = new Commento("1", "123", "Testo del commento", new Date(), 0, null, "1");

        System.out.println("Testing aggiungiCommentoAPost with invalid post ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiCommentoAPost(invalidIdPost, commento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiCommentoAPost(anyString(), any(Commento.class));
    }

    @Test
    void aggiungiCommentoAPost_ShouldThrowExceptionForInvalidPostIdAndCommento() {
        String invalidIdPost = null;
        Commento invalidCommento = new Commento();

        System.out.println("Testing aggiungiCommentoAPost with invalid post ID and invalid comment...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiCommentoAPost(invalidIdPost, invalidCommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiCommentoAPost(anyString(), any(Commento.class));
    }

    /**
     * Test per aggiungiSubcommentoACommento.
     */
    @Test
    void aggiungiSubcommentoACommento_ShouldReturnCreatedSubcommento() {
        String idCommento = "123";
        Subcommento subcommento = new Subcommento("1", "Testo del subcommento", new Date());

        when(commentoService.aggiungiSubcommentoACommento(eq(idCommento), any(Subcommento.class))).thenReturn(subcommento);

        System.out.println("Testing aggiungiSubcommentoACommento with valid comment ID and valid subcomment...");
        ResponseEntity<Subcommento> response = controller.aggiungiSubcommentoACommento(idCommento, subcommento);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(subcommento, response.getBody());
        verify(commentoService, times(1)).aggiungiSubcommentoACommento(eq(idCommento), any(Subcommento.class));
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForInvalidSubcommento() {
        String idCommento = "123";
        Subcommento invalidSubcommento = new Subcommento();

        System.out.println("Testing aggiungiSubcommentoACommento with valid comment ID but invalid subcomment...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiSubcommentoACommento(idCommento, invalidSubcommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiSubcommentoACommento(anyString(), any(Subcommento.class));
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForInvalidCommentId() {
        String invalidIdCommento = null;
        Subcommento subcommento = new Subcommento("1", "Testo del subcommento", new Date());

        System.out.println("Testing aggiungiSubcommentoACommento with invalid comment ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiSubcommentoACommento(invalidIdCommento, subcommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiSubcommentoACommento(anyString(), any(Subcommento.class));
    }

    @Test
    void aggiungiSubcommentoACommento_ShouldThrowExceptionForInvalidCommentIdAndSubcommento() {
        String invalidIdCommento = null;
        Subcommento invalidSubcommento = new Subcommento();

        System.out.println("Testing aggiungiSubcommentoACommento with invalid comment ID and invalid subcomment...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.aggiungiSubcommentoACommento(invalidIdCommento, invalidSubcommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).aggiungiSubcommentoACommento(anyString(), any(Subcommento.class));
    }

    /**
     * Test per trovaCommentiPerPost.
     */
    @Test
    void trovaCommentiPerPost_ShouldReturnCommentiList() {
        String idPost = "1";
        List<Commento> commenti = Arrays.asList(
                new Commento("1", "123", "Testo commento 1", new Date(), 10, null, idPost),
                new Commento("2", "124", "Testo commento 2", new Date(), 5, null, idPost)
        );

        when(commentoService.trovaCommentiPerPost(idPost)).thenReturn(commenti);

        System.out.println("Testing trovaCommentiPerPost with valid post ID and associated comments...");
        ResponseEntity<List<Commento>> response = controller.trovaCommentiPerPost(idPost);

        assertNotNull(response.getBody());
        System.out.println("Response: " + response);
        assertEquals(2, response.getBody().size());
        assertEquals(commenti, response.getBody());
        verify(commentoService, times(1)).trovaCommentiPerPost(idPost);
    }

    @Test
    void trovaCommentiPerPost_ShouldReturnEmptyList() {
        String idPost = "1";
        when(commentoService.trovaCommentiPerPost(idPost)).thenReturn(Collections.emptyList());

        System.out.println("Testing trovaCommentiPerPost with valid post ID but no associated comments...");
        ResponseEntity<List<Commento>> response = controller.trovaCommentiPerPost(idPost);

        assertNotNull(response.getBody());
        System.out.println("Response: " + response);
        assertTrue(response.getBody().isEmpty());
        verify(commentoService, times(1)).trovaCommentiPerPost(idPost);
    }

    @Test
    void trovaCommentiPerPost_ShouldThrowExceptionForInvalidPostId() {
        String invalidIdPost = null;

        System.out.println("Testing trovaCommentiPerPost with invalid post ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.trovaCommentiPerPost(invalidIdPost));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).trovaCommentiPerPost(anyString());
    }

    /**
     * Test per trovaSubcommentiPerCommento.
     */
    @Test
    void trovaSubcommentiPerCommento_ShouldReturnSubcommentiList() {
        String idCommento = "123";
        List<Subcommento> subcommenti = Arrays.asList(
                new Subcommento("1", "Testo subcommento 1", new Date()),
                new Subcommento("2", "Testo subcommento 2", new Date())
        );

        when(commentoService.trovaSubcommentiPerCommento(idCommento)).thenReturn(subcommenti);

        System.out.println("Testing trovaSubcommentiPerCommento with valid comment ID and associated subcomments...");
        ResponseEntity<List<Subcommento>> response = controller.trovaSubcommentiPerCommento(idCommento);

        assertNotNull(response.getBody());
        System.out.println("Response: " + response);
        assertEquals(2, response.getBody().size());
        assertEquals(subcommenti, response.getBody());
        verify(commentoService, times(1)).trovaSubcommentiPerCommento(idCommento);
    }

    @Test
    void trovaSubcommentiPerCommento_ShouldReturnEmptyList() {
        String idCommento = "123";
        when(commentoService.trovaSubcommentiPerCommento(idCommento)).thenReturn(Collections.emptyList());

        System.out.println("Testing trovaSubcommentiPerCommento with valid comment ID but no associated subcomments...");
        ResponseEntity<List<Subcommento>> response = controller.trovaSubcommentiPerCommento(idCommento);

        assertNotNull(response.getBody());
        System.out.println("Response: " + response);
        assertTrue(response.getBody().isEmpty());
        verify(commentoService, times(1)).trovaSubcommentiPerCommento(idCommento);
    }

    @Test
    void trovaSubcommentiPerCommento_ShouldThrowExceptionForInvalidCommentId() {
        String invalidIdCommento = null;

        System.out.println("Testing trovaSubcommentiPerCommento with invalid comment ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.trovaSubcommentiPerCommento(invalidIdCommento));

        System.out.println("Exception: " + exception.getMessage());
        verify(commentoService, never()).trovaSubcommentiPerCommento(anyString());
    }
}
