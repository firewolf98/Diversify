package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Segnalazione;
import it.unisa.diversifybe.Service.SegnalazioneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller che gestisce le operazioni relative alle segnalazioni.
 * Questo controller espone i seguenti endpoint:
 * - GET /segnalazioni: per ottenere tutte le segnalazioni.
 * - POST /segnalazioni: per creare una nuova segnalazione.
 * - GET /segnalazione/{id}: per ottenere una segnalazione specifica tramite ID.
 * - PUT /segnalazione/{id}: per aggiornare una segnalazione esistente.
 * - DELETE /segnalazione/{id}: per eliminare una segnalazione esistente.
 * - GET /segnalazioni/utente/{idSegnalante}: per ottenere tutte le segnalazioni fatte da un utente specifico.
 */
@RestController
@RequestMapping("/segnalazioni")
public class SegnalazioneController {

    private final SegnalazioneService segnalazioneService;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio SegnalazioneService.
     *
     * @param segnalazioneService il servizio che gestisce la logica per le segnalazioni.
     */
    public SegnalazioneController(SegnalazioneService segnalazioneService) {
        this.segnalazioneService = segnalazioneService;
    }

    /**
     * Restituisce tutte le segnalazioni.
     *
     * @return una lista di tutte le segnalazioni.
     */
    @GetMapping
    public List<Segnalazione> getAllSegnalazioni() {
        return segnalazioneService.getAllSegnalazioni();
    }

    /**
     * Restituisce una segnalazione specifica per ID.
     *
     * @param id l'ID della segnalazione.
     * @return la segnalazione con l'ID specificato, oppure una risposta HTTP 404 se non trovata.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Segnalazione> getSegnalazioneById(@PathVariable("id") String id) {
        Optional<Segnalazione> segnalazione = segnalazioneService.getSegnalazioneById(id);
        return segnalazione.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Crea una nuova segnalazione.
     *
     * @param segnalazione l'oggetto {@link Segnalazione} contenente i dati da creare.
     * @return una risposta HTTP 201 (CREATED) con la segnalazione appena creata,
     *         oppure una risposta HTTP 400 (BAD REQUEST) in caso di errore durante la creazione.
     */
    @PostMapping
    public ResponseEntity<?> createSegnalazione(@RequestBody Segnalazione segnalazione) {
        try {
            Segnalazione createdSegnalazione = segnalazioneService.createSegnalazione(segnalazione);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSegnalazione);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la creazione della segnalazione");
        }
    }

    /**
     * Aggiorna una segnalazione esistente.
     *
     * @param id l'ID della segnalazione da aggiornare.
     * @param segnalazione l'oggetto {@link Segnalazione} con i nuovi dati da aggiornare.
     * @return una risposta HTTP 200 (OK) con la segnalazione aggiornata,
     *         oppure una risposta HTTP 404 (NOT FOUND) se la segnalazione non esiste.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSegnalazione(@PathVariable("id") String id, @RequestBody Segnalazione segnalazione) {
        Optional<Segnalazione> existingSegnalazione = segnalazioneService.getSegnalazioneById(id);
        if (existingSegnalazione.isPresent()) {
            segnalazione.setIdSegnalazione(id); // Impostiamo l'ID per l'aggiornamento
            Segnalazione updatedSegnalazione = segnalazioneService.updateSegnalazione(segnalazione);
            return ResponseEntity.ok(updatedSegnalazione);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Segnalazione non trovata");
        }
    }

    /**
     * Elimina una segnalazione specifica per ID.
     *
     * @param id l'ID della segnalazione da eliminare.
     * @return una risposta HTTP 204 (NO CONTENT) se la segnalazione è stata eliminata,
     *         oppure una risposta HTTP 404 (NOT FOUND) se la segnalazione non esiste.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSegnalazione(@PathVariable("id") String id) {
        Optional<Segnalazione> existingSegnalazione = segnalazioneService.getSegnalazioneById(id);
        if (existingSegnalazione.isPresent()) {
            segnalazioneService.deleteSegnalazione(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Segnalazione eliminata");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Segnalazione non trovata");
        }
    }

    /**
     * Restituisce tutte le segnalazioni fatte da un utente specifico.
     *
     * @param idSegnalante l'ID dell'utente che ha effettuato le segnalazioni.
     * @return una lista di segnalazioni fatte dall'utente con l'ID specificato.
     */
    @GetMapping("/utente/{idSegnalante}")
    public List<Segnalazione> getSegnalazioniByUtente(@PathVariable("idSegnalante") String idSegnalante) {
        return segnalazioneService.getSegnalazioniByIdSegnalante(idSegnalante);
    }
}
