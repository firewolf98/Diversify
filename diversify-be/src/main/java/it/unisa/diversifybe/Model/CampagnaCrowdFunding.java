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
@Document(collection = "Campagne di crowdfunding")
public class CampagnaCrowdFunding {

    /**
     * Identificativo unico della campagna di crowdfunding.
     */

    private String id_campagna;

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

    private LocalDate data_inizio;

    /**
     * Data di fine prevista della campagna di crowdfunding.
     */

    private LocalDate data_prevista_fine;

    /**
     * L'importo che la campagna di crowdfunding si propone di raccogliere.
     */

    private BigDecimal somma_da_raccogliere;

    /**
     * L'importo effettivamente raccolto dalla campagna di crowdfunding.
     */

    private BigDecimal somma_raccolta;

    /**
     * Stato attuale della campagna di crowdfunding.
     * Gli stati possibili includono "terminata", "attiva", "in sospensione".
     */

    private String stato;

    /* Possibile aggiunta di pagamento PayPal
    private List<String> paymentMethods;  // Esempio: ["PayPal"]

    public void addPaypalPaymentMethod() {
        if (!paymentMethods.contains("PayPal")) {
            paymentMethods.add("PayPal");
     */
}
