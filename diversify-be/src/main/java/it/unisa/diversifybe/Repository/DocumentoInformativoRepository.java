package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DocumentoInformativoRepository extends MongoRepository<DocumentoInformativo, String> {

    /**
     * Trova tutti i documenti informativi associati a un determinato Paese.
     *
     * @param idPaese L'ID del Paese.
     * @return Una lista di documenti informativi.
     */
    List<DocumentoInformativo> findByIdPaese(String idPaese);
}