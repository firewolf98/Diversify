package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Repository.CommentoRepository;
import it.unisa.diversifybe.Repository.PostRepository;
import it.unisa.diversifybe.Repository.SubcommentoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentoService {

    private CommentoRepository commentoRepository;

    private PostRepository postRepository;

    private SubcommentoRepository subcommentoRepository;

    /**
     * Aggiunge un commento a un post specifico.
     *
     * @param idPost   L'ID del post a cui aggiungere il commento.
     * @param commento Il commento da aggiungere.
     * @return Il commento salvato.
     */
    public Commento aggiungiCommentoAPost(String idPost, Commento commento) {
        Optional<Post> optionalPost = postRepository.findById(idPost);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Imposta i dati del commento
            commento.setData_creazione(new Date());
            commentoRepository.save(commento);

            // Aggiunge il commento alla lista dei commenti del post
            if (post.getCommenti() == null) {
                post.setCommenti(List.of(commento));
            } else {
                post.getCommenti().add(commento);
            }

            // Salva il post aggiornato
            postRepository.save(post);
            return commento;
        } else {
            throw new IllegalArgumentException("Post con ID " + idPost + " non trovato.");
        }
    }

    /**
     * Aggiunge un subcommento a un commento principale.
     *
     * @param idCommento  L'ID del commento principale.
     * @param subcommento Il subcommento da aggiungere.
     * @return Il subcommento salvato.
     */
    public Subcommento aggiungiSubcommentoACommento(String idCommento, Subcommento subcommento) {
        Optional<Commento> optionalCommento = commentoRepository.findById(idCommento);

        if (optionalCommento.isPresent()) {
            Commento commento = optionalCommento.get();

            // Imposta i dati del subcommento
            subcommento.setData_creazione(new Date());
            subcommentoRepository.save(subcommento);

            // Aggiunge il subcommento alla lista dei subcommenti
            if (commento.getSubcommenti() == null) {
                commento.setSubcommenti(List.of(subcommento));
            } else {
                commento.getSubcommenti().add(subcommento);
            }

            // Salva il commento aggiornato
            commentoRepository.save(commento);
            return subcommento;
        } else {
            throw new IllegalArgumentException("Commento con ID " + idCommento + " non trovato.");
        }
    }

    /**
     * Recupera tutti i commenti associati a un post.
     *
     * @param idPost L'ID del post.
     * @return La lista dei commenti.
     */
    public List<Commento> trovaCommentiPerPost(String idPost) {
        return commentoRepository.findById_autore(idPost);
    }

    /**
     * Trova un commento in base al suo ID.
     *
     * @param commentoId L'ID del commento da trovare.
     * @return Un {@link Optional} contenente il commento se trovato,
     * altrimenti un {@link Optional#empty()} se non esiste un commento con l'ID specificato.
     */

    public Optional<Commento> findById(String commentoId) {
        return commentoRepository.findById(commentoId);
    }

    /**
     * Salva o aggiorna un commento nel database.
     *
     * @param commento L'oggetto {@link Commento} da salvare o aggiornare.
     *                 Se il commento esiste già, verrà aggiornato; altrimenti, verrà creato un nuovo record.
     */
    public void save(Commento commento) {
        commentoRepository.save(commento);
    }

}