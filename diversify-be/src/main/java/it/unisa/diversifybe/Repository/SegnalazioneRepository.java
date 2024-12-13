package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Segnalazione;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

/**
 * Repository per la gestione delle entità {@link Segnalazione} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository} per fornire metodi CRUD standard e
 * include metodi personalizzati per query specifiche.
 */

public interface SegnalazioneRepository extends MongoRepository<Segnalazione, Object> {

    /**
     * Trova una segnalazione in base al suo ID.
     *
     * @param idSegnalazione l'ID della segnalazione.
     * @return la segnalazione corrispondente all'ID specificato, o null se non trovata.
     */

    List<Segnalazione> findByIdSegnalazione(String idSegnalazione);

    /**
     * Trova tutte le segnalazioni effettuate da un utente specifico.
     *
     * @param idSegnalante l'ID dell'utente che ha effettuato le segnalazioni.
     * @return una lista di segnalazioni effettuate dall'utente specificato.
     */

    List<Segnalazione> findByIdSegnalante(String idSegnalante);

    /**
     * Trova tutte le segnalazioni che coinvolgono un utente segnalato specifico.
     *
     * @param idSegnalato l'ID dell'utente segnalato.
     * @return una lista di segnalazioni che coinvolgono l'utente specificato.
     */

    List<Segnalazione> findByIdSegnalato(String idSegnalato);

    /**
     * Trova tutte le segnalazioni con una motivazione specifica.
     *
     * @param motivazione la motivazione della segnalazione.
     * @return una lista di segnalazioni con la motivazione specificata.
     */

    List<Segnalazione> findByMotivazione(String motivazione);

    /**
     * Trova tutte le segnalazioni effettuate in una data specifica.
     *
     * @param dataSegnalazione la data della segnalazione.
     * @return una lista di segnalazioni effettuate nella data specificata.
     */

    List<Segnalazione> findByDataSegnalazione(Date dataSegnalazione);

    /**
     * Trova tutte le segnalazioni con un tipo specifico.
     *
     * @param tipoSegnalazione il tipo della segnalazione (ad esempio: "abuso", "spam").
     * @return una lista di segnalazioni con il tipo specificato.
     */

    List<Segnalazione> findByTipoSegnalazione(String tipoSegnalazione);

    /**
     * Trova tutte le segnalazioni effettuate a partire da una data specifica (inclusa).
     *
     * @param dataSegnalazione la data minima della segnalazione.
     * @return una lista di segnalazioni effettuate nella data specificata o successivamente.
     */

    List<Segnalazione> findByDataSegnalazioneGreaterThanEqual(Date dataSegnalazione);
}
