package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * La classe rappresenta un documento informativo su un determinato Paese all'interno della piattaforma.
 * Un documento informativo ha un titolo e un link che punta al contenuto del documento.
 */

@Data
@Document(collection = "DocumentiInformativi")
public class DocumentoInformativo {

   private String idDocumentoInformativo;

    private String titolo;

    private String descrizione;

    private String contenuto;

    private String idPaese;

    private String linkImmagine;

    private String linkVideo;

}
