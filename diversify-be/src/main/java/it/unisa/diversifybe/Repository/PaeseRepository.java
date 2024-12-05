package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Paese;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Repository per la gestione delle entità {@link Paese} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository}, fornendo operazioni CRUD standard per le entità di tipo {@link Paese}.
 * Include metodi personalizzati per query specifiche basate sui campi del modello.
 */

public interface PaeseRepository extends MongoRepository<Paese, String> {

    /**
     * Trova i paesi in base al nome esatto.
     *
     * @param nome il nome esatto del paese da cercare.
     * @return una lista di paesi con il nome specificato.
     */

    List<Paese> findByNome(String nome);

    /**
     * Trova i paesi che contengono una specifica parola chiave nel nome.
     *
     * @param keyword la parola chiave da cercare nel nome dei paesi.
     * @return una lista di paesi con nomi che contengono la parola chiave specificata.
     */

    List<Paese> findByNomeContaining(String keyword);

    /**
     * Trova i paesi che sono associati a un forum specifico.
     *
     * @param forumID l'ID del forum associato al paese.
     * @return una lista di paesi con il forum specificato.
     */

    List<Paese> findByForum(String forumID);

    /**
     * Trova i paesi che contengono una specifica campagna di crowdfunding.
     *
     * @param campagnaID l'ID della campagna di crowdfunding.
     * @return una lista di paesi che includono la campagna di crowdfunding specificata.
     */

    List<Paese> findByCampagne_crowdfundingContaining(String campagnaID);

    /**
     * Trova i paesi che hanno un benchmark di un tipo specifico.
     *
     * @param tipo il tipo di benchmark da cercare.
     * @return una lista di paesi che includono il benchmark con il tipo specificato.
     */

    List<Paese> findByBenchmark_Tipo(String tipo);

    /**
     * Trova i paesi che includono un documento informativo con un titolo specifico.
     *
     * @param titoloDocumento il titolo del documento informativo da cercare.
     * @return una lista di paesi che includono un documento con il titolo specificato.
     */

    List<Paese> findByDocumenti_informativi_Titolo(String titoloDocumento);
}
