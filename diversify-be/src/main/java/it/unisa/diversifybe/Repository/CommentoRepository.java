package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Commento;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interfaccia repository per la gestione delle entità {@link Commento} nel database MongoDB.
 * Fornisce metodi per eseguire operazioni CRUD e query personalizzate.
 * <p>
 * Estende {@link MongoRepository}, che include operazioni standard per MongoDB.
 */

public interface CommentoRepository extends MongoRepository<Commento,String> {
    Optional<Commento> findByIdAutore(String idAutore);
    /**
     * Trova una lista di commenti creati da un autore specifico.
     *
     * @param idCommento l'ID univoco dell'autore del commento.
     * @return una lista di commenti associati all'ID autore specificato.
     */

    Optional<Commento> findByIdCommento(String idCommento);

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
     * @param dataCreazione la data di creazione del commento.
     * @return una lista di commenti creati nella data specificata.
     */

    List<Commento> findByDataCreazioneIs(Date dataCreazione);

    /**
     * Trova una lista di commenti creati in una data specifica o successiva.
     *
     * @param dataCreazione la data minima di creazione del commento.
     * @return una lista di commenti creati nella data specificata o successiva.
     */

    List<Commento> findByDataCreazioneGreaterThanEqual(Date dataCreazione);

    /**
     * Trova una lista di commenti creati in una data specifica o precedente.
     *
     * @param dataCreazione la data massima di creazione del commento.
     * @return una lista di commenti creati nella data specificata o precedente.
     */

    List<Commento> findByDataCreazioneLessThanEqual(Date dataCreazione);

    /**
     * Trova una lista di commenti associati a un determinato post.
     *
     * @param idPost l'ID del post.
     * @return una lista di commenti associati al post specificato.
     */

    List<Commento> findByIdPost(String idPost);
}