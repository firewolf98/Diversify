package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

/**
 * Repository per la gestione delle entità {@link Post} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository} per fornire metodi CRUD standard e
 * include metodi personalizzati per eseguire query specifiche.
 */

public interface PostRepository extends MongoRepository<Post, String> {

    /**
     * Trova tutti i post scritti da un autore specifico.
     *
     * @param idAutore l'ID dell'autore.
     * @return una lista di post associati all'autore specificato.
     */

    List<Post> findByIdAutore(String idAutore);

    /**
     * Trova tutti i post il cui titolo contiene una parola chiave, ignorando le maiuscole/minuscole.
     *
     * @param titolo la parola chiave da cercare nei titoli dei post.
     * @return una lista di post con titoli che contengono la parola chiave specificata.
     */

    List<Post> findByTitoloContainingIgnoreCase(String titolo);

    /**
     * Trova tutti i post creati in una data esatta.
     *
     * @param dataCreazione la data di creazione da cercare.
     * @return una lista di post creati nella data specificata.
     */

    List<Post> findByDataCreazione(Date dataCreazione);

    /**
     * Trova tutti i post creati a partire da una certa data (inclusa).
     *
     * @param dataCreazione la data di creazione minima.
     * @return una lista di post creati nella data specificata o successivamente.
     */

    List<Post> findByDataCreazioneGreaterThanEqual(Date dataCreazione);

    /**
     * Trova tutti i post creati fino a una certa data (inclusa).
     *
     * @param dataCreazione la data di creazione massima.
     * @return una lista di post creati nella data specificata o precedentemente.
     */

    List<Post> findByDataCreazioneLessThanEqual(Date dataCreazione);

    /**
     * Trova tutti i post con almeno un certo numero di "like".
     *
     * @param like il numero minimo di like richiesti.
     * @return una lista di post con un numero di like maggiore o uguale a quello specificato.
     */

    List<Post> findByLikeGreaterThanEqual(int like);
}
