package it.unisa.diversifybe.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * La classe {@code ConversazioneChatbot} rappresenta una conversazione tra un utente e la chatbot.
 */
@Data
public class ConversazioneChatbot {

    private String idUtente;
    private String idConversazione;
    private LocalDateTime dataUltimaInterazione;
    private List<Messaggio> messaggi;

    /**
     * Classe interna per rappresentare i messaggi all'interno della conversazione.
     * Ogni messaggio contiene la domanda dell'utente, la risposta della chatbot e il timestamp della risposta.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Messaggio {
        private String domandaUtente;
        private String rispostaChatbot;
        private LocalDateTime timestamp;
    }
}
