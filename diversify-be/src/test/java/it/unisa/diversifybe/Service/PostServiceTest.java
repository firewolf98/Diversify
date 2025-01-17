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
    private PostRepository postRepository;

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private PostService postService;  // L'istanza di PostService da testare

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
        //post valido
        Post post = new Post();
        post.setIdPost("123");  // ID del post
        post.setTitolo("Titolo del post");  // Titolo del post
        post.setContenuto("Contenuto del post");  // Contenuto del post
        post.setIdForum("forum1");  // ID del forum
        post.setDataCreazione(new Date());  // Data di creazione del post
        post.setLike(10);  // Numero di like
        post.setCommenti(new ArrayList<>());  // Lista di commenti vuota
        post.setIdAutore("autore1");  // ID dell'autore

        // Comportamento atteso: il post viene salvato nel repository
        when(postRepository.save(post)).thenReturn(post);
        when(forumRepository.findByIdForum(post.getIdForum())).thenReturn(new ArrayList<>());  // Mock del forum

        // Esegui il metodo
        postService.save(post);

        // Verifica che il metodo save sia stato chiamato una volta
        verify(postRepository, times(1)).save(post);
        verify(forumRepository, times(1)).findByIdForum(post.getIdForum());

        System.out.println("Metodo save chiamato correttamente.");
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
        // Categoria 1: idForum valido
        String idForum = "forum1";

        // Creazione di tre post di prova con valori assegnati uno per riga
        Post post1 = new Post();
        post1.setIdPost("123");
        post1.setTitolo("Titolo 1");
        post1.setIdAutore("Autore1");
        post1.setContenuto("Contenuto del primo post");
        post1.setDataCreazione(new Date());
        post1.setLike(10);
        post1.setCommenti(new ArrayList<>());
        post1.setIdForum(idForum);

        Post post2 = new Post();
        post2.setIdPost("124");
        post2.setTitolo("Titolo 2");
        post2.setIdAutore("Autore2");
        post2.setContenuto("Contenuto del secondo post");
        post2.setDataCreazione(new Date());
        post2.setLike(5);
        post2.setCommenti(new ArrayList<>());
        post2.setIdForum("forum1");

        Post post3 = new Post();
        post3.setIdPost("125");
        post3.setTitolo("Titolo 3");
        post3.setIdAutore("Autore3");
        post3.setContenuto("Contenuto del terzo post");
        post3.setDataCreazione(new Date());
        post3.setLike(8);
        post3.setCommenti(new ArrayList<>());
        post3.setIdForum("forum1");

        // Crea un forum che contiene i post
        Forum forum = new Forum();
        forum.setIdForum(idForum);
        forum.setPost(List.of(post1, post2, post3));

        // Mock del comportamento del repository
        when(forumRepository.findById(idForum)).thenReturn(Optional.of(forum));

        // Stampa dei dettagli prima dell'esecuzione del metodo
        System.out.println("Prima della ricerca:");
        System.out.println("ID del forum: " + idForum);
        System.out.println("Post nel repository:");
        List<Post> posts = List.of(post1, post2, post3);
        posts.forEach(System.out::println);

        // Esecuzione del metodo di ricerca
        List<Post> result = postService.findPostsByForum(idForum);

        // Stampa dei dettagli dopo l'esecuzione del metodo
        System.out.println("Dopo la ricerca:");
        System.out.println("Risultati trovati: ");
        result.forEach(System.out::println);

        // Verifiche
        assertEquals(3, result.size());
        assertEquals(post1, result.get(0)); // Verifica che il primo post trovato sia quello giusto
        System.out.println("Primo post trovato: " + result.get(0));
    }


    @Test
    void testFindPostsByForum_InvalidIdForum() {
        // Categoria 2: idForum non valido
        String idForum = "nonExistentForum";

        // Creazione di tre post con valori di prova
        Post post1 = new Post();
        post1.setIdPost("101");
        post1.setTitolo("Primo Post");
        post1.setIdAutore("Autore1");
        post1.setContenuto("Contenuto del primo post");
        post1.setDataCreazione(new Date());
        post1.setLike(10);
        post1.setCommenti(new ArrayList<>());
        post1.setIdForum("forum1");

        Post post2 = new Post();
        post2.setIdPost("102");
        post2.setTitolo("Secondo Post");
        post2.setIdAutore("Autore2");
        post2.setContenuto("Contenuto del secondo post");
        post2.setDataCreazione(new Date());
        post2.setLike(20);
        post2.setCommenti(new ArrayList<>());
        post2.setIdForum("forum1");

        Post post3 = new Post();
        post3.setIdPost("103");
        post3.setTitolo("Terzo Post");
        post3.setIdAutore("Autore3");
        post3.setContenuto("Contenuto del terzo post");
        post3.setDataCreazione(new Date());
        post3.setLike(30);
        post3.setCommenti(new ArrayList<>());
        post3.setIdForum("forum1");

        // Aggiunta dei post in una lista (anche se non verranno trovati per idForum non valido)
        List<Post> posts = List.of(post1, post2, post3);

        // Mock del comportamento del repository per forum non valido
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Stampa dei dettagli prima dell'esecuzione del metodo
        System.out.println("Prima della ricerca:");
        System.out.println("Post nel repository:");
        posts.forEach(System.out::println);

        // Esecuzione del metodo di ricerca
        List<Post> result = postService.findPostsByForum(idForum);

        // Stampa dei dettagli dopo l'esecuzione del metodo
        System.out.println("Dopo la ricerca:");
        System.out.println("Risultati trovati:");
        result.forEach(System.out::println);

        // Verifiche
        assertTrue(result.isEmpty());
        System.out.println("Nessun post trovato per idForum essendo invalido: " + idForum);
    }


    @Test
    void testFindPostsByForum_NullIdForum() {
        // Categoria 3: idForum null
        String idForum = null;

        // Mock del comportamento del repository per idForum null
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Esecuzione del metodo di ricerca
        List<Post> result = postService.findPostsByForum(idForum);

        // Stampa dei dettagli dopo l'esecuzione del metodo
        System.out.println("Dopo la ricerca:");
        System.out.println("Risultati trovati:");
        result.forEach(System.out::println);

        // Verifiche
        assertTrue(result.isEmpty());
        System.out.println("Nessun post trovato per idForum: " + idForum);
    }


    @Test
    void testFindPostsByForum_EmptyIdForum() {
        // Categoria 4: idForum vuoto
        String idForum = "";

        // Creazione di tre post con valori di prova
        Post post1 = new Post();
        post1.setIdPost("101");
        post1.setTitolo("Primo Post");
        post1.setIdAutore("Autore1");
        post1.setContenuto("Contenuto del primo post");
        post1.setDataCreazione(new Date());
        post1.setLike(10);
        post1.setCommenti(new ArrayList<>());
        post1.setIdForum("forum1");

        Post post2 = new Post();
        post2.setIdPost("102");
        post2.setTitolo("Secondo Post");
        post2.setIdAutore("Autore2");
        post2.setContenuto("Contenuto del secondo post");
        post2.setDataCreazione(new Date());
        post2.setLike(20);
        post2.setCommenti(new ArrayList<>());
        post2.setIdForum("forum2");

        Post post3 = new Post();
        post3.setIdPost("103");
        post3.setTitolo("Terzo Post");
        post3.setIdAutore("Autore3");
        post3.setContenuto("Contenuto del terzo post");
        post3.setDataCreazione(new Date());
        post3.setLike(30);
        post3.setCommenti(new ArrayList<>());
        post3.setIdForum("forum3");

        // Aggiunta dei post in una lista
        List<Post> posts = List.of(post1, post2, post3);

        // Mock del comportamento del forumRepository per idForum vuoto
        when(forumRepository.findById(idForum)).thenReturn(Optional.empty());

        // Esecuzione del metodo di ricerca
        List<Post> result = postService.findPostsByForum(idForum);

        // Verifica che la lista risultante sia vuota
        assertTrue(result.isEmpty());
        System.out.println("Nessun post trovato per idForum: " + idForum);

        // Verifica le interazioni con il mock
        verify(forumRepository, times(1)).findById(idForum);
    }
}