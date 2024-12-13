package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Subcommento;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

/**
 * Repository per la gestione delle entità {@link Subcommento} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository} per fornire metodi CRUD standard e
 * include metodi personalizzati per query specifiche sui subcommenti.
 */

public interface SubcommentoRepository extends MongoRepository<Subcommento,String> {

    /**
     * Trova tutti i subcommenti scritti da un autore specifico.
     *
     * @param idAutore l'ID dell'autore del subcommento.
     * @return una lista di subcommenti creati dall'autore specificato.
     */

    List<Subcommento> findByIdAutore(String idAutore);

    /**
     * Trova tutti i subcommenti che contengono una parola chiave specificata nel contenuto.
     *
     * @param keyword la parola chiave da cercare nel contenuto dei subcommenti.
     * @return una lista di subcommenti che contengono la parola chiave specificata.
     */

    List<Subcommento> findByContenutoContaining(String keyword);

    /**
     * Trova tutti i subcommenti creati in una data specifica.
     *
     * @param dataCreazione la data esatta di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata.
     */

    List<Subcommento> findByDataCreazioneIs(Date dataCreazione);

    /**
     * Trova tutti i subcommenti creati in una data specifica o successiva.
     *
     * @param dataCreazione la data minima di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata o successivamente.
     */

    List<Subcommento> findByDataCreazioneGreaterThanEqual(Date dataCreazione);

    /**
     * Trova tutti i subcommenti creati in una data specifica o precedente.
     *
     * @param dataCreazione la data massima di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata o precedentemente.
     */

    List<Subcommento> findByDataCreazioneLessThanEqual(Date dataCreazione);
}