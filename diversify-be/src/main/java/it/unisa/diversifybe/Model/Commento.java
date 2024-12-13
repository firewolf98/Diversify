package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

/**
 * La classe rappresenta un commento all'interno di un post.
 * Ogni commento ha un autore, un contenuto, una data di creazione,
 * un numero di like e una lista di subcommenti.
 */

@Data
@Document(collection = "Commenti")
public class Commento {

    /**
     * L'identificativo dell'autore del commento.
     * Questo rappresenta l'ID dell'utente che ha scritto il commento.
     */

    private String idCommento;

    /**
     * Il contenuto del commento.
     * Rappresenta il testo effettivo scritto dall'autore del commento.
     */

    private String contenuto;

    /**
     * La data di creazione del commento.
     * Indica quando il commento è stato creato.
     */

    private Date dataCreazione;

    /**
     * Il numero di like ricevuti dal commento.
     * Questo valore rappresenta la popolarità o l'approvazione del commento da parte degli altri utenti.
     */

    private int like;

    /**
     * Una lista di subcommenti associati a questo commento.
     * Ogni subcommento è una risposta a un commento principale.
     */

    private List<Subcommento> subcommenti;
}