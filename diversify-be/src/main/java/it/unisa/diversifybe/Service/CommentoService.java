package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Model.Post;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Repository.CommentoRepository;
import it.unisa.diversifybe.Repository.ForumRepository;
import it.unisa.diversifybe.Repository.PostRepository;
import it.unisa.diversifybe.Repository.SubcommentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentoService {

    @Autowired
    private CommentoRepository commentoRepository;

    @Autowired
    private ForumRepository forumRepository;

    /**
     * Aggiunge un commento a un post specifico.
     *
     * @param idPost   L'ID del post a cui aggiungere il commento.
     * @param commento Il commento da aggiungere.
     * @return Il commento salvato.
     * @throws IllegalArgumentException se l'ID del post o il commento non sono validi.
     */
    public Commento aggiungiCommentoAPost(String idPost, Commento commento) {
        if (idPost == null || idPost.isBlank()) {
            throw new IllegalArgumentException("L'ID del post non può essere nullo o vuoto.");
        }
        if (commento == null) {
            throw new IllegalArgumentException("Il commento non può essere nullo.");
        }
        if (commento.getIdAutore() == null || commento.getIdAutore().isBlank() ||
                commento.getContenuto() == null || commento.getContenuto().isBlank()) {
            throw new IllegalArgumentException("Il commento deve avere un autore e un contenuto valido.");
        }

        if (commento.getIdCommento() == null || commento.getIdCommento().isEmpty()) {
            commento.setIdCommento(UUID.randomUUID().toString());
        }

        // Imposta i valori mancanti
        if (commento.getDataCreazione() == null) {
            commento.setDataCreazione(new Date());
        }
        if (commento.getSubcommenti() == null) {
            commento.setSubcommenti(new ArrayList<>());
        }
        commento.setLike(0);

        List<Forum> forums = forumRepository.findAll();
        for(Forum forum : forums) {
            List<Post> posts = forum.getPost();
            if(posts != null) {
                for(Post post : posts) {
                    if(post.getIdPost().equals(idPost)) {
                        List<Commento> commenti = post.getCommenti();
                        commenti.add(commento);
                        post.setCommenti(commenti);
                        forum.setPost(posts);
                        forumRepository.save(forum);
                    }
                }
            }
        }
        return commento;
    }

    /**
     * Aggiunge un subcommento a un commento principale.
     *
     * @param idCommento  L'ID del commento principale.
     * @param subcommento Il subcommento da aggiungere.
     * @return Il subcommento salvato.
     * @throws IllegalArgumentException se il subcommento è nullo o contiene campi mancanti/non validi.
     */
    public Subcommento aggiungiSubcommentoACommento(String idCommento, Subcommento subcommento) {
        if (idCommento == null || idCommento.isBlank()) {
            throw new IllegalArgumentException("L'ID del commento non può essere nullo o vuoto.");
        }
        if (subcommento == null) {
            throw new IllegalArgumentException("Il subcommento non può essere nullo.");
        }
        if (subcommento.getIdAutore() == null || subcommento.getIdAutore().isBlank() ||
                subcommento.getContenuto() == null || subcommento.getContenuto().isBlank()) {
            throw new IllegalArgumentException("Il subcommento deve avere un autore e un contenuto valido.");
        }

        // Imposta i dati del subcommento
        subcommento.setDataCreazione(new Date());

        List<Forum> forums = forumRepository.findAll();
        for(Forum forum : forums) {
            List<Post> posts = forum.getPost();
            if(posts != null) {
                for(Post post : posts) {
                    List<Commento> commenti = post.getCommenti();
                    for(Commento commento : commenti) {
                        if(commento.getIdCommento()!=null && commento.getIdCommento().equals(idCommento)) {
                            List<Subcommento> subcommenti = commento.getSubcommenti();
                            subcommenti.add(subcommento);
                            commento.setSubcommenti(subcommenti);
                            post.setCommenti(commenti);
                            forum.setPost(posts);
                            forumRepository.save(forum);
                        }
                    }
                }
            }
        }

        return subcommento;
    }

    /**
     * Recupera tutti i commenti associati a un post.
     *
     * @param idPost L'ID del post.
     * @return La lista dei commenti.
     */
    public List<Commento> trovaCommentiPerPost(String idPost) {
        return commentoRepository.findByIdPost(idPost);
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

    public List<Subcommento> trovaSubcommentiPerCommento(String idCommento) {
        return commentoRepository.findById(idCommento)
                .map(Commento::getSubcommenti)
                .orElse(List.of());
    }

}