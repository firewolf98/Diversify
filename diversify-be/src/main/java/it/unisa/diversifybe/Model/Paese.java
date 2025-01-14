package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * La classe rappresenta un paese con una serie di informazioni ad esso associate,
 * come il nome, il forum, le campagne di crowdfunding, i benchmark, i documenti informativi, ecc.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Paesi")
public class Paese {

    /**
     * L'identificativo univoco del paese.
     * Questo campo viene utilizzato per identificare in modo univoco un paese nella piattaforma.
     */

    private String idPaese;

    /**
     * Il nome del paese.
     * Rappresenta il nome ufficiale del paese.
     */

    private String nome;

    /**
     * I forum associati a questo paese.
     * Questo campo rappresenta la discussione principale e i forum legati a quel paese.
     */

    private List<String> forum;

    /**
     * Una lista di campagne di crowdfunding associate al paese.
     * Ogni campagna è rappresentata da un identificativo (ID).
     */

    private List<CampagnaCrowdFunding> campagneCrowdfunding;

    /**
     * Una lista di benchmark associati al paese.
     * I benchmark sono metriche o valori che possono essere utilizzati per confrontare i paesi tra di loro.
     */


    private List<Benchmark> benchmark;

    /**
     * Il link all'immagine della bandiera del paese.
     * Questo campo contiene l'URL dell'immagine della bandiera.
     */

    private String linkImmagineBandiera;

    /**
     * Una lista di documenti informativi relativi al paese.
     * Ogni documento contiene un titolo e un link al documento stesso.
     */

    private List<DocumentoInformativo> documentiInformativi;

}
