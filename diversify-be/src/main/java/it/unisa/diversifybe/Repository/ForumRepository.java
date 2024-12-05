package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Forum;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository per la gestione delle entità {@link Forum} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository}, fornendo operazioni CRUD standard per le entità di tipo {@link Forum}.
 * I metodi personalizzati definiti in questa interfaccia richiedono un'implementazione esplicita.
 */

public interface ForumRepository extends MongoRepository<Forum, String>{

    /**
     * Aggiunge un nuovo forum al database.
     *
     * @param forum il forum da aggiungere.
     */

    void addForum(Forum forum);

    /**
     * Recupera un forum dal database tramite il suo ID.
     *
     * @param id l'ID del forum da recuperare.
     * @return il forum corrispondente all'ID fornito, oppure {@code null} se non trovato.
     */

    Forum getForumById(String id);

    /**
     * Conta il numero di post presenti in un forum specifico.
     *
     * @param forum il forum di cui contare i post.
     * @return il numero di post presenti nel forum.
     */

    int getNPost(Forum forum);

    /**
     * Aggiorna un forum esistente nel database.
     *
     * @param forum il forum da aggiornare con i nuovi dati.
     */

    void updateForum(Forum forum);

    /**
     * Cancella un forum dal database tramite il suo ID.
     *
     * @param id l'ID del forum da cancellare.
     */

    void deleteForumById(String id);
}
