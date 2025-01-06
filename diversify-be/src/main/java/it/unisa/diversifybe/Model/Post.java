package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

/**
 * La classe rappresenta un post in un forum.
 * Ogni post ha un titolo, un autore, contenuto, data di creazione, numero di "like" e commenti associati.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Post")
public class Post {

    /**
     * L'identificativo univoco del post.
     * Questo campo viene utilizzato per identificare in modo univoco un post all'interno della piattaforma.
     */
    @Id
    private String idPost;

    /**
     * Il titolo del post.
     * Rappresenta l'intestazione o il tema principale del post.
     */

    private String titolo;

    /**
     * L'identificativo dell'autore del post.
     * Questo campo rappresenta l'autore del post, in genere un riferimento al suo profilo utente.
     */
    @Id
    private String idAutore;

    /**
     * Il contenuto del post.
     * Questo campo contiene il testo o il materiale principale del post.
     */

    private String contenuto;

    /**
     * La data di creazione del post.
     * Rappresenta la data e l'ora in cui il post è stato creato.
     */

    private Date dataCreazione;

    /**
     * Il numero di "like" ricevuti dal post.
     * Indica la quantità di apprezzamenti che il post ha ricevuto dalla comunità.
     */

    private int like;

    /**
     * Una lista di commenti associati al post.
     * Ogni commento è un'ulteriore interazione degli utenti con il post.
     */

    private List<Commento> commenti;

    /**
     * L'Id del forum dove è contenuto il post
     */
    private String idForum;
}
