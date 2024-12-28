package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * La classe rappresenta un sottocommento di un commento principale.
 * Ogni sottocommento contiene informazioni sull'autore, il contenuto e la data di creazione.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Subcommenti")
public class Subcommento {

    /**
     * L'identificativo dell'autore del sottocommento.
     * Rappresenta l'utente che ha scritto il sottocommento.
     */

    private String idAutore;

    /**
     * Il contenuto del sottocommento.
     * Contiene il testo che l'autore ha scritto come risposta o commento aggiuntivo.
     */

    private String contenuto;

    /**
     * La data di creazione del sottocommento.
     * Rappresenta il momento in cui il sottocommento è stato pubblicato.
     */

    private Date dataCreazione;
}

