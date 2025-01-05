package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Trova un post per ID.
     *
     * @param id L'ID del post da trovare.
     * @return Un'entità ResponseEntity contenente il post se trovato, altrimenti uno stato 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Optional<Post> post = postService.findById(id);
        return post.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuovo post.
     *
     * @param post L'oggetto Post da salvare.
     * @return Il post creato.
     */
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        if (post == null) {
            throw new NullPointerException("Il post fornito è nullo.");
        }
        postService.save(post);
        return post;
    }

    /**
     * Aggiorna un post esistente.
     *
     * @param id   L'ID del post da aggiornare.
     * @param post L'oggetto Post aggiornato.
     * @return Un'entità ResponseEntity contenente il post aggiornato, oppure uno stato 404 se non trovato.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Il post non può essere nullo");
        }
        Optional<Post> updatedPost = postService.updatePost(id, post);
        return updatedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un post esistente.
     * Utilizza il metodo POST per la cancellazione, come richiesto.
     *
     * @param id L'ID del post da eliminare.
     * @return Una risposta senza contenuto (204) se l'eliminazione ha successo, oppure uno stato 404 se non trovato.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        Optional<Post> existingPost = postService.findById(id);
        if (existingPost.isPresent()) {
            postService.deletePost(id);
            return ResponseEntity.noContent().build(); // Stato 204 se eliminazione avviene correttamente
        } else {
            return ResponseEntity.notFound().build(); // Stato 404 se il post non è trovato
        }
    }

    /**
     * Recupera tutti i post associati a un determinato forum.
     *
     * @param forumId L'ID del forum.
     * @return Una lista di post associati al forum.
     */
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<Post>> getPostsByForumId(@PathVariable String forumId) {
        List<Post> posts = postService.findPostsByForum(forumId); // Utilizza direttamente findByForumId()
        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build(); // Stato 404 se nessun post trovato
        }
        return ResponseEntity.ok(posts); // Stato 200 con i post
    }
}