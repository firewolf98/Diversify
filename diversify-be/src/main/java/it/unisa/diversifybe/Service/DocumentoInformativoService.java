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

    private final DocumentoInformativoRepository documentoInformativoRepository;
    private final PaeseRepository paeseRepository;

    public DocumentoInformativoService(DocumentoInformativoRepository documentoInformativoRepository, PaeseRepository paeseRepository) {
        this.documentoInformativoRepository = documentoInformativoRepository;
        this.paeseRepository = paeseRepository;
    }

    /**
     * Trova i documenti informativi per un Paese specifico.
     * <p>
     * Questo metodo recupera una lista di documenti informativi associati all'ID del Paese fornito.
     * Se l'ID del Paese è nullo o vuoto, viene lanciata un'eccezione {@link IllegalArgumentException}.
     * </p>
     *
     * @param idPaese l'ID del Paese per cui recuperare i documenti informativi.
     * @return una lista di {@link DocumentoInformativo} associati all'ID del Paese.
     * @throws IllegalArgumentException se l'ID del Paese è nullo o vuoto.
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