package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Service.CampagnaCrowdFundingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST per la gestione delle campagne di crowdfunding.
 * Fornisce endpoint per la creazione, aggiornamento, eliminazione e ricerca delle campagne.
 */

@RestController
@RequestMapping("/api/campagne")
@RequiredArgsConstructor
@CrossOrigin
public class CampagnaCrowdFundingController {

    private final CampagnaCrowdFundingService service;

    /**
     * Restituisce tutte le campagne di crowdfunding.
     *
     * @return una lista di tutte le campagne disponibili.
     */

    @GetMapping
    public ResponseEntity<List<CampagnaCrowdFunding>> getAllCampagne() {
        return ResponseEntity.ok(service.getAllCampagne());
    }
    @GetMapping("/by-paese/{paese}")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByPaese(@PathVariable String paese) {
        List<CampagnaCrowdFunding> campagne = service.findCampagneByPaese(paese);

        if (campagne.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(campagne);
    }
    /**
     * Restituisce una campagna specifica in base al suo ID.
     *
     * @param idCampagna l'ID della campagna.
     * @return la campagna corrispondente all'ID, oppure un errore 404 se non trovata.
     */

    @GetMapping("/{idCampagna}")
    public ResponseEntity<CampagnaCrowdFunding> getCampagnaByIdCampagna(@PathVariable String idCampagna) {
        return service.getCampagnaByIdCampagna(idCampagna)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Restituisce tutte le campagne con un titolo esatto specificato.
     *
     * @param titolo il titolo della campagna.
     * @return una lista di campagne con il titolo specificato.
     */

    @GetMapping("/titolo")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByTitolo(@RequestParam String titolo) {
        return ResponseEntity.ok(service.getCampagneByTitolo(titolo));
    }

    /**
     * Restituisce tutte le campagne che contengono una parola chiave nel titolo.
     *
     * @param keyword la parola chiave da cercare nel titolo.
     * @return una lista di campagne che contengono la parola chiave nel titolo.
     */

    @GetMapping("/titolo/containing")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByTitoloContaining(@RequestParam String keyword) {
        return ResponseEntity.ok(service.getCampagneByTitoloContaining(keyword));
    }

    /**
     * Restituisce tutte le campagne in base al loro stato.
     *
     * @param stato lo stato della campagna.
     * @return una lista di campagne con lo stato specificato.
     */

    @GetMapping("/stato")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByStato(@RequestParam String stato) {
        return ResponseEntity.ok(service.getCampagneByStato(stato));
    }

    /**
     * Restituisce tutte le campagne con una data di inizio specifica.
     *
     * @param dataInizio la data di inizio in formato ISO (yyyy-MM-dd).
     * @return una lista di campagne con la data di inizio specificata.
     */

    @GetMapping("/data-inizio")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByDataInizio(@RequestParam String dataInizio) {
        LocalDate date = LocalDate.parse(dataInizio);
        return ResponseEntity.ok(service.getCampagneByDataInizio(date));
    }

    /**
     * Restituisce tutte le campagne con una data di fine prevista specifica.
     *
     * @param dataPrevistaFine la data di fine prevista in formato ISO (yyyy-MM-dd).
     * @return una lista di campagne con la data di fine prevista specificata.
     */

    @GetMapping("/data-prevista-fine")
    public ResponseEntity<List<CampagnaCrowdFunding>> getCampagneByDataPrevistaFine(@RequestParam String dataPrevistaFine) {
        LocalDate date = LocalDate.parse(dataPrevistaFine);
        return ResponseEntity.ok(service.getCampagneByDataPrevistaFine(date));
    }

    /**
     * Crea una nuova campagna di crowdfunding.
     *
     * @param campagna i dati della nuova campagna.
     * @return la campagna creata.
     */

    @PostMapping("/create")
    public ResponseEntity<CampagnaCrowdFunding> createCampagna(@RequestBody CampagnaCrowdFunding campagna) {
        try {
            CampagnaCrowdFunding createdCampagna = service.createCampagna(campagna);
            return ResponseEntity.ok(createdCampagna);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // Gestisce l'eccezione di sicurezza
        }
    }

    /**
     * Aggiorna una campagna esistente.
     *
     * @return la campagna aggiornata.
     */

    @PutMapping("update/{idCampagna}")
    public ResponseEntity<CampagnaCrowdFunding> updateCampagna(@PathVariable String idCampagna, @RequestBody CampagnaCrowdFunding campagna) {
        try {
            CampagnaCrowdFunding updatedCampagna = service.updateCampagna(idCampagna, campagna);
            return ResponseEntity.ok(updatedCampagna);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // Gestisce l'eccezione di sicurezza
        }
    }

    /**
     * Gestisce la richiesta di eliminazione di una campagna di crowdfunding.
     *
     * <p>Questo endpoint consente di eliminare una campagna specificata tramite il suo identificativo.
     * Se l'eliminazione viene completata con successo, restituisce una risposta senza contenuto
     * (HTTP 204). In caso di violazione delle autorizzazioni, restituisce un errore di sicurezza
     * (HTTP 403).</p>
     *
     * @param idCampagna l'identificativo univoco della campagna di crowdfunding da eliminare.
     *                   Deve essere una stringa valida non nulla o vuota.
     * @return {@link ResponseEntity} con uno dei seguenti codici di stato:
     *         <ul>
     *             <li><b>204 No Content</b>: se la campagna viene eliminata con successo.</li>
     *             <li><b>403 Forbidden</b>: se si verifica una violazione delle autorizzazioni.</li>
     *         </ul>
     * @throws IllegalArgumentException se {@code idCampagna} è nullo o vuoto.
     */


    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCampagna(@RequestBody String idCampagna) {
        try {
            service.deleteCampagna(idCampagna);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // Gestisce l'eccezione di sicurezza
        }
    }
}