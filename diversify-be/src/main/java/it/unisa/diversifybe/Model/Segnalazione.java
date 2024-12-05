package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

/**
 * La classe rappresenta una segnalazione effettuata da un utente su un altro utente.
 * Ogni segnalazione ha informazioni sull'utente che la ha effettuata, l'utente segnalato,
 * la motivazione della segnalazione, la data e il tipo di segnalazione.
 */

@Data
@Document(collection="Segnalazioni")
public class Segnalazione {

    /**
     * L'identificativo univoco della segnalazione.
     */

    private String id_segnalazione;

    /**
     * L'identificativo dell'utente che ha effettuato la segnalazione.
     * Questo campo fa riferimento all'utente che ha deciso di segnalare un altro utente.
     */

    private String id_segnalante;

    /**
     * L'identificativo dell'utente segnalato.
     * Questo campo fa riferimento all'utente che è stato segnalato.
     */

    private String id_segnalato;


    /**
     * La motivazione per cui è stata effettuata la segnalazione.
     * Descrive il motivo della segnalazione (ad esempio: comportamento offensivo, contenuto inappropriato, ecc.).
     */

    private String motivazione;

    /**
     * La data in cui la segnalazione è stata effettuata.
     * Rappresenta il momento in cui l'utente ha effettuato la segnalazione.
     */

    private Date data_segnalazione;

    /**
     * Il tipo di segnalazione.
     * Indica la natura della segnalazione (ad esempio: "offensivo", "spam", ecc.).
     */

    private String tipo_segnalazione;
}