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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        // Configura i dati
        String idPost = "1";
        Post post = new Post(idPost, "Titolo", "1", "Contenuto", new Date(), 0, new ArrayList<>(), "forum1");
        Commento commento = new Commento("1", "1", "Testo commento", new Date(), 0, null, idPost);

        Forum forum = new Forum("forum1", "Titolo forum", "Descrizione forum", "Italia", new ArrayList<>(List.of(post)));

        // Simula il comportamento dei repository
        when(commentoRepository.save(any(Commento.class))).thenReturn(commento);
        when(postRepository.save(any(Post.class))).thenReturn(post);  // Mock per il salvataggio del post
        when(forumRepository.findAll()).thenReturn(new ArrayList<>(List.of(forum)));  // Mock per il forum che contiene il post

        // Usa lenient() per evitare l'eccezione di stubbing non necessario
        lenient().when(forumRepository.save(any(Forum.class))).thenReturn(forum);  // Lenient per evitare l'eccezione

        // Esegui il test
        Commento result = service.aggiungiCommentoAPost(idPost, commento);

        // Verifiche
        assertNotNull(result);
        assertEquals("Testo commento", result.getContenuto());
        verify(commentoRepository, times(1)).save(commento);  // Verifica che il commento sia stato salvato
        verify(postRepository, times(1)).save(post);  // Verifica che il post sia stato salvato
        verify(forumRepository, times(1)).findAll();  // Verifica che il forumRepository sia stato chiamato
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
        // Dati di input
        String idPost = "1";
        Commento invalidCommento = new Commento(null, null, "", new Date(), 0, null, idPost);

        // Test che verifica l'eccezione lanciata quando il commento è invalido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiCommentoAPost(idPost, invalidCommento));

        // Verifica che il messaggio dell'eccezione sia quello atteso
        assertEquals("Il commento deve avere un autore e un contenuto valido.", exception.getMessage());

        // Verifica che il metodo save non venga mai chiamato sul repository dei commenti
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

        // Configura i mock
        when(commentoRepository.findById(idCommento)).thenReturn(Optional.of(commento));
        when(subcommentoRepository.save(subcommento)).thenReturn(subcommento);
        when(commentoRepository.save(commento)).thenReturn(commento);

        // Esegui il metodo
        Subcommento result = service.aggiungiSubcommentoACommento(idCommento, subcommento);

        // Verifiche
        assertNotNull(result);
        assertEquals("Testo subcommento", result.getContenuto());
        assertTrue(commento.getSubcommenti().contains(subcommento));
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
        // Dati di input
        String idCommento = "1";
        Commento commento = new Commento("1", idCommento, "Testo commento", new Date(), 0, new ArrayList<>(), "1");
        Subcommento invalidSubcommento = new Subcommento(null, "", null);

        // Test che verifica l'eccezione lanciata quando il subcommento è invalido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.aggiungiSubcommentoACommento(idCommento, invalidSubcommento));

        // Verifica che il messaggio dell'eccezione sia quello atteso
        assertEquals("Il subcommento deve avere un autore e un contenuto valido.", exception.getMessage());

        // Verifica che il metodo save non venga mai chiamato sul repository dei subcommenti
        verify(subcommentoRepository, never()).save(any(Subcommento.class));
    }

}
