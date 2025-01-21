package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Forum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository per la gestione delle entità {@link Forum} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository}, fornendo operazioni CRUD standard per le entità di tipo {@link Forum}.
 * I metodi personalizzati definiti in questa interfaccia richiedono un'implementazione esplicita.
 */

public interface ForumRepository extends MongoRepository<Forum, String>{
    /**
     * Recupera un forum dal database tramite il suo ID.
     *
     * @param idForum l'ID del forum da recuperare.
     * @return il forum corrispondente all'ID fornito, oppure {@code null} se non trovato.
     */

    List<Forum> findByIdForum(String idForum);

    /**
     * Conta il numero di post presenti in un forum specifico.
     *
     * @param post il forum di cui contare i post.
     * @return il numero di post presenti nel forum.
     */

    List<Forum> findByPostContaining(Forum post);

    List<Forum> findByPaese(String paese);
}

