package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Service.DocumentoInformativoService;
import it.unisa.diversifybe.Service.PaeseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST per la gestione delle operazioni relative ai Paesi.
 * Espone endpoint per ottenere, creare, aggiornare e cancellare i dati sui Paesi.
 * - Gli endpoint `GET` sono pubblici e accessibili a tutti gli utenti.
 * - Gli endpoint `POST`, `PUT` e `DELETE` sono protetti e richiedono il ruolo `ADMIN`.
 */
@RestController
@RequestMapping("/api/paesi")
public class PaeseController {

    private final PaeseService paeseService;
    private final DocumentoInformativoService documentoInformativoService;

    public PaeseController(PaeseService paeseService, DocumentoInformativoService documentoInformativoService) {
        this.paeseService = paeseService;
        this.documentoInformativoService = documentoInformativoService;
    }

    /**
     * Restituisce una lista di tutti i Paesi disponibili.
     *
     * @return una lista di oggetti {@link Paese}.
     */
    @GetMapping
    public List<Paese> getAllPaesi() {
        return paeseService.getAllPaesi();
    }

    /**
     * Restituisce i dettagli di un Paese specifico dato il suo ID.
     *
     * @param id l'ID del Paese da cercare.
     * @return un'entità {@link ResponseEntity} contenente il Paese,
     *         oppure uno stato 404 se il Paese non viene trovato.
     * @throws IllegalArgumentException se l'ID è nullo o vuoto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paese> getPaeseById(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID fornito non può essere nullo o vuoto.");
        }
        Optional<Paese> paese = paeseService.getPaeseById(id);
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
        return paeseService.createPaese(paese);
    }

    /**
     * Aggiorna un Paese esistente dato il suo ID.
     * Richiede il ruolo di amministratore (`ADMIN`).
     *
     * @param id           l'ID del Paese da aggiornare.
     * @param updatedPaese i dati aggiornati del Paese.
     * @return un'entità {@link ResponseEntity} contenente il Paese aggiornato,
     *         oppure uno stato 404 se il Paese non viene trovato.
     * @throws IllegalArgumentException se l'ID è nullo o vuoto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Paese> updatePaese(@PathVariable String id, @RequestBody Paese updatedPaese) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del Paese non può essere nullo o vuoto.");
        }
        Optional<Paese> paese = paeseService.updatePaese(id, updatedPaese);
        return paese.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Elimina un Paese esistente dato il suo ID.
     * Richiede il ruolo di amministratore (`ADMIN`).
     *
     * @param id l'ID del Paese da cancellare.
     * @return una risposta senza contenuto (`204`) se la cancellazione ha successo,
     *         oppure uno stato 404 se il Paese non viene trovato.
     * @throws IllegalArgumentException se l'ID è nullo o vuoto.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaese(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID fornito non può essere nullo o vuoto.");
        }
        paeseService.deletePaese(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Restituisce i paesi associati a un forum specifico.
     *
     * @param idForum l'ID del forum.
     * @return una lista di {@link Paese} associati.
     * @throws IllegalArgumentException se l'ID del forum è nullo o vuoto.
     */
    @GetMapping("/forum/{idForum}")
    public List<Paese> findPaesiByForum(@PathVariable String idForum) {
        if (idForum == null || idForum.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del forum non può essere nullo o vuoto.");
        }
        return paeseService.findPaesiByForum(idForum);
    }

    /**
     * Restituisce i paesi associati a una campagna di crowdfunding specifica.
     *
     * @param idCampagna l'ID della campagna di crowdfunding.
     * @return una lista di {@link Paese} associati.
     */
    @GetMapping("/campagna/{idCampagna}")
    public List<Paese> findPaesiByCampagna(@PathVariable String idCampagna) {
        return paeseService.findPaesiByCampagna(idCampagna);
    }
    /**
     * Restituisce i paesi basati su un determinato benchmark.
     *
     * @param benchmark il valore del benchmark da cercare.
     * @return una lista di {@link Paese} associati al benchmark specificato.
     * @throws IllegalArgumentException se il benchmark è null o vuoto.
     */
    @GetMapping("/benchmark/{benchmark}")
    public List<Paese> findPaesiByBenchmark(@PathVariable String benchmark) {
        if (benchmark == null || benchmark.trim().isEmpty()) {
            throw new IllegalArgumentException("Il benchmark fornito non può essere null o vuoto.");
        }
        return paeseService.findPaesiByBenchmark(benchmark);
    }


    /**
     * Restituisce i documenti informativi associati a un Paese.
     *
     * @param idPaese L'ID del Paese.
     * @return una lista di documenti informativi.
     * @throws IllegalArgumentException se l'ID del Paese è nullo o vuoto.
     */
    @GetMapping("/{idPaese}/documenti-informativi")
    public ResponseEntity<List<DocumentoInformativo>> getDocumentiInformativiByPaese(@PathVariable String idPaese) {
        if (idPaese == null || idPaese.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del Paese non può essere nullo o vuoto.");
        }
        List<DocumentoInformativo> documentiInformativi = documentoInformativoService.findByIdPaese(idPaese);
        if (documentiInformativi.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documentiInformativi);
    }

}