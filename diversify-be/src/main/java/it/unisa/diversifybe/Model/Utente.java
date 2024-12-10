package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * La classe rappresenta un utente all'interno del sistema.
 * Contiene le informazioni personali dell'utente come nome, cognome, email,
 * username, password (hashata), e altre informazioni relative alla gestione dell'account.
 * Inoltre, include metodi per la gestione della blacklist dei forum.
 */

@Data
@Document(collection = "Utenti")
public class Utente {

    /**
     * L'identificativo unico dell'utente nel sistema.
     */

    private String id;

    /**
     * Il nome dell'utente.
     */

    private String nome;


    /**
     * Il cognome dell'utente.
     */

    private String cognome;

    /**
     * Il codice fiscale dell'utente.
     */

    private String cf;

    /**
     * L'indirizzo email dell'utente.
     */

    private String email;

    /**
     * Il nome utente scelto dall'utente per accedere al sistema.
     */

    private String username;

    /**
     * La password hashata dell'utente (SHA-256).
     */

    private String passwordHash;

    /**
     * Il tipo di domanda di sicurezza associata all'utente (es. "Madre" o "Città di nascita").
     */

    private String tipoDomanda;

    /**
     * La risposta alla domanda di sicurezza, anch'essa hashata.
     */

    private String rispostaHash;

    /**
     * Una lista che contiene gli ID dei forum in cui l'utente è bannato.
     * I forum in blacklist non sono accessibili dall'utente.
     */

    private List<String> blacklistForum;

    /**
     * Il ruolo dell'utente (ad esempio, "ADMIN", "USER").
     */

    private String ruolo;

    /**
     * Verifica se un ID forum è nella blacklist.
     *
     * @param forumId l'ID del forum da verificare (String)
     * @return true se il forum è nella blacklist, false altrimenti
     */
    public boolean isForumInBlacklist(String forumId) {
        return blacklistForum != null && blacklistForum.contains(forumId);
    }
}

