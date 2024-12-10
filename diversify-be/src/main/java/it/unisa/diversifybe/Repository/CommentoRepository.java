package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Commento;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

/**
 * Interfaccia repository per la gestione delle entità {@link Commento} nel database MongoDB.
 * Fornisce metodi per eseguire operazioni CRUD e query personalizzate.
 * <p>
 * Estende {@link MongoRepository}, che include operazioni standard per MongoDB.
 */

public interface CommentoRepository extends MongoRepository<Commento,String> {

    /**
     * Trova una lista di commenti creati da un autore specifico.
     *
     * @param id_autore l'ID univoco dell'autore del commento.
     * @return una lista di commenti associati all'ID autore specificato.
     */

    List<Commento> findById_autore(String id_autore);

    /**
     * Trova una lista di commenti il cui contenuto contiene una parola chiave specifica.
     *
     * @param keyword la parola chiave da cercare nel contenuto dei commenti.
     * @return una lista di commenti il cui contenuto contiene la parola chiave specificata.
     */

    List<Commento> findByContenutoContaining(String keyword);

    /**
     * Trova una lista di commenti creati in una data specifica.
     *
     * @param data_creazione la data di creazione del commento.
     * @return una lista di commenti creati nella data specificata.
     */

    List<Commento> findByData_creazioneIs(Date data_creazione);

    /**
     * Trova una lista di commenti creati in una data specifica o successiva.
     *
     * @param data_creazione la data minima di creazione del commento.
     * @return una lista di commenti creati nella data specificata o successiva.
     */

    List<Commento> findByData_creazioneGreaterThanEqual(Date data_creazione);

    /**
     * Trova una lista di commenti creati in una data specifica o precedente.
     *
     * @param data_creazione la data massima di creazione del commento.
     * @return una lista di commenti creati nella data specificata o precedente.
     */

    List<Commento> findByData_creazioneSmallerThanEqual(Date data_creazione);
}