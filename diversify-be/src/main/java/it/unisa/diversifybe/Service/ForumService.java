package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Classe di servizio per la gestione dei forum.
 * Fornisce metodi per recuperare, aggiungere, modificare ed eliminare forum.
 * Alcune operazioni sono riservate agli amministratori.
 */

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    /**
     * Recupera tutti i forum.
     *
     * @return una lista di tutti i forum disponibili.
     */

    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    /**
     * Recupera un forum tramite il suo ID.
     *
     * @param idForum l'ID del forum da recuperare.
     * @return un oggetto Optional contenente il forum se trovato, altrimenti vuoto.
     */

    public Optional<Forum> getForumById(String idForum) {
        return forumRepository.findById(idForum);
    }

    /**
     * Aggiunge un nuovo forum. Operazione riservata agli amministratori.
     *
     * @param forum l'oggetto Forum da aggiungere.
     * @param isAdmin booleano che indica se l'utente è un amministratore.
     * @return il forum appena aggiunto.
     * @throws SecurityException se l'utente non è un amministratore.
     */

    public Forum addForum(Forum forum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono aggiungere un forum.");
        }
        return forumRepository.save(forum);
    }

    /**
     * Modifica un forum esistente. Operazione riservata agli amministratori.
     *
     * @param idForum l'ID del forum da modificare.
     * @param updatedForum l'oggetto Forum con i dati aggiornati.
     * @param isAdmin booleano che indica se l'utente è un amministratore.
     * @return il forum aggiornato.
     * @throws SecurityException se l'utente non è un amministratore.
     * @throws IllegalArgumentException se il forum con l'ID specificato non esiste.
     */

    public Forum updateForum(String idForum, Forum updatedForum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono modificare un forum.");
        }

        return forumRepository.findById(idForum).map(forum -> {
            forum.setTitolo(updatedForum.getTitolo());
            forum.setDescrizione(updatedForum.getDescrizione());
            forum.setPost(updatedForum.getPost());
            return forumRepository.save(forum);
        }).orElseThrow(() -> new IllegalArgumentException("Forum non trovato con ID: " + idForum));
    }

    /**
     * Elimina un forum. Operazione riservata agli amministratori.
     *
     * @param idForum l'ID del forum da eliminare.
     * @param isAdmin booleano che indica se l'utente è un amministratore.
     * @throws SecurityException se l'utente non è un amministratore.
     */

    public void deleteForum(String idForum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono eliminare un forum.");
        }
        forumRepository.deleteById(idForum);
    }
}