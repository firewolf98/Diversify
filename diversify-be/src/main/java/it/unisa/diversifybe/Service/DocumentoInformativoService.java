package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Repository.DocumentoInformativoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoInformativoService {

    private final DocumentoInformativoRepository documentoInformativoRepository;

    public DocumentoInformativoService(DocumentoInformativoRepository documentoInformativoRepository) {
        this.documentoInformativoRepository = documentoInformativoRepository;
    }

    /**
     * Trova i documenti informativi per un Paese.
     *
     * @param idPaese L'ID del Paese.
     * @return Lista di documenti informativi.
     */
    public List<DocumentoInformativo> findByIdPaese(String idPaese) {
        return documentoInformativoRepository.findByIdPaese(idPaese);
    }
}