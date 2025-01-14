package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia repository per la gestione delle entità {@link CampagnaCrowdFunding}
 * nel database MongoDB. Fornisce metodi per eseguire operazioni CRUD e query personalizzate.
 * <p>
 * Estende {@link MongoRepository}, che include operazioni standard per MongoDB.
 */

public interface CampagnaCrowdFundingRepository extends MongoRepository<CampagnaCrowdFunding, String> {

    /**
     * Trova una lista di campagne con l'ID specificato.
     *
     * @param idCampagna l'ID univoco della campagna.
     * @return una lista di campagne con l'ID specificato.
     */

    List<CampagnaCrowdFunding> findByIdCampagna(String idCampagna);

    /**
     * Trova una lista di campagne con la data di inizio specificata.
     *
     * @param dataInizio la data di inizio della campagna.
     * @return una lista di campagne che iniziano nella data specificata.
     */

    List<CampagnaCrowdFunding> findByDataInizio(LocalDate dataInizio);

    /**
     * Trova una lista di campagne con il titolo esattamente corrispondente.
     *
     * @param titolo il titolo esatto della campagna.
     * @return una lista di campagne con il titolo specificato.
     */

    List<CampagnaCrowdFunding> findByTitolo(String titolo);

    /**
     * Trova una lista di campagne il cui titolo contiene una parola chiave specifica.
     *
     * @param keyword la parola chiave da cercare nei titoli delle campagne.
     * @return una lista di campagne con titoli che contengono la parola chiave specificata.
     */

    List<CampagnaCrowdFunding> findByTitoloContaining(String keyword);

    /**
     * Trova una lista di campagne che terminano in una data prevista specifica.
     *
     * @param dataPrevistaFine la data prevista di fine della campagna.
     * @return una lista di campagne con la data prevista di fine specificata.
     */

    List<CampagnaCrowdFunding> findByDataPrevistaFine(LocalDate dataPrevistaFine);

    /**
     * Trova una lista di campagne che terminano in una data prevista specifica o successiva.
     *
     * @param dataPrevistaFine la data prevista di fine della campagna.
     * @return una lista di campagne con data prevista di fine uguale o successiva a quella specificata.
     */

    List<CampagnaCrowdFunding> findByDataPrevistaFineGreaterThanEqual(LocalDate dataPrevistaFine);

    /**
     * Trova una lista di campagne con lo stato specificato.
     *
     * @param stato lo stato della campagna (ad esempio: "attiva", "completata").
     * @return una lista di campagne con lo stato specificato.
     */

    List<CampagnaCrowdFunding> findByStato(String stato);

    /**
     * Trova una lista di campagne in base alla categoria della campagna.
     *
     * @param keyword la categoria della campagna (ad esempio: "Sostenibilità").
     * @return una lista di campagne con la categoria specificata.
     */

    List<CampagnaCrowdFunding> findByCategoria(String keyword);

    @Query("{ 'Paese': ?0 }")
    List<CampagnaCrowdFunding> findCampagneByPaese(String paese);
}
