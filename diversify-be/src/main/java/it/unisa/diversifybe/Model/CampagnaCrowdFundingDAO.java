package it.unisa.diversifybe.Model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia DAO (Data Access Object) per la gestione delle entità {@link CampagnaCrowdFunding}
 * nel database MongoDB. Fornisce metodi per eseguire operazioni di lettura e query personalizzate.
 * <p>
 * Estende {@link MongoRepository}, che include operazioni CRUD standard per MongoDB.
 */
public interface CampagnaCrowdFundingDAO extends MongoRepository<CampagnaCrowdFunding, String> {

    /**
     * Restituisce una lista di campagne che corrispondono all'ID specificato.
     *
     * @param id_campagna l'ID univoco della campagna.
     * @return una lista di campagne con l'ID specificato.
     */
    List<CampagnaCrowdFunding> findById_campagna(String id_campagna);

    /**
     * Restituisce una lista di campagne che corrispondono esattamente al titolo specificato.
     *
     * @param titolo il titolo esatto della campagna.
     * @return una lista di campagne con il titolo specificato.
     */
    List<CampagnaCrowdFunding> findByTitolo(String titolo);

    /**
     * Restituisce una lista di campagne il cui titolo contiene la parola chiave specificata.
     *
     * @param keyword la parola chiave da cercare nei titoli delle campagne.
     * @return una lista di campagne con titoli che contengono la parola chiave specificata.
     */
    List<CampagnaCrowdFunding> findByTitoloContains(String keyword);

    /**
     * Restituisce una lista di campagne che iniziano in una data specifica.
     *
     * @param data_inizio la data di inizio della campagna.
     * @return una lista di campagne con la data di inizio specificata.
     */
    List<CampagnaCrowdFunding> findByData_inizio(LocalDate data_inizio);

    /**
     * Restituisce una lista di campagne che iniziano in una data specificata o successiva.
     *
     * @param data_inizio la data di inizio della campagna.
     * @return una lista di campagne con data di inizio uguale o successiva a quella specificata.
     */
    List<CampagnaCrowdFunding> findByData_inizioGreaterThanEqual(LocalDate data_inizio);

    /**
     * Restituisce una lista di campagne che terminano in una data prevista specifica.
     *
     * @param data_prevista_fine la data prevista di fine della campagna.
     * @return una lista di campagne con la data prevista di fine specificata.
     */
    List<CampagnaCrowdFunding> findByData_prevista_fine(LocalDate data_prevista_fine);

    /**
     * Restituisce una lista di campagne che terminano in una data prevista specifica o successiva.
     *
     * @param data_prevista_fine la data prevista di fine della campagna.
     * @return una lista di campagne con data prevista di fine uguale o successiva a quella specificata.
     */
    List<CampagnaCrowdFunding> findByData_prevista_fineGreaterThanEqual(LocalDate data_prevista_fine);

    /**
     * Restituisce una lista di campagne in base allo stato specificato.
     *
     * @param stato lo stato della campagna (ad esempio: "attiva", "completata").
     * @return una lista di campagne con lo stato specificato.
     */
    List<CampagnaCrowdFunding> findByStato(String stato);

}
