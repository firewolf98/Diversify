package it.unisa.diversifybe.Model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
    private String id;
    private String nome;
    private String cognome;
    private String cf;
    private String email;
    private String username;
    private String passwordHash;
    private String tipoDomanda;
    private String rispostaHash;
    private List<String> blacklistForum;
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

