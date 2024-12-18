package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

/**
 * La classe rappresenta un forum in cui gli utenti possono interagire.
 * Ogni forum ha un titolo, una descrizione e una lista di post associati.
 */

@Data
@Document(collection = "Forum")
public class Forum {

    /**
     * L'identificativo univoco del forum.
     * Questo campo viene utilizzato per distinguere il forum dagli altri nella piattaforma.
     */

    private String idForum;

    /**
     * Il titolo del forum.
     * Rappresenta il nome o la tematica principale del forum.
     */

    private String titolo;

    /**
     * La descrizione del forum.
     * Fornisce una breve spiegazione del forum e dei suoi contenuti.
     */

    private String descrizione;

    /**
     * Una lista di post che appartengono a questo forum.
     * Ogni post è una discussione che può contenere commenti e interazioni.
     */

    private List<Post> post;
}