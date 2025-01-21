package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.DocumentoInformativoRepository;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentoInformativoService {

    private final PaeseRepository paeseRepository;

    public DocumentoInformativoService(DocumentoInformativoRepository documentoInformativoRepository, PaeseRepository paeseRepository) {
        this.paeseRepository = paeseRepository;
    }

    /**
     * Recupera una lista di documenti informativi associati a un Paese specifico.
     *
     * <p>Il metodo cerca nei dati salvati in {@code paeseRepository} tutti i paesi e restituisce
     * una lista di documenti informativi associati al Paese identificato da {@code idPaese}.
     * Se il Paese non viene trovato o non ha documenti associati, viene restituita una lista vuota.</p>
     *
     * @param idPaese l'identificativo univoco del Paese per cui recuperare i documenti informativi.
     *                Non può essere nullo o vuoto.
     * @return una lista di {@link DocumentoInformativo} associati al Paese con l'ID specificato.
     *         Se il Paese non esiste o non ha documenti, la lista sarà vuota.
     * @throws IllegalArgumentException se {@code idPaese} è nullo o vuoto.
     */

    public List<DocumentoInformativo> findByIdPaese(String idPaese) {
        if (idPaese == null || idPaese.isBlank()) {
            throw new IllegalArgumentException("ID Paese non può essere nullo o vuoto.");
        }
        List<DocumentoInformativo> documenti= new ArrayList<>();
        List<Paese> paesi= paeseRepository.findAll();
        for (Paese paese: paesi) {
            if (paese.getIdPaese().equals(idPaese)) {
                documenti.addAll(paese.getDocumentiInformativi());
            }
        }
        return documenti;
    }

}