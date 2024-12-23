package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Repository.PostRepository;
import org.springframework.stereotype.Service;

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
     * @param id L'ID del post da trovare.
     * @return Un Optional contenente il post se trovato, altrimenti vuoto.
     */
    public Optional<Post> findById(String id) {
        return postRepository.findById(id);
    }

    /**
     * Salva un post.
     *
     * @param post L'oggetto Post da salvare.
     */
    public void save(Post post) {
        postRepository.save(post);
    }
}
