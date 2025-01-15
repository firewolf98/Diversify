package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.CampagnaCrowdFundingRepository;
import it.unisa.diversifybe.Repository.PaeseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per la gestione delle campagne di crowdfunding.
 * Fornisce metodi per le operazioni CRUD e per il filtraggio delle campagne.
 */
@Service
@RequiredArgsConstructor
public class CampagnaCrowdFundingService {

    private final PaeseRepository paeseRepository;
    private final CampagnaCrowdFundingRepository repository;
    private final CampagnaCrowdFundingRepository campagnaCrowdFundingRepository;

    /**
     * Restituisce tutte le campagne di crowdfunding.
     *
     * @return una lista di tutte le campagne disponibili.
     */
    public List<CampagnaCrowdFunding> getAllCampagne() {
        List<CampagnaCrowdFunding> allCampaigns = new ArrayList<>();
        List<Paese> paesi = paeseRepository.findAll();
        for (Paese paese : paesi) {
            if (paese.getCampagneCrowdfunding() != null) {
                allCampaigns.addAll(paese.getCampagneCrowdfunding());
            }
        }
        return allCampaigns;
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

        // Cerca nei documenti della collection Paese
        List<Paese> paesi = paeseRepository.findAll(); // Assumendo che esista un metodo per ottenere tutti i Paesi

        for (Paese paese : paesi) {
            if (paese.getCampagneCrowdfunding() != null) {
                for (CampagnaCrowdFunding campagna : paese.getCampagneCrowdfunding()) {
                    if (idCampagna.equals(campagna.getIdCampagna())) {
                        return Optional.of(campagna); // Restituisci la campagna trovata
                    }
                }
            }
        }

        return Optional.empty(); // Restituisci un Optional vuoto se non trovata
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

    public List<CampagnaCrowdFunding> getCampagneByDataInizio(LocalDate dataInizio) {
        if (dataInizio == null) {
            throw new IllegalArgumentException("Lo stato non può essere nullo o vuoto.");
        }
        return repository.findByDataInizio(dataInizio);
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
     * Cerca le campagne in base al Paese in cui sono state pubblicate
     *
     * @param paese il Paese in cui sono state pubblicate.
     * @return una lista di campagne pubblicate in quel Paese
     */


    public List<CampagnaCrowdFunding> findCampagneByPaese(String paese) {
        List<Paese> paesi = paeseRepository.findByNome(paese);
        List<CampagnaCrowdFunding> campagneEstratte = new ArrayList<>();

        for (Paese p : paesi) {
            if (p.getCampagneCrowdfunding() != null) {
                campagneEstratte.addAll(p.getCampagneCrowdfunding());
            }
        }

        return campagneEstratte;
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
        String paese=campagna.getPaese();
        // Trova il paese di riferimento
        Paese paeseEntity = paeseRepository.findByNome(paese).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Paese non trovato: " + paese));

        // Aggiungi la campagna alla lista
        if (paeseEntity.getCampagneCrowdfunding() == null) {
            paeseEntity.setCampagneCrowdfunding(new ArrayList<>());
        }
        paeseEntity.getCampagneCrowdfunding().add(campagna);

        // Salva il paese aggiornato
        paeseRepository.save(paeseEntity);
        return campagna;
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
        if (idCampagna == null || idCampagna.isBlank()) {
            throw new IllegalArgumentException("L'ID della campagna non può essere nullo o vuoto.");
        }

        // Cerca nei documenti della collection Paese
        List<Paese> paesi = paeseRepository.findAll();

        for (Paese paese : paesi) {
            if (paese.getCampagneCrowdfunding() != null) {
                for (CampagnaCrowdFunding campagna : paese.getCampagneCrowdfunding()) {
                    if (idCampagna.equals(campagna.getIdCampagna())) {

                        // Aggiorna i dettagli della campagna
                        campagna.setTitolo(updatedCampagna.getTitolo());
                        campagna.setDescrizione(updatedCampagna.getDescrizione());
                        campagna.setCategoria(updatedCampagna.getCategoria());
                        campagna.setDataInizio(updatedCampagna.getDataInizio());
                        campagna.setDataPrevistaFine(updatedCampagna.getDataPrevistaFine());
                        campagna.setSommaDaRaccogliere(updatedCampagna.getSommaDaRaccogliere());
                        campagna.setSommaRaccolta(updatedCampagna.getSommaRaccolta());
                        campagna.setStato(updatedCampagna.getStato());
                        campagna.setLinkDonazione(updatedCampagna.getLinkDonazione());
                        campagna.setImmagine(updatedCampagna.getImmagine());
                        campagna.setPaese(updatedCampagna.getPaese());

                        // Salva il paese aggiornato
                        paeseRepository.save(paese);
                        return campagna;
                    }
                }
            }
        }

        throw new RuntimeException("Campagna non trovata con ID: " + idCampagna);
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

        // Cerca nei documenti della collection Paese
        List<Paese> paesi = paeseRepository.findAll();

        boolean campagnaTrovata = false;

        for (Paese paese : paesi) {
            if (paese.getCampagneCrowdfunding() != null) {
                List<CampagnaCrowdFunding> campagne = paese.getCampagneCrowdfunding();

                // Rimuovi la campagna con l'ID specificato
                boolean campagnaRimossa = campagne.removeIf(campagna -> idCampagna.equals(campagna.getIdCampagna()));

                if (campagnaRimossa) {
                    // Salva il Paese aggiornato
                    paeseRepository.save(paese);
                    campagnaTrovata = true;
                    break; // Esci dal ciclo poiché la campagna è stata trovata e rimossa
                }
            }
        }

        if (!campagnaTrovata) {
            throw new RuntimeException("Campagna non trovata con ID: " + idCampagna);
        }
    }


}