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
     * Questo metodo cerca un paese utilizzando l'ID specificato. Se l'ID è nullo o vuoto,
     * viene sollevata un'eccezione {@link IllegalArgumentException}.
     *
     * @param id l'ID del paese da cercare.
     * @return un {@link Optional} contenente il paese trovato, o vuoto se non esiste.
     * @throws IllegalArgumentException se l'ID è nullo o vuoto.
     */
    public Optional<Paese> getPaeseById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID del paese non può essere nullo o vuoto.");
        }
        return paeseRepository.findById(id);
    }

    /**
     * Crea un nuovo paese.
     * Questo metodo crea un nuovo paese nella piattaforma. Se i dati del paese sono incompleti o nulli,
     * viene sollevata un'eccezione {@link IllegalArgumentException}. Inoltre, verifica se il paese con
     * lo stesso ID esiste già e in quel caso solleva un'eccezione {@link IllegalStateException}.
     *
     * @param paese il paese da creare.
     * @return il paese creato.
     * @throws IllegalArgumentException se i dati del paese sono nulli o incompleti.
     * @throws IllegalStateException se il paese con lo stesso ID esiste già.
     */
    public Paese createPaese(Paese paese) {
        if (paese == null || paese.getIdPaese() == null || paese.getIdPaese().isBlank() || paese.getNome() == null || paese.getNome().isBlank()) {
            throw new IllegalArgumentException("I dati del paese non possono essere nulli o incompleti.");
        }
        if (paeseRepository.existsById(paese.getIdPaese())) {
            throw new IllegalStateException("Il paese con ID " + paese.getIdPaese() + " esiste già.");
        }
        return paeseRepository.save(paese);
    }

    /**
     * Aggiorna un paese esistente.
     * Questo metodo aggiorna i dati di un paese nella piattaforma. Se l'ID è nullo o vuoto,
     * o se i dati aggiornati del paese sono incompleti o nulli, viene sollevata un'eccezione
     * {@link IllegalArgumentException}. Se il paese specificato non esiste, restituisce un
     * {@link Optional} vuoto.
     *
     * @param id           l'ID del paese da aggiornare.
     * @param updatedPaese i dati aggiornati del paese.
     * @return un Optional contenente il paese aggiornato, o vuoto se non esiste.
     * @throws IllegalArgumentException se l'ID o i dati aggiornati del paese sono nulli o incompleti.
     */
    public Optional<Paese> updatePaese(String id, Paese updatedPaese) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID del paese non può essere nullo o vuoto.");
        }
        if (updatedPaese == null || updatedPaese.getNome() == null || updatedPaese.getNome().isBlank()) {
            throw new IllegalArgumentException("I dati aggiornati del paese non possono essere nulli o incompleti.");
        }

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
     * Questo metodo verifica se l'ID è nullo o vuoto e solleva un'eccezione {@link IllegalArgumentException}.
     * Se il paese esiste, viene eliminato. In caso contrario, non viene eseguita alcuna operazione.
     *
     * @param id l'ID del paese da eliminare.
     * @throws IllegalArgumentException se l'ID è nullo o vuoto.
     * @throws IllegalStateException se il paese con l'ID specificato non esiste.
     */
    public void deletePaese(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID del paese non può essere nullo o vuoto.");
        }

        if (!paeseRepository.existsById(id)) {
            throw new IllegalStateException("Il paese con ID " + id + " non esiste.");
        }

        paeseRepository.deleteById(id);
    }

    /**
     * Restituisce i paesi associati a un forum specifico.
     *<p>
     * Questo metodo verifica se l'ID del forum è nullo o vuoto e solleva un'eccezione {@link IllegalArgumentException}.
     * Restituisce una lista di paesi associati al forum specificato.
     *
     * @param idForum l'ID del forum.
     * @return una lista di {@link Paese} associati al forum specificato.
     * @throws IllegalArgumentException se l'ID del forum è nullo o vuoto.
     */
    public List<Paese> findPaesiByForum(String idForum) {
        if (idForum == null || idForum.isBlank()) {
            throw new IllegalArgumentException("L'ID del forum non può essere nullo o vuoto.");
        }

        return paeseRepository.findAll().stream()
                .filter(p -> p.getForum() != null && p.getForum().contains(idForum))
                .collect(Collectors.toList());
    }
    /**
     * Restituisce i paesi associati a una campagna di crowdfunding specifica.
     *<p>
     * Questo metodo verifica se l'ID della campagna è nullo o vuoto e solleva un'eccezione {@link IllegalArgumentException}.
     * Restituisce una lista di paesi associati alla campagna specificata.
     *
     * @param idCampagna l'ID della campagna di crowdfunding.
     * @return una lista di {@link Paese} associati alla campagna specificata.
     * @throws IllegalArgumentException se l'ID della campagna è nullo o vuoto.
     */
    public List<Paese> findPaesiByCampagna(String idCampagna) {
        if (idCampagna == null || idCampagna.isBlank()) {
            throw new IllegalArgumentException("L'ID della campagna non può essere nullo o vuoto.");
        }

        return paeseRepository.findAll().stream()
                .filter(p -> p.getCampagneCrowdfunding() != null && p.getCampagneCrowdfunding().contains(idCampagna))
                .collect(Collectors.toList());
    }

    /**
     * Restituisce i paesi basati su un determinato benchmark.
     * Questo metodo cerca tutti i paesi che contengono un benchmark con un ID corrispondente
     * al valore specificato. Se l'ID è nullo o vuoto, viene sollevata un'eccezione.
     *
     * @param idBenchmark il valore del benchmark da cercare.
     * @return una lista di {@link Paese} associati al benchmark specificato.
     * @throws IllegalArgumentException se l'idBenchmark è nullo o vuoto.
     */
    public List<Paese> findPaesiByBenchmark(String idBenchmark) {
        if (idBenchmark == null || idBenchmark.isBlank()) {
            throw new IllegalArgumentException("L'ID del benchmark non può essere nullo o vuoto.");
        }
        return paeseRepository.findAll().stream()
                .filter(p -> p.getBenchmark() != null &&
                        p.getBenchmark().stream()
                                .anyMatch(b -> b.getIdBenchmark().equals(idBenchmark))) // Confronta con gli ID dei benchmark
                .collect(Collectors.toList());
    }

}
