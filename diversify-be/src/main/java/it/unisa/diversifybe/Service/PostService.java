package it.unisa.diversifybe.Service;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Trova un post per ID.
     *
     * @param idPost L'ID del post da trovare.
     * @return Un Optional contenente il post se trovato, altrimenti vuoto.
     */
    public Optional<Post> findById(String idPost) {
        return postRepository.findById(idPost);
    }

    /**
     * Salva un post.
     *
     * @param post L'oggetto Post da salvare.
     */
    public void save(Post post) {
        postRepository.save(post);
    }

    /**
     * Aggiorna un post esistente.
     *
     * @param id L'ID del post da aggiornare.
     * @param updatedPost i dati aggiornati del post.
     * @return il post aggiornato, o un Optional.empty() se non trovato.
     */
    public Optional<Post> updatePost(String id, Post updatedPost) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            post.setTitolo(updatedPost.getTitolo());
            post.setContenuto(updatedPost.getContenuto());
            post.setIdForum(updatedPost.getIdForum()); // Aggiorna il forum se necessario
            postRepository.save(post);
            return Optional.of(post);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Elimina un post.
     *
     * @param id L'ID del post da eliminare.
     */
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    /**
     * Trova tutti i post appartenenti a un determinato forum.
     *
     * @param idForum L'ID del forum di cui recuperare i post.
     * @return Una lista di post che appartengono al forum specificato.
     */
    public List<Post> findPostsByForum(String idForum) {
        return postRepository.findByIdForum(idForum);
    }
}