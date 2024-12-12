package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Repository per la gestione delle entità {@link Utente} nel database MongoDB.
 * <p>
 * Estende {@link MongoRepository} per fornire metodi CRUD standard e
 * include metodi personalizzati per query specifiche sugli utenti.
 */

public interface UtenteRepository extends MongoRepository<Utente, String> {

    /**
     * Trova un utente in base al suo username.
     *
     * @param username lo username dell'utente.
     * @return un {@link Optional} che contiene l'utente corrispondente, se esiste.
     */

    Optional<Utente> findByUsername(String username);

    /**
     * Trova un utente in base alla sua email.
     *
     * @param email l'email dell'utente.
     * @return un {@link Optional} che contiene l'utente corrispondente, se esiste.
     */

    Optional<Utente> findByEmail(String email);


    /**
     * Trova un utente in base alla risposta alla domanda personale (in formato hash).
     *
     * @param rispostaHash l'hash della risposta alla domanda personale dell'utente.
     * @return un {@link Optional} che contiene l'utente corrispondente, se esiste.
     */

    Optional<Utente> findByRispostaHash(String rispostaHash);

}
