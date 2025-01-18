package it.unisa.diversifybe.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Repository.ForumRepository;
import it.unisa.diversifybe.Repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)  // Assicura che i mock vengano iniettati correttamente
class PostServiceTest {

    @Mock
    private PostRepository postRepository;  // Mock del repository

    @InjectMocks
    private PostService postService;  // L'istanza di PostService da testare

    @Mock
    private ForumRepository forumRepository;

    @BeforeEach
    void setUp() {
        // Questo metodo viene eseguito prima di ogni test per assicurarsi che i mock vengano creati correttamente
    }

    @Test
    void testFindById_NullId() {
        // Categoria 1: idPost non valido (idPost è null)
        String idPost = null;

        // Esecuzione del metodo da testare
        System.out.println("Test FindById con idPost null:");
        System.out.println("idPost fornito: " + idPost);

        // Utilizziamo assertThrows per verificare che venga lanciata l'eccezione IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.findById(idPost);  // Chiama il metodo findById sull'istanza
        });

        // Stampa dei dettagli del test
        System.out.println("Eccezione lanciata: " + exception.getClass().getSimpleName());
        System.out.println("Messaggio dell'eccezione: " + exception.getMessage());

        // Verifica che l'eccezione sia effettivamente di tipo IllegalArgumentException e il messaggio corretto
        assertEquals("idPost non può essere null", exception.getMessage());
    }

    @Test
    void testFindById_EmptyId() {
        // Categoria 1: idPost non valido (idPost è una stringa vuota)
        String idPost = "";

        // Esecuzione del metodo da testare
        System.out.println("Test FindById con idPost vuoto:");
        System.out.println("idPost fornito: " + idPost);

        // Utilizziamo assertThrows per verificare che venga lanciata l'eccezione IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.findById(idPost);  // Chiama il metodo findById sull'istanza
        });

        // Stampa dei dettagli del test
        System.out.println("Eccezione lanciata: " + exception.getClass().getSimpleName());
        System.out.println("Messaggio dell'eccezione: " + exception.getMessage());

        // Verifica che l'eccezione sia effettivamente di tipo IllegalArgumentException e il messaggio corretto
        assertEquals("idPost non può essere vuoto", exception.getMessage());
    }

    @Test
    void testFindById_ValidId() {
        // Categoria 2: idPost valido (idPost esistente nel repository)
        String idPost = "123";  // Valore di prova per idPost
        String titolo = "Titolo del post";  // Valore di prova per titolo
        String idAutore = "utente1";  // Valore di prova per idAutore
        String contenuto = "Contenuto del post";  // Valore di prova per contenuto
        Date dataCreazione = new Date();  // Valore di prova per dataCreazione
        int like = 10;  // Valore di prova per like
        List<Commento> commenti = new ArrayList<>();  // Valore di prova per commenti

        // Creazione dei commenti con valori di prova
        Commento commento1 = new Commento();
        commento1.setIdAutore("utente1");
        commento1.setIdCommento("commento1");
        commento1.setContenuto("Contenuto del primo commento");
        commento1.setDataCreazione(new Date());
        commento1.setLike(5);
        commento1.setIdPost(idPost);

        Commento commento2 = new Commento();
        commento2.setIdAutore("utente2");
        commento2.setIdCommento("commento2");
        commento2.setContenuto("Contenuto del secondo commento");
        commento2.setDataCreazione(new Date());
        commento2.setLike(3);
        commento2.setIdPost(idPost);
        commenti.add(commento1);  // Aggiungi il primo commento
        commenti.add(commento2);  // Aggiungi il secondo commento
        String idForum = "forum1";  // Valore di prova per idForum

        Post post = new Post();
        post.setIdPost(idPost);
        post.setTitolo(titolo);
        post.setIdAutore(idAutore);
        post.setContenuto(contenuto);
        post.setDataCreazione(dataCreazione);
        post.setLike(like);
        post.setCommenti(commenti);
        post.setIdForum(idForum);

        // Comportamento atteso: restituisce un Optional con il post trovato
        when(postRepository.findById(idPost)).thenReturn(Optional.of(post));

        Optional<Post> result = postService.findById(idPost);

        System.out.println("Test Find By Id con ID valido:");
        System.out.println("ID fornito: " + idPost);
        System.out.println("Post trovato: " + result.get());
        System.out.println("Titolo del post: " + result.get().getTitolo());
        System.out.println("Contenuto del post: " + result.get().getContenuto());
        System.out.println("ID del forum: " + result.get().getIdForum());

        assertTrue(true);
        assertEquals(post, result.get());
    }

    @Test
    void testFindById_InvalidId() {
        // Categoria 2: idPost valido (idPost non esistente nel repository)
        String idPost = "999";  // Valore di prova per idPost

        // Comportamento atteso: lancia un'eccezione IllegalArgumentException
        when(postRepository.findById(idPost)).thenThrow(new IllegalArgumentException("ID non valido"));

        try {
            postService.findById(idPost);
            fail("Ci si aspettava un'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("Test Find By Id con ID non valido:");
            System.out.println("ID fornito: " + idPost);
            System.out.println("Eccezione lanciata: " + e.getClass().getSimpleName());
            System.out.println("Messaggio dell'eccezione: " + e.getMessage());

            assertEquals("ID non valido", e.getMessage());
        }
    }

    @Test
    void testSave_ValidPost() {
        // Arrange
        String idForum = "forum123";
        Post post = new Post();
        post.setTitolo("Titolo del post");
        post.setContenuto("Contenuto del post");
        post.setIdForum(idForum);

        List<Forum> forumList = new ArrayList<>();
        Forum forum = new Forum();
        forum.setIdForum(idForum);
        forum.setPost(new ArrayList<>());
        forumList.add(forum);

        when(forumRepository.findByIdForum(idForum)).thenReturn(forumList);
        when(forumRepository.save(any(Forum.class))).thenReturn(forum);

        // Act
        Post result = postService.save(post);

        // Assert
        assertNotNull(result.getIdPost(), "L'ID del post non dovrebbe essere nullo.");
        assertEquals("Titolo del post", result.getTitolo());
        assertEquals("Contenuto del post", result.getContenuto());
        assertNotNull(result.getDataCreazione(), "La data di creazione non dovrebbe essere nulla.");
        assertEquals(0, result.getLike(), "I like iniziali dovrebbero essere 0.");
        assertNotNull(result.getCommenti(), "La lista dei commenti non dovrebbe essere nulla.");
        assertTrue(result.getCommenti().isEmpty(), "La lista dei commenti dovrebbe essere vuota.");

        verify(forumRepository, times(1)).findByIdForum(idForum);
        verify(forumRepository, times(1)).save(any(Forum.class));
    }

    @Test
    void testSave_InvalidPost() {
        // Categoria 2: post con campi invalidi
        Post post = new Post();
        post.setIdPost("123");  // ID del post
        post.setTitolo(null);  // Titolo nullo
        post.setContenuto("Contenuto del post");  // Contenuto del post
        post.setIdForum("forum1");  // ID del forum
        post.setDataCreazione(new Date());  // Data di creazione del post
        post.setLike(10);  // Numero di like
        post.setCommenti(new ArrayList<>());  // Lista di commenti vuota
        post.setIdAutore("autore1");  // ID dell'autore

        // Comportamento atteso: un'eccezione viene lanciata per il campo titolo nullo
        try {
            System.out.println("Test Save con Post con campi invalidi (titolo null):");
            System.out.println("ID Post: " + post.getIdPost());
            System.out.println("Titolo: " + post.getTitolo());  // Titolo nullo
            System.out.println("Contenuto: " + post.getContenuto());
            System.out.println("ID Forum: " + post.getIdForum());
            System.out.println("ID Autore: " + post.getIdAutore());
            System.out.println("Data di creazione: " + post.getDataCreazione());
            System.out.println("Numero di like: " + post.getLike());
            System.out.println("Numero di commenti: " + post.getCommenti().size());

            // Verifica che venga lanciata un'eccezione IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> postService.save(post));
            System.out.println("Eccezione prevista: " + exception.getMessage());
        } catch (Exception e) {
            // In caso di eccezione diversa, il test fallisce
            fail("Ci si aspettava un'eccezione IllegalArgumentException, ma è stata lanciata: " + e.getClass().getSimpleName());
        }

        // Verifica che il metodo save non venga chiamato
        verify(postRepository, times(0)).save(post);
    }

    @Test
    void testUpdatePost_ValidId_ValidPost() {
        // Categoria 4: id valido e updatedPost valido
        String id = "123";

        // Valori di prova per il post esistente
        String existingTitle = "Titolo esistente";
        String existingContent = "Contenuto esistente";
        String existingForumId = "forum1";

        Post existingPost = new Post();
        existingPost.setIdPost(id);
        existingPost.setTitolo(existingTitle);
        existingPost.setContenuto(existingContent);
        existingPost.setIdForum(existingForumId);
        existingPost.setIdAutore("autore1");
        existingPost.setDataCreazione(new Date());
        existingPost.setLike(5);
        existingPost.setCommenti(new ArrayList<>());

        // Valori di prova per il post aggiornato
        String updatedTitle = "Nuovo Titolo";
        String updatedContent = "Nuovo Contenuto";

        Post updatedPost = new Post();
        updatedPost.setIdPost(id);
        updatedPost.setTitolo(updatedTitle);
        updatedPost.setContenuto(updatedContent);
        updatedPost.setIdForum(existingForumId);
        updatedPost.setIdAutore("autore1");
        updatedPost.setDataCreazione(new Date());
        updatedPost.setLike(5);
        updatedPost.setCommenti(new ArrayList<>());

        // Stampa dei dettagli prima dell'update
        System.out.println("Post Esistente:");
        System.out.println("ID: " + existingPost.getIdPost());
        System.out.println("Titolo: " + existingPost.getTitolo());
        System.out.println("Contenuto: " + existingPost.getContenuto());
        System.out.println("Forum ID: " + existingPost.getIdForum());

        // Comportamento atteso: il post esistente viene trovato e aggiornato
        when(postRepository.findById(id)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(updatedPost);

        Optional<Post> result = postService.updatePost(id, updatedPost);

        // Stampa dei dettagli dopo l'update
        System.out.println("Post Aggiornato:");
        System.out.println("ID: " + result.get().getIdPost());
        System.out.println("Titolo: " + result.get().getTitolo());
        System.out.println("Contenuto: " + result.get().getContenuto());
        System.out.println("Forum ID: " + result.get().getIdForum());

        assertTrue(true);
        assertEquals(updatedPost.getTitolo(), result.get().getTitolo());
        assertEquals(updatedPost.getContenuto(), result.get().getContenuto());
    }

    @Test
    void testUpdatePost_ValidId_InvalidPost() {
        // Categoria 4: id valido e updatedPost con campi invalidi
        String id = "123";

        // Valori di prova per il post esistente
        String existingTitle = "Titolo esistente";
        String existingContent = "Contenuto esistente";
        String existingForumId = "forum1";

        Post existingPost = new Post();
        existingPost.setIdPost(id);
        existingPost.setTitolo(existingTitle);
        existingPost.setContenuto(existingContent);
        existingPost.setIdForum(existingForumId);
        existingPost.setIdAutore("autore1");
        existingPost.setDataCreazione(new Date());
        existingPost.setLike(10);
        existingPost.setCommenti(new ArrayList<>());

        // Valori di prova per il post aggiornato (titolo nullo, quindi invalido)
        Post updatedPost = new Post();
        updatedPost.setIdPost(id);
        updatedPost.setTitolo(null); // Campo invalido
        updatedPost.setContenuto("Nuovo Contenuto");
        updatedPost.setIdForum(existingForumId);
        updatedPost.setIdAutore("autore1");
        updatedPost.setDataCreazione(new Date());
        updatedPost.setLike(15);
        updatedPost.setCommenti(new ArrayList<>());

        // Stampa dei dettagli del post esistente
        System.out.println("Post Esistente:");
        System.out.println("ID: " + existingPost.getIdPost());
        System.out.println("Titolo: " + existingPost.getTitolo());
        System.out.println("Contenuto: " + existingPost.getContenuto());
        System.out.println("Forum ID: " + existingPost.getIdForum());

        // Stampa dei dettagli del post aggiornato (prima dell'update)
        System.out.println("Post Aggiornato (Invalido):");
        System.out.println("ID: " + updatedPost.getIdPost());
        System.out.println("Titolo: " + updatedPost.getTitolo());
        System.out.println("Contenuto: " + updatedPost.getContenuto());
        System.out.println("Forum ID: " + updatedPost.getIdForum());

        // Comportamento atteso: l'eccezione viene lanciata
        when(postRepository.findById(id)).thenReturn(Optional.of(existingPost));

        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(id, updatedPost));
    }

    @Test
    void testUpdatePost_InvalidId_InvalidPost() {
        // Categoria 4: id non valido e updatedPost con campi invalidi
        String id = "999"; // ID che non esiste nel repository

        // Valori di prova per il post aggiornato (titolo nullo, quindi invalido)
        Post updatedPost = new Post();
        updatedPost.setIdPost(id);
        updatedPost.setTitolo(null); // Campo invalido
        updatedPost.setContenuto("Nuovo Contenuto");
        updatedPost.setIdForum("forum1");
        updatedPost.setIdAutore("autore1");
        updatedPost.setDataCreazione(new Date());
        updatedPost.setLike(5);
        updatedPost.setCommenti(new ArrayList<>());

        // Stampa dei dettagli del post aggiornato (prima dell'update)
        System.out.println("Prima dell'aggiornamento:");
        System.out.println("ID: " + updatedPost.getIdPost());
        System.out.println("Titolo: " + updatedPost.getTitolo());
        System.out.println("Contenuto: " + updatedPost.getContenuto());
        System.out.println("Forum ID: " + updatedPost.getIdForum());
        System.out.println("Autore ID: " + updatedPost.getIdAutore());
        System.out.println("Data Creazione: " + updatedPost.getDataCreazione());
        System.out.println("Numero di Like: " + updatedPost.getLike());
        System.out.println("Commenti: " + updatedPost.getCommenti());

        // Mock del comportamento del repository
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // Comportamento atteso: restituisce Optional.empty()
        Optional<Post> result = postService.updatePost(id, updatedPost);

        // Stampa del risultato dopo il tentativo di aggiornamento
        System.out.println("Dopo l'aggiornamento:");
        if (result.isPresent()) {
            Post postResult = result.get();
            System.out.println("ID: " + postResult.getIdPost());
            System.out.println("Titolo: " + postResult.getTitolo());
            System.out.println("Contenuto: " + postResult.getContenuto());
            System.out.println("Forum ID: " + postResult.getIdForum());
            System.out.println("Autore ID: " + postResult.getIdAutore());
            System.out.println("Data Creazione: " + postResult.getDataCreazione());
            System.out.println("Numero di Like: " + postResult.getLike());
            System.out.println("Commenti: " + postResult.getCommenti());
        } else {
            System.out.println("Nessun post trovato.");
        }

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeletePost_ValidId() {
        // Categoria 1: id valido
        String id = "123"; // ID del post da eliminare

        // Mock del comportamento del repository
        doAnswer(invocation -> {
            System.out.println("Eliminazione del post con ID: " + id);
            return null;
        }).when(postRepository).deleteById(id);

        // Stampa dei dettagli prima dell'eliminazione
        System.out.println("Prima dell'eliminazione:");
        System.out.println("ID del post da eliminare: " + id);

        // Esecuzione del metodo di eliminazione
        postService.deletePost(id);

        // Verifica che il metodo deleteById sia stato chiamato una volta
        verify(postRepository, times(1)).deleteById(id);

        // Stampa dei dettagli dopo l'eliminazione
        System.out.println("Dopo l'eliminazione:");
        System.out.println("Post con ID " + id + " eliminato con successo.");
    }

    @Test
    void testDeletePost_InvalidId() {
        // Categoria 2: id non valido
        String id = "999"; // ID che non esiste nel repository

        // Mock del comportamento del repository
        doAnswer(invocation -> {
            System.out.println("Tentativo di eliminazione del post con ID: " + id);
            return null;
        }).when(postRepository).deleteById(id);

        // Stampa dei dettagli prima dell'eliminazione
        System.out.println("Prima dell'eliminazione:");
        System.out.println("ID del post da eliminare: " + id);

        // Esecuzione del metodo di eliminazione
        postService.deletePost(id);

        // Verifica che il metodo deleteById sia stato chiamato una volta
        verify(postRepository, times(1)).deleteById(id);

        // Stampa dei dettagli dopo l'eliminazione
        System.out.println("Dopo l'eliminazione:");
        System.out.println("Post con ID " + id + " non trovato o non eliminato.");
    }

    @Test
    void testFindPostsByForum_ValidIdForum() {
        // Arrange
        String idForum = "forum123";

        // Caso 1: Il forum contiene post
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setIdPost("post1");
        post1.setTitolo("Titolo 1");
        posts.add(post1);

        Forum forumWithPosts = new Forum();
        forumWithPosts.setIdForum(idForum);
        forumWithPosts.setPost(posts);

        when(forumRepository.findById(idForum)).thenReturn(Optional.of(forumWithPosts));

        // Act
        List<Post> resultWithPosts = postService.findPostsByForum(idForum);

        // Assert
        assertNotNull(resultWithPosts);
        assertEquals(1, resultWithPosts.size());
        assertEquals("post1", resultWithPosts.get(0).getIdPost());
        assertEquals("Titolo 1", resultWithPosts.get(0).getTitolo());

        // Caso 2: Il forum non contiene post
        Forum forumWithoutPosts = new Forum();
        forumWithoutPosts.setIdForum(idForum);
        forumWithoutPosts.setPost(new ArrayList<>());

        when(forumRepository.findById(idForum)).thenReturn(Optional.of(forumWithoutPosts));

        // Act
        List<Post> resultWithoutPosts = postService.findPostsByForum(idForum);

        // Assert
        assertNotNull(resultWithoutPosts);
        assertTrue(resultWithoutPosts.isEmpty());
    }

    @Test
    void testFindPostsByForum_NullIdForum() {
        // Caso 1: idForum è nullo
        String idForumNull = null;

        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class, () ->
                postService.findPostsByForum(idForumNull));
        assertEquals("L'ID del forum non può essere nullo o vuoto.", exceptionNull.getMessage());
    }

    @Test
    void testFindPostsByForum_EmptyIdForum() {
        // Arrange
        String idForum = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                postService.findPostsByForum(idForum));
        assertEquals("L'ID del forum non può essere nullo o vuoto.", exception.getMessage());
    }

}