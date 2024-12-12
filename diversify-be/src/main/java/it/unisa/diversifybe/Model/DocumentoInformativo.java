package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * La classe rappresenta un documento informativo su un determinato Paese all'interno della piattaforma.
 * Un documento informativo ha un titolo e un link che punta al contenuto del documento.
 */

@Data
@Document(collection = "Documenti informativi")
public class DocumentoInformativo {

    /**
     * Il titolo del documento informativo.
     * Rappresenta il nome o l'intestazione del documento.
     */

    private String titolo;

    /**
     * Il link al documento informativo.
     * Questo campo contiene l'URL che porta al contenuto del documento.
     */

    private String link;
}
