package it.unisa.diversifybe.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * La classe {@code ConversazioneChatbot} rappresenta una conversazione tra un utente e la chatbot.
 */
@Data
@Document(collection = "ConversazioneChatbot")
public class ConversazioneChatbot {

    @Id
    private String idConversazione;

    @Indexed(unique = true) // Impone l'unicità per idUtente
    private String idUtente;

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
