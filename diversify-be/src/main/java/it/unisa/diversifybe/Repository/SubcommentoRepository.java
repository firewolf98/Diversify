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
     * @param id_autore l'ID dell'autore del subcommento.
     * @return una lista di subcommenti creati dall'autore specificato.
     */

    List<Subcommento> findById_autore(String id_autore);

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
     * @param data_creazione la data esatta di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata.
     */

    List<Subcommento> findByData_creazioneIs(Date data_creazione);

    /**
     * Trova tutti i subcommenti creati in una data specifica o successiva.
     *
     * @param data_creazione la data minima di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata o successivamente.
     */

    List<Subcommento> findByData_creazioneGreaterThanEqual(Date data_creazione);

    /**
     * Trova tutti i subcommenti creati in una data specifica o precedente.
     *
     * @param data_creazione la data massima di creazione dei subcommenti.
     * @return una lista di subcommenti creati nella data specificata o precedentemente.
     */

    List<Subcommento> findByData_creazioneSmallerThanEqual(Date data_creazione);
}