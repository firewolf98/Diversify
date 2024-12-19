package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.PaeseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Servizio per la generazione di file PDF contenenti informazioni sui Paesi.
 * Utilizza la libreria PDFBox per creare e personalizzare i documenti PDF.
 */
@Service
@RequiredArgsConstructor
public class PDFService {

    /**
     * Repository per accedere ai dati dei Paesi dal database.
     */
    private final PaeseRepository paeseRepository;

    /**
     * Genera un file PDF contenente i dettagli di un Paese specifico.
     *
     * @param id l'ID del Paese di cui generare il PDF.
     * @return un {@link ByteArrayInputStream} contenente il PDF generato.
     * @throws IOException se si verifica un errore durante la creazione del PDF.
     * @throws IllegalArgumentException se il Paese con l'ID specificato non viene trovato.
     */
    public ByteArrayInputStream generatePdfForPaese(String id) throws IOException {
        // Recupera il Paese dal repository
        Optional<Paese> optionalPaese = paeseRepository.findById(id);

        if (optionalPaese.isEmpty()) {
            throw new IllegalArgumentException("Paese con ID " + id + " non trovato.");
        }

        Paese paese = optionalPaese.get();

        // Crea il documento PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Aggiunge contenuti alla pagina
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 700);

                contentStream.showText("Dettagli del Paese");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                contentStream.showText("Nome: " + paese.getNome());
                contentStream.newLine();
                contentStream.showText("Forum: " + paese.getForum());
                contentStream.newLine();
                contentStream.showText("Campagne di Crowdfunding: " + paese.getCampagneCrowdfunding());
                contentStream.newLine();
                contentStream.showText("Link immagine bandiera: " + paese.getLinkImmagineBandiera());
                contentStream.newLine();

                contentStream.endText();
            }

            // Converte il documento in un array di byte
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}
