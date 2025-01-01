package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Repository.CampagnaCrowdFundingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service per la gestione delle campagne di crowdfunding.
 * Fornisce metodi per le operazioni CRUD e per il filtraggio delle campagne.
 */
@Service
@RequiredArgsConstructor
public class CampagnaCrowdFundingService {

    private final CampagnaCrowdFundingRepository repository;
    private final CampagnaCrowdFundingRepository campagnaCrowdFundingRepository;

    /**
     * Restituisce tutte le campagne di crowdfunding.
     *
     * @return una lista di tutte le campagne disponibili.
     */
    public List<CampagnaCrowdFunding> getAllCampagne() {
        return repository.findAll();
    }

    /**
     * Cerca una campagna di crowdfunding in base al suo ID.
     *
     * @param idCampagna l'ID della campagna.
     * @return un Optional contenente la campagna se trovata, altrimenti vuoto.
     */


    public Optional<CampagnaCrowdFunding> getCampagnaByIdCampagna(String idCampagna) {
        if (idCampagna == null || idCampagna.isBlank()) {
            throw new IllegalArgumentException("L'ID della campagna non può essere nullo o vuoto.");
        }
        return repository.findByIdCampagna(idCampagna).stream().findFirst();
    }

    /**
     * Restituisce tutte le campagne con un titolo esatto specificato.
     *
     * @param titolo il titolo della campagna.
     * @return una lista di campagne con il titolo specificato.
     */

    public List<CampagnaCrowdFunding> getCampagneByTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Il titolo non può essere nullo o vuoto.");
        }
        return repository.findByTitolo(titolo);
    }


    /**
     * Cerca le campagne che contengono una parola chiave nel titolo.
     *
     * @param keyword la parola chiave da cercare nel titolo.
     * @return una lista di campagne che contengono la parola chiave.
     */

    public List<CampagnaCrowdFunding> getCampagneByTitoloContaining(String keyword) {
        return repository.findByTitoloContaining(keyword);
    }

    /**
     * Cerca le campagne in base al loro stato.
     *
     * @param stato lo stato della campagna.
     * @return una lista di campagne con lo stato specificato.
     */

    public List<CampagnaCrowdFunding> getCampagneByStato(String stato) {
        if (stato == null || stato.isBlank()) {
            throw new IllegalArgumentException("Lo stato non può essere nullo o vuoto.");
        }
        return repository.findByStato(stato);
    }


    /**
     * Cerca le campagne in base alla data di inizio.
     *
     * @param dataInizio la data di inizio della campagna.
     * @return una lista di campagne con la data di inizio specificata.
     */

    public List<CampagnaCrowdFunding> getCampagneByDataInizio(LocalDate dataInizio) {
        if (dataInizio == null) {
            throw new IllegalArgumentException("La data di inizio non può essere nulla.");
        }
        return repository.findByDataInizio(dataInizio);
    }

    public List<CampagnaCrowdFunding> findCampagneByPaese(String paese) {
        return campagnaCrowdFundingRepository.findByPaese(paese);
    }
    /**
     * Cerca le campagne in base alla data prevista di fine.
     *
     * @param dataPrevistaFine la data prevista di fine della campagna.
     * @return una lista di campagne con la data di fine prevista specificata.
     */

    public List<CampagnaCrowdFunding> getCampagneByDataPrevistaFine(LocalDate dataPrevistaFine) {
        if (dataPrevistaFine == null) {
            throw new IllegalArgumentException("La data prevista di fine non può essere nulla.");
        }
        return repository.findByDataPrevistaFine(dataPrevistaFine);
    }


    /**
     * Crea una nuova campagna di crowdfunding.
     *
     * @param campagna i dati della nuova campagna.
     * @return la campagna creata.
     */

    public CampagnaCrowdFunding createCampagna(CampagnaCrowdFunding campagna) {
        if (campagna == null || campagna.getTitolo() == null || campagna.getTitolo().isBlank()) {
            throw new IllegalArgumentException("I dati della campagna non possono essere nulli o incompleti.");
        }
        return repository.save(campagna);
    }


    /**
     * Aggiorna una campagna esistente.
     *
     * @param idCampagna      l'ID della campagna da aggiornare.
     * @param updatedCampagna i dati aggiornati della campagna.
     * @return la campagna aggiornata.
     * @throws RuntimeException se la campagna con l'ID specificato non viene trovata.
     */

    public CampagnaCrowdFunding updateCampagna(String idCampagna, CampagnaCrowdFunding updatedCampagna) {
        return repository.findByIdCampagna(idCampagna).stream().findFirst()
                .map(existingCampagna -> {
                    existingCampagna.setTitolo(updatedCampagna.getTitolo());
                    existingCampagna.setContenuto(updatedCampagna.getContenuto());
                    existingCampagna.setImages(updatedCampagna.getImages());
                    existingCampagna.setDataInizio(updatedCampagna.getDataInizio());
                    existingCampagna.setDataPrevistaFine(updatedCampagna.getDataPrevistaFine());
                    existingCampagna.setSommaDaRaccogliere(updatedCampagna.getSommaDaRaccogliere());
                    existingCampagna.setSommaRaccolta(updatedCampagna.getSommaRaccolta());
                    existingCampagna.setStato(updatedCampagna.getStato());
                    return repository.save(existingCampagna);
                }).orElseThrow(() -> new RuntimeException("Campagna non trovata con ID: " + idCampagna));
    }

    /**
     * Elimina una campagna specifica in base al suo ID.
     *
     * @param idCampagna l'ID della campagna da eliminare.
     * @throws RuntimeException se la campagna con l'ID specificato non viene trovata.
     */

    public void deleteCampagna(String idCampagna) {
        if (idCampagna == null || idCampagna.isBlank()) {
            throw new IllegalArgumentException("L'ID della campagna non può essere nullo o vuoto.");
        }

        Optional<CampagnaCrowdFunding> campagna = repository.findByIdCampagna(idCampagna).stream().findFirst();
        if (campagna.isPresent()) {
            repository.delete(campagna.get());
        } else {
            throw new RuntimeException("Campagna non trovata con ID: " + idCampagna);
        }
    }

}