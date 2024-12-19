package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST per la gestione delle operazioni relative ai Paesi.
 * Espone endpoint per ottenere, creare, aggiornare e cancellare i dati sui Paesi.
 *
 * - Gli endpoint `GET` sono pubblici e accessibili a tutti gli utenti.
 * - Gli endpoint `POST`, `PUT` e `DELETE` sono protetti e richiedono il ruolo `ADMIN`.
 */
@RestController
@RequestMapping("/api/paesi")
public class PaeseController {

    /**
     * DAO per accedere ai dati dei Paesi.
     */

    private PaeseRepository paeseRepository;

    /**
     * Restituisce una lista di tutti i Paesi disponibili.
     *
     * @return una lista di oggetti {@link Paese}.
     */
    @GetMapping
    public List<Paese> getAllPaesi() {
        return paeseRepository.findAll();
    }

    /**
     * Restituisce i dettagli di un Paese specifico dato il suo ID.
     *
     * @param id l'ID del Paese da cercare.
     * @return un'entità {@link ResponseEntity} contenente il Paese,
     *         oppure uno stato 404 se il Paese non viene trovato.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paese> getPaeseById(@PathVariable String id) {
        Optional<Paese> paese = paeseRepository.findById(id);
        return paese.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuovo Paese.
     * Richiede il ruolo di amministratore (`ADMIN`).
     *
     * @param paese l'oggetto {@link Paese} da creare.
     * @return il Paese creato.
     */

    @PostMapping
    public Paese createPaese(@RequestBody Paese paese) {
        return paeseRepository.save(paese);
    }

    /**
     * Aggiorna un Paese esistente dato il suo ID.
     * Richiede il ruolo di amministratore (`ADMIN`).
     *
     * @param id l'ID del Paese da aggiornare.
     * @param updatedPaese i dati aggiornati del Paese.
     * @return un'entità {@link ResponseEntity} contenente il Paese aggiornato,
     *         oppure uno stato 404 se il Paese non viene trovato.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Paese> updatePaese(@PathVariable String id, @RequestBody Paese updatedPaese) {
        Optional<Paese> existingPaese = paeseRepository.findById(id);
        if (existingPaese.isPresent()) {
            Paese paese = existingPaese.get();
            paese.setNome(updatedPaese.getNome());
            paese.setForum(updatedPaese.getForum());
            paese.setCampagneCrowdfunding(updatedPaese.getCampagneCrowdfunding());
            paese.setBenchmark(updatedPaese.getBenchmark());
            paese.setLinkImmagineBandiera(updatedPaese.getLinkImmagineBandiera());
            paese.setDocumentiInformativi(updatedPaese.getDocumentiInformativi());
            return ResponseEntity.ok(paeseRepository.save(paese));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Cancella un Paese esistente dato il suo ID.
     * Richiede il ruolo di amministratore (`ADMIN`).
     *
     * @param id l'ID del Paese da cancellare.
     * @return una risposta senza contenuto (`204`) se la cancellazione ha successo,
     *         oppure uno stato 404 se il Paese non viene trovato.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaese(@PathVariable String id) {
        if (paeseRepository.existsById(id)) {
            paeseRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}