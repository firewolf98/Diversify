package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.PaeseRepository;
import it.unisa.diversifybe.Service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller per la gestione del download di file PDF relativi ai Paesi.
 * Fornisce un endpoint per generare e scaricare un PDF contenente le informazioni di un Paese specifico.
 */
@RestController
@RequestMapping("/api/paesi")
@RequiredArgsConstructor
public class PDFController {

    /**
     * Repository per l'accesso ai dati dei Paesi dal database.
     */
    private final PaeseRepository paeseRepository;

    /**
     * Servizio per la generazione dei file PDF.
     */
    private final PDFService pdfService;

    /**
     * Endpoint per scaricare un file PDF contenente le informazioni di un Paese specifico.
     *
     * @param id l'ID del Paese di cui scaricare il PDF.
     * @return un {@link ResponseEntity} contenente il PDF come {@link ByteArrayInputStream}
     *         e gli header per il download, oppure un errore 404 se il Paese non viene trovato.
     */
    @GetMapping("/{id}/download-pdf")
    public ResponseEntity<ByteArrayInputStream> downloadPdf(@PathVariable String id) {
        // Cerca il Paese con l'ID specificato nel repository
        Optional<Paese> paeseOpt = paeseRepository.findById(id);

        if (paeseOpt.isEmpty()) {
            // Restituisce un errore 404 se il Paese non viene trovato
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        if (paeseOpt.isPresent()) {
            Paese paese = paeseOpt.get();
            ByteArrayInputStream pdfBytes = null;

            try {
                // Genera il PDF utilizzando il servizio PDFService
                pdfBytes = pdfService.generatePdfForPaese(paese.getId_paese());
            } catch (IOException e) {
                // Gestisce eventuali errori di I/O durante la generazione del PDF
                throw new RuntimeException(e);
            }

            // Imposta gli header per il download del file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "Hai scaricato" + paese.getNome() + ".pdf");

            // Restituisce il PDF come risposta con status 200
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(pdfBytes);
        } else {
            // Restituisce un errore 404 se il Paese non viene trovato
            return ResponseEntity.notFound().build();
        }
    }
}
