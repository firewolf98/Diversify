package it.unisa.diversifybe.Service;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UtenteService utenteService;

    public PostService(PostRepository postRepository, UtenteService utenteService) {
        this.postRepository = postRepository;
        this.utenteService = utenteService;
    }

    /**
     * Trova un post per ID.
     *
     * @param idPost L'ID del post da trovare.
     * @return Un Optional contenente il post se trovato, altrimenti vuoto.
     */
    public Optional<Post> findById(String idPost) {
        if (idPost == null) {
            throw new IllegalArgumentException("idPost non può essere null");
        }
        if (idPost.isEmpty()) {
            throw new IllegalArgumentException("idPost non può essere vuoto");
        }
        return postRepository.findById(idPost);
    }

    /**
     * Salva un post. Completa i dettagli mancanti (autore, data, commenti, like)
     * e valida i dati prima di salvare.
     *
     * @param post L'oggetto Post da salvare.
     * @return Il post salvato.
     */
    public Post save(Post post) {
        // Valida i dati di base
        if (post.getTitolo() == null || post.getTitolo().isEmpty()) {
            throw new IllegalArgumentException("Il titolo del post non può essere nullo o vuoto.");
        }
        if (post.getContenuto() == null || post.getContenuto().isEmpty()) {
            throw new IllegalArgumentException("Il contenuto del post non può essere nullo o vuoto.");
        }

        // Recupera l'ID dell'autore dal nome utente
        if (post.getIdAutore() == null || post.getIdAutore().isEmpty()) {
            String idAutore = utenteService.getIdByUsername(post.getIdAutore());
            if (idAutore == null || idAutore.isEmpty()) {
                throw new IllegalArgumentException("Impossibile trovare l'autore del post.");
            }
            post.setIdAutore(idAutore);
        }

        // Imposta i valori mancanti
        if (post.getDataCreazione() == null) {
            post.setDataCreazione(new Date());
        }
        if (post.getCommenti() == null) {
            post.setCommenti(new ArrayList<>());
        }
        post.setLike(0);

        // Valida l'ID del forum
        if (post.getIdForum() == null || post.getIdForum().isEmpty()) {
            throw new IllegalArgumentException("L'ID del forum non può essere nullo o vuoto.");
        }

        // Salva il post nel repository
        return postRepository.save(post);
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
            if (post.getTitolo() == null || post.getTitolo().isEmpty()) {
                throw new IllegalArgumentException("Il titolo del post non può essere nullo o vuoto.");
            }
            if (post.getContenuto() == null || post.getContenuto().isEmpty()) {
                throw new IllegalArgumentException("Il contenuto del post non può essere nullo o vuoto.");
            }
            if (post.getIdForum() == null || post.getIdForum().isEmpty()) {
                throw new IllegalArgumentException("L'ID del forum non può essere nullo o vuoto.");
            }
            if (post.getIdAutore() == null || post.getIdAutore().isEmpty()) {
                throw new IllegalArgumentException("L'ID dell'autore non può essere nullo o vuoto.");
            }
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