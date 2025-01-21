package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Repository.ForumRepository;
import it.unisa.diversifybe.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private UtenteRepository utenteRepository;

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
     * @param ruolo booleano che indica se l'utente è un amministratore.
     * @return il forum appena aggiunto.
     * @throws SecurityException se l'utente non è un amministratore.
     */
    public Forum addForum(Forum forum, boolean ruolo) {
        if (forum==null) {
            throw new NullPointerException("Forum non può essere null.");
        }
        if (!ruolo) {
            throw new SecurityException("Solo gli amministratori possono aggiungere un forum.");
        }
        forum.setPost(new ArrayList<>());
        return forumRepository.save(forum);
    }

    /**
     * Modifica un forum esistente. Operazione riservata agli amministratori.
     *
     * @param idForum l'ID del forum da modificare.
     * @param updatedForum l'oggetto Forum con i dati aggiornati.
     * @param ruolo booleano che indica se l'utente è un amministratore.
     * @return il forum aggiornato.
     * @throws SecurityException se l'utente non è un amministratore.
     * @throws IllegalArgumentException se il forum con l'ID specificato non esiste.
     */
    public Forum updateForum(String idForum, Forum updatedForum, boolean ruolo) {
        if (!ruolo) {
            throw new SecurityException("Solo gli amministratori possono modificare un forum.");
        }

        if (idForum == null || idForum.isBlank()) {
            throw new IllegalArgumentException("Forum non trovato con ID: " + idForum);
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
     * @param ruolo booleano che indica se l'utente è un amministratore.
     * @throws SecurityException se l'utente non è un amministratore.
     */
    public void deleteForum(String idForum, boolean ruolo) {
        if (idForum == null || idForum.isBlank()) {
            throw new IllegalArgumentException("Il parametro 'idForum' non può essere nullo o vuoto.");
        }
        if (!ruolo) {
            throw new SecurityException("Solo gli amministratori possono eliminare un forum.");
        }
        forumRepository.deleteById(idForum);
    }

    /**
     * Recupera i forum associati a un determinato paese.
     *
     * @param paese il paese per cui recuperare i forum.
     * @return una lista di {@link Forum} associati al paese specificato.
     * @throws IllegalArgumentException se il parametro {@code paese} è nullo o vuoto.
     */
    public List<Forum> findForumsByPaese(String paese) {
        if (paese == null || paese.isBlank()) {
            throw new IllegalArgumentException("Il parametro 'paese' non può essere nullo o vuoto.");
        }

        return  forumRepository.findByPaese(paese);
    }

}