package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

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
     * Categoria della campagna di crowdfunding.
     */
    private String categoria;

    /**
     * Descrizione dettagliata della campagna di crowdfunding.
     */
    private String descrizione;

    /**
     * Data inizio campagna
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
     * Immagine dello sfondo della campagna
     */
    private String immagine;

    /**
     * Il Paese della campagna
     */
    private String paese;
}
