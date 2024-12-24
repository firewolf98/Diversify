package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaeseService {

    @Autowired
    private PaeseRepository paeseRepository;

    /**
     * Restituisce tutti i paesi.
     *
     * @return una lista di {@link Paese}.
     */
    public List<Paese> getAllPaesi() {
        return paeseRepository.findAll();
    }

    /**
     * Trova un paese specifico dato il suo ID.
     *
     * @param id l'ID del paese da cercare.
     * @return un Optional contenente il paese trovato, o vuoto se non esiste.
     */
    public Optional<Paese> getPaeseById(String id) {
        return paeseRepository.findById(id);
    }

    /**
     * Crea un nuovo paese.
     *
     * @param paese il paese da creare.
     * @return il paese creato.
     */
    public Paese createPaese(Paese paese) {
        return paeseRepository.save(paese);
    }

    /**
     * Aggiorna un paese esistente.
     *
     * @param id           l'ID del paese da aggiornare.
     * @param updatedPaese i dati aggiornati del paese.
     * @return un Optional contenente il paese aggiornato, o vuoto se non esiste.
     */
    public Optional<Paese> updatePaese(String id, Paese updatedPaese) {
        Optional<Paese> existingPaese = paeseRepository.findById(id);
        if (existingPaese.isPresent()) {
            Paese paese = existingPaese.get();
            paese.setNome(updatedPaese.getNome());
            paese.setForum(updatedPaese.getForum());
            paese.setCampagneCrowdfunding(updatedPaese.getCampagneCrowdfunding());
            paese.setBenchmark(updatedPaese.getBenchmark());
            paese.setLinkImmagineBandiera(updatedPaese.getLinkImmagineBandiera());
            paese.setDocumentiInformativi(updatedPaese.getDocumentiInformativi());
            return Optional.of(paeseRepository.save(paese));
        }
        return Optional.empty();
    }

    /**
     * Elimina un paese dato il suo ID.
     *
     * @param id l'ID del paese da eliminare.
     */
    public void deletePaese(String id) {
        if (paeseRepository.existsById(id)) {
            paeseRepository.deleteById(id);
        }
    }

    /**
     * Restituisce i paesi associati a un forum specifico.
     *
     * @param idForum l'ID del forum.
     * @return una lista di {@link Paese} associati.
     */

    public List<Paese> findPaesiByForum(String idForum) {
        return paeseRepository.findAll().stream()
                .filter(p -> p.getForum() != null &&
                        p.getForum().contains(idForum))  // Verifica se l'ID del forum è presente nella lista
                .collect(Collectors.toList());
    }


    /**
     * Restituisce i paesi associati a una campagna di crowdfunding specifica.
     *
     * @param idCampagna l'ID della campagna di crowdfunding.
     * @return una lista di {@link Paese} associati.
     */
    public List<Paese> findPaesiByCampagna(String idCampagna) {
        return paeseRepository.findAll().stream()
                .filter(p -> p.getCampagneCrowdfunding() != null &&
                        p.getCampagneCrowdfunding().contains(idCampagna))  // Controlla se la lista contiene l'idCampagna
                .collect(Collectors.toList());
    }

    /**
     * Restituisce i paesi basati su un determinato benchmark.
     *
     * @param idBenchmark il valore del benchmark da cercare.
     * @return una lista di {@link Paese} associati al benchmark specificato.
     */
    public List<Paese> findPaesiByBenchmark(String idBenchmark) {
        return paeseRepository.findAll().stream()
                .filter(p -> p.getBenchmark() != null &&
                        p.getBenchmark().contains(idBenchmark))  // Verifica se l'ID del benchmark è presente nella lista
                .collect(Collectors.toList());
    }
}
