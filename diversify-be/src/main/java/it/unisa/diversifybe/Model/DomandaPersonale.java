package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * La classe rappresenta una domanda personale con una risposta associata.
 * Ogni domanda ha un tipo e una risposta hashata (SHA-256).
 */

@Data
@Document(collection = "Domande personali")
public class DomandaPersonale {

    /**
     * Il tipo della domanda personale.
     * Ad esempio, la domanda potrebbe essere "Qual è il tuo colore preferito?".
     */

    private String tipoDomanda;

    /**
     * La risposta personale associata alla domanda.
     * La risposta è memorizzata come una stringa hashata usando l'algoritmo SHA-256.
     */

    private String rispostaPersonaleSha256;
}
