package it.unisa.diversifybe.Model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampagnaCrowdFunding {
    private String id_campagna;
    private String titolo;
    private String contenuto;
    private String email;
    private List<String> images;
    private LocalDate data_inizio;
    private LocalDate data_prevista_fine;
    private BigDecimal somma_da_raccogliere;
    private BigDecimal somma_raccolta;
    private String stato;
    //ad esempio terminata, attiva, in sospensione

    /* Possibile aggiunta di pagamento PayPal
    private List<String> paymentMethods;  // Esempio: ["PayPal"]

    public void addPaypalPaymentMethod() {
        if (!paymentMethods.contains("PayPal")) {
            paymentMethods.add("PayPal");
     */
}
