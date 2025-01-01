package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * La classe rappresenta una campagna di crowdfunding.
 * Una campagna include informazioni sulla raccolta fondi, le date di inizio e fine previste,
 * l'importo da raccogliere e l'importo attualmente raccolto.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Campagne")
public class CampagnaCrowdFunding {

    /**
     * Identificativo unico della campagna di crowdfunding.
     */

    private String idCampagna;

    /**
     * Titolo della campagna di crowdfunding.
     */

    private String titolo;

    /**
     * Descrizione dettagliata della campagna di crowdfunding.
     */

    private String contenuto;

    /**
     * Lista delle immagini relative alla campagna di crowdfunding.
     * Le immagini possono essere utilizzate per illustrare la campagna.
     */

    private List<String> images;

    /**
     * Data di inizio della campagna di crowdfunding.
     */

    private LocalDate dataInizio;

    /**
     * Data di fine prevista della campagna di crowdfunding.
     */

    private LocalDate dataPrevistaFine;

    /**
     * L'importo che la campagna di crowdfunding si propone di raccogliere.
     */

    private BigDecimal sommaDaRaccogliere;

    /**
     * L'importo effettivamente raccolto dalla campagna di crowdfunding.
     */

    private BigDecimal sommaRaccolta;

    /**
     * Stato attuale della campagna di crowdfunding.
     * Gli stati possibili includono "terminata", "attiva", "in sospensione".
     */

    private String stato;

    /**
     * Paese della campagna di crowdfunding
     */
    private String Paese;

}
