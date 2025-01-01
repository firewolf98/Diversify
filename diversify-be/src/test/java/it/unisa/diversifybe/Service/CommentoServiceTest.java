package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Repository.CommentoRepository;
import it.unisa.diversifybe.Repository.PostRepository;
import it.unisa.diversifybe.Repository.SubcommentoRepository;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per aggiungiCommentoAPost
     */
    @Test
    void aggiungiCommentoAPost_ShouldAddCommentoToPost() {
        String idPost = "1";
        Post post = new Post(idPost, "Titolo", "1", "Contenuto", new Date(), 0, new ArrayList<>(), "forum1");
        Commento commento = new Commento("1", null, "Testo commento", new Date(), 0, null, idPost);

        when(postRepository.findById(idPost)).thenReturn(Optional.of(post));
        when(commentoRepository.save(commento)).thenReturn(commento);
        when(postRepository.save(post)).thenReturn(post);

        Commento result = service.aggiungiCommentoAPost(idPost, commento);

        assertNotNull(result);
        assertEquals("Testo commento", result.getContenuto());
        verify(commentoRepository, times(1)).save(commento);
        verify(postRepository, times(1)).save(post);
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
        String idCommento = "1";
        Commento commento = new Commento("1", idCommento, "Testo commento", new Date(), 0, new ArrayList<>(), "1");
        Subcommento subcommento = new Subcommento("2", "Testo subcommento", new Date());

        when(commentoRepository.findById(idCommento)).thenReturn(Optional.of(commento));
        when(subcommentoRepository.save(subcommento)).thenReturn(subcommento);
        when(commentoRepository.save(commento)).thenReturn(commento);

        Subcommento result = service.aggiungiSubcommentoACommento(idCommento, subcommento);

        assertNotNull(result);
        assertEquals("Testo subcommento", result.getContenuto());
        verify(subcommentoRepository, times(1)).save(subcommento);
        verify(commentoRepository, times(1)).save(commento);
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
