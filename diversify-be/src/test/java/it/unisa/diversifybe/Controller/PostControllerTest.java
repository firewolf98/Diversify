package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Service.PostService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

public class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    public PostControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    /*
Il codice contiene una serie di test unitari per il controller `PostController`, che gestisce le operazioni CRUD (Create, Read, Update, Delete) sui post.
Ogni metodo di test simula un comportamento specifico in base a vari input, verificando la risposta del controller.

1. testGetPostById_ValidId: Quando viene fornito un ID valido, il metodo simula il recupero di un post dal servizio,
   e il controller restituisce il post con lo stato HTTP 200 (OK) e i dati corretti nel corpo della risposta.

2. testGetPostById_InvalidId: Quando viene fornito un ID non valido (ad esempio, un ID inesistente),
   il metodo simula l'assenza del post nel servizio, e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.

3. testGetPostById_NullId: Quando viene fornito un ID nullo, il metodo simula l'assenza del post e il controller
   restituisce un errore 404 (Not Found) senza corpo nella risposta.

4. testCreatePost_ValidPost: Quando viene fornito un post valido, il metodo simula il salvataggio del post nel servizio,
   e il controller restituisce il post creato con lo stato HTTP 200 (OK).

5. testCreatePost_NullPost: Quando viene fornito un post nullo, il metodo simula una condizione di errore e
   il controller lancia un'eccezione `NullPointerException`, con il messaggio "Il post fornito è nullo".

6. testUpdatePost_ValidIdAndPost: Quando viene fornito un ID valido e un post valido, il metodo simula l'aggiornamento del post nel servizio,
   e il controller restituisce il post aggiornato con lo stato HTTP 200 (OK).

7. testUpdatePost_InvalidId: Quando viene fornito un ID non valido e un post valido, il metodo simula l'assenza del post nel servizio,
   e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.

8. testUpdatePost_NullPost: Quando viene fornito un ID valido ma un post nullo, il metodo simula una condizione di errore e
   il controller lancia un'eccezione `NullPointerException`.

9. testDeletePost_ValidId: Quando viene fornito un ID valido, il metodo simula l'eliminazione del post nel servizio,
   e il controller restituisce lo stato HTTP 204 (No Content), indicando che il post è stato eliminato con successo.

10. testDeletePost_InvalidId: Quando viene fornito un ID non valido, il metodo simula l'assenza del post nel servizio,
    e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.

11. testDeletePost_NullId: Quando viene fornito un ID nullo, il metodo simula l'assenza del post nel servizio,
    e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.

12. testGetPostsByForumId_ValidForumIdWithPosts: Quando viene fornito un forum ID valido con post associati,
    il metodo simula il recupero dei post dal servizio, e il controller restituisce una lista di post con lo stato HTTP 200 (OK)
    e i dati corretti nel corpo della risposta.

13. testGetPostsByForumId_ValidForumIdNoPosts: Quando viene fornito un forum ID valido ma senza post associati,
    il metodo simula l'assenza di post nel servizio, e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.

14. testGetPostsByForumId_NullForumId: Quando viene fornito un forum ID nullo, il metodo simula l'assenza di post nel servizio,
    e il controller restituisce un errore 404 (Not Found) senza corpo nella risposta.
*/

    // 1. Metodo: getPostById(String id)
    @Test
    public void testGetPostById_ValidId() {
        // Creazione di un oggetto Post con valori di prova
        Post post = new Post();
        post.setIdPost("post123");
        post.setTitolo("Esempio di Post");
        post.setIdAutore("autore456");
        post.setContenuto("Questo è un contenuto di esempio per il post.");
        post.setDataCreazione(new Date());
        post.setLike(42);
        post.setIdForum("forum789");

        // Simulazione del comportamento del servizio
        when(postService.findById("post123")).thenReturn(Optional.of(post));

        // Esecuzione del metodo da testare
        ResponseEntity<Post> response = postController.getPostById("post123");

        // Verifica dello stato della risposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());

        // Stampa dei valori del corpo della risposta
        System.out.println("Response Body:");
        System.out.println("ID Post: " + Objects.requireNonNull(response.getBody()).getIdPost());
        System.out.println("Titolo: " + response.getBody().getTitolo());
        System.out.println("ID Autore: " + response.getBody().getIdAutore());
        System.out.println("Contenuto: " + response.getBody().getContenuto());
        System.out.println("Data Creazione: " + response.getBody().getDataCreazione());
        System.out.println("Numero di Like: " + response.getBody().getLike());
        System.out.println("ID Forum: " + response.getBody().getIdForum());
    }

    @Test
    public void testGetPostById_InvalidId() {
        // Input: ID non valido
        String invalidId = "999";

        // Simulazione del comportamento del servizio
        when(postService.findById(invalidId)).thenReturn(Optional.empty());

        // Esecuzione del metodo da testare
        ResponseEntity<Post> response = postController.getPostById(invalidId);

        // Verifica dello stato della risposta
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Stampa dei dettagli della risposta
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
    }

    @Test
    public void testGetPostById_NullId() {
        // Input: ID nullo
        String nullId = null;

        // Esecuzione del metodo da testare
        ResponseEntity<Post> response = postController.getPostById(nullId);

        // Verifica dello stato della risposta
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Stampa dei dettagli della risposta
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
    }

    // 2. Metodo: createPost(Post post)
    @Test
    public void testCreatePost_ValidPost() {
        // Creazione di un oggetto Post con valori di prova
        Post post = new Post();
        post.setIdPost("post123");
        post.setTitolo("Titolo di Prova");
        post.setIdAutore("autore456");
        post.setContenuto("Questo è un contenuto di esempio.");
        post.setDataCreazione(new Date());
        post.setLike(10);
        post.setIdForum("forum789");

        // Simulazione del comportamento del servizio
        doAnswer(invocation -> {
            Post argument = invocation.getArgument(0); // Ottiene l'argomento passato al metodo save
            assertEquals(post, argument); // Verifica che il post passato sia quello atteso
            return null; // Simula il comportamento di un metodo void
        }).when(postService).save(any(Post.class));

        // Esecuzione del metodo da testare
        Post response = postController.createPost(post);

        // Verifica della risposta
        assertNotNull(response);
        assertEquals(post.getIdPost(), response.getIdPost());
        assertEquals(post.getTitolo(), response.getTitolo());
        assertEquals(post.getIdAutore(), response.getIdAutore());
        assertEquals(post.getContenuto(), response.getContenuto());
        assertEquals(post.getDataCreazione(), response.getDataCreazione());
        assertEquals(post.getLike(), response.getLike());
        assertEquals(post.getIdForum(), response.getIdForum());

        // Verifica delle interazioni con il servizio
        verify(postService, times(1)).save(post);

        // Stampa dei dettagli della risposta
        System.out.println("Post Creato:");
        System.out.println("ID Post: " + response.getIdPost());
        System.out.println("Titolo: " + response.getTitolo());
        System.out.println("ID Autore: " + response.getIdAutore());
        System.out.println("Contenuto: " + response.getContenuto());
        System.out.println("Data Creazione: " + response.getDataCreazione());
        System.out.println("Numero di Like: " + response.getLike());
        System.out.println("ID Forum: " + response.getIdForum());
    }


    @Test
    public void testCreatePost_NullPost() {
        // Input: Post nullo
        Post nullPost = null;

        // Esecuzione del metodo da testare e verifica dell'eccezione
        Exception exception = assertThrows(NullPointerException.class, () -> postController.createPost(nullPost));

        // Verifica del messaggio dell'eccezione
        assertEquals("Il post fornito è nullo.", exception.getMessage());

        // Stampa dei dettagli dell'eccezione
        System.out.println("Test Create Post with Null Post:");
        System.out.println("Eccezione catturata: " + exception.getClass().getSimpleName());
        System.out.println("Messaggio dell'eccezione: " + exception.getMessage());
    }

    // 3. Metodo: updatePost(String id, Post post)
    @Test
    public void testUpdatePost_ValidIdAndPost() {
        // Input: ID valido e Post valido
        String validId = "123";
        Post post = new Post();
        post.setIdPost(validId);
        post.setTitolo("Titolo Aggiornato");
        post.setIdAutore("autore456");
        post.setContenuto("Contenuto aggiornato del post.");
        post.setDataCreazione(new Date());
        post.setLike(10);
        post.setIdForum("forum123");

        // Mock del comportamento del servizio
        when(postService.updatePost(validId, post)).thenReturn(Optional.of(post));

        // Esecuzione del metodo da testare
        ResponseEntity<Post> response = postController.updatePost(validId, post);

        // Verifica dell'output
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());

        // Stampa dei dettagli del test
        System.out.println("Test Update Post with Valid ID and Post:");
        System.out.println("ID fornito: " + validId);
        System.out.println("Post fornito:");
        System.out.println("  ID Post: " + post.getIdPost());
        System.out.println("  Titolo: " + post.getTitolo());
        System.out.println("  ID Autore: " + post.getIdAutore());
        System.out.println("  Contenuto: " + post.getContenuto());
        System.out.println("  Data Creazione: " + post.getDataCreazione());
        System.out.println("  Numero di Like: " + post.getLike());
        System.out.println("  ID Forum: " + post.getIdForum());
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testUpdatePost_InvalidId() {
        // Input: ID non valido e Post valido
        String invalidId = "999";
        Post post = new Post();
        post.setIdPost("post123");
        post.setTitolo("Titolo non valido");
        post.setIdAutore("autore456");
        post.setContenuto("Contenuto del post con ID non valido.");
        post.setDataCreazione(new Date());
        post.setLike(5);
        post.setIdForum("forum123");

        // Mock del comportamento del servizio
        when(postService.updatePost(invalidId, post)).thenReturn(Optional.empty());

        // Esecuzione del metodo da testare
        ResponseEntity<Post> response = postController.updatePost(invalidId, post);

        // Verifica dell'output
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Stampa dei dettagli del test
        System.out.println("Test Update Post with Invalid ID:");
        System.out.println("ID fornito: " + invalidId);
        System.out.println("Post fornito:");
        System.out.println("  ID Post: " + post.getIdPost());
        System.out.println("  Titolo: " + post.getTitolo());
        System.out.println("  ID Autore: " + post.getIdAutore());
        System.out.println("  Contenuto: " + post.getContenuto());
        System.out.println("  Data Creazione: " + post.getDataCreazione());
        System.out.println("  Numero di Like: " + post.getLike());
        System.out.println("  ID Forum: " + post.getIdForum());
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testUpdatePost_NullPost() {
        // Input: ID valido e Post nullo
        String validId = "123";

        // Esecuzione del metodo da testare e verifica dell'eccezione
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                postController.updatePost(validId, null));

        // Stampa dei dettagli del test
        System.out.println("Test Update Post with Null Post:");
        System.out.println("ID fornito: " + validId);
        System.out.println("Post fornito: null");
        System.out.println("Eccezione lanciata: " + exception.getClass().getSimpleName());
        System.out.println("Messaggio dell'eccezione: " + exception.getMessage());

        // Controllo del messaggio dell'eccezione
        assertEquals("Il post non può essere nullo", exception.getMessage());
    }

    // 4. Metodo: deletePost(String id)
    @Test
    public void testDeletePost_ValidId() {
        // InputValue: ID valido per il post
        String validId = "123";

        // Creazione di un oggetto Post con valori di prova
        Post post = new Post();
        post.setIdPost(validId);
        post.setTitolo("Post da eliminare");
        post.setIdAutore("autore123");
        post.setContenuto("Contenuto del post da eliminare.");
        post.setDataCreazione(new Date());
        post.setLike(15);
        post.setIdForum("forum123");

        // Mock del comportamento del servizio
        when(postService.findById(validId)).thenReturn(Optional.of(post));

        // Esecuzione del metodo da testare
        ResponseEntity<Void> response = postController.deletePost(validId);

        // Verifica dell'output
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verifica che il metodo deletePost del servizio sia stato chiamato una sola volta
        verify(postService, times(1)).deletePost(validId);

        // Stampa dei dettagli del test
        System.out.println("Test Delete Post with Valid ID:");
        System.out.println("ID fornito: " + validId);
        System.out.println("Post fornito:");
        System.out.println("  ID Post: " + post.getIdPost());
        System.out.println("  Titolo: " + post.getTitolo());
        System.out.println("  ID Autore: " + post.getIdAutore());
        System.out.println("  Contenuto: " + post.getContenuto());
        System.out.println("  Data Creazione: " + post.getDataCreazione());
        System.out.println("  Numero di Like: " + post.getLike());
        System.out.println("  ID Forum: " + post.getIdForum());
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testDeletePost_InvalidId() {
        // InputValue: ID non valido per il post
        String invalidId = "999";

        // Mock del comportamento del servizio: nessun post trovato per l'ID fornito
        when(postService.findById(invalidId)).thenReturn(Optional.empty());

        // Esecuzione del metodo da testare
        ResponseEntity<Void> response = postController.deletePost(invalidId);

        // Verifica dell'output
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Stampa dei dettagli del test
        System.out.println("Test Delete Post with Invalid ID:");
        System.out.println("ID fornito: " + invalidId);
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testDeletePost_NullId() {
        // Input: ID nullo per il post
        String nullId = null;

        // Esecuzione del metodo da testare
        ResponseEntity<Void> response = postController.deletePost(nullId);

        // Verifica dell'output
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Stampa dei dettagli del test
        System.out.println("Test Delete Post with Null ID:");
        System.out.println("ID fornito: " + nullId);
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    // 5. Metodo: getPostsByForumId(String forumId)
    @Test
    public void testGetPostsByForumId_ValidForumIdWithPosts() {
        // Input: forumId valido con dei post associati
        String forumId = "forum123";

        // Creazione di una lista di post di prova
        Post post1 = new Post();
        post1.setIdPost("post1");
        post1.setTitolo("Post 1");
        post1.setIdAutore("autore1");
        post1.setContenuto("Contenuto del primo post.");
        post1.setDataCreazione(new Date());
        post1.setLike(10);
        post1.setIdForum(forumId);

        Post post2 = new Post();
        post2.setIdPost("post2");
        post2.setTitolo("Post 2");
        post2.setIdAutore("autore2");
        post2.setContenuto("Contenuto del secondo post.");
        post2.setDataCreazione(new Date());
        post2.setLike(20);
        post2.setIdForum(forumId);

        List<Post> posts = List.of(post1, post2); // Lista di post mock

        // Mock del comportamento del servizio: restituisce la lista di post per il forumId fornito
        when(postService.findPostsByForum(forumId)).thenReturn(posts);

        // Esecuzione del metodo da testare
        ResponseEntity<List<Post>> response = postController.getPostsByForumId(forumId);

        // Verifica dell'output
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());

        // Stampa dei dettagli del test
        System.out.println("Test Get Posts by Forum ID with Posts:");
        System.out.println("Forum ID fornito: " + forumId);
        System.out.println("Lista dei post:");
        for (Post post : posts) {
            System.out.println("  ID Post: " + post.getIdPost());
            System.out.println("  Titolo: " + post.getTitolo());
            System.out.println("  Autore: " + post.getIdAutore());
            System.out.println("  Contenuto: " + post.getContenuto());
            System.out.println("  Data Creazione: " + post.getDataCreazione());
            System.out.println("  Like: " + post.getLike());
        }
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testGetPostsByForumId_ValidForumIdNoPosts() {
        // InputValue: forumId valido, ma senza post associati
        String forumId = "forum123";

        // Mock del comportamento del servizio: restituisce una lista vuota per il forumId fornito
        when(postService.findPostsByForum(forumId)).thenReturn(List.of());

        // Esecuzione del metodo da testare
        ResponseEntity<List<Post>> response = postController.getPostsByForumId(forumId);

        // Verifica che lo stato della risposta sia NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verifica che il corpo della risposta sia null
        assertNull(response.getBody(), "Il corpo della risposta dovrebbe essere null quando non ci sono post.");

        // Stampa dei dettagli del test per il debug
        System.out.println("Test Get Posts by Forum ID with No Posts:");
        System.out.println("Forum ID fornito: " + forumId);
        System.out.println("Lista dei post restituita: " + response.getBody()); // Dovrebbe essere null
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }

    @Test
    public void testGetPostsByForumId_NullForumId() {
        // Input: forumId nullo
        String forumId = null;

        // Mock del comportamento del servizio: restituisce una lista vuota per un forumId nullo
        when(postService.findPostsByForum(forumId)).thenReturn(List.of());

        // Esecuzione del metodo da testare
        ResponseEntity<List<Post>> response = postController.getPostsByForumId(forumId);

        // Verifica che lo stato della risposta sia NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Lo stato della risposta dovrebbe essere 404 quando il forumId è nullo");

        // Verifica che il corpo della risposta sia null
        assertNull(response.getBody(), "Il corpo della risposta dovrebbe essere null quando il forumId è nullo");

        // Stampa dei dettagli del test per il debug
        System.out.println("Test Get Posts by Forum ID with Null Forum ID:");
        System.out.println("Forum ID fornito: " + forumId);
        System.out.println("Lista dei post restituita: " + response.getBody()); // Dovrebbe essere null
        System.out.println("Risultato:");
        System.out.println("  Stato HTTP: " + response.getStatusCode());
        System.out.println("  Corpo della risposta: " + response.getBody());
    }
}