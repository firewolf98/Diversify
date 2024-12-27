package it.unisa.diversifybe.DTO;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Rappresenta un messaggio in una conversazione con la chatbot,
 * che include la domanda dell'utente e la risposta della chatbot.
 */
@Data
public class ChatbotMessage {
    private String userId;        // ID dell'utente che ha inviato il messaggio
    private String question;      // Domanda o messaggio inviato dall'utente
    private String answer;        // Risposta fornita dalla chatbot
    private String timestamp;     // Data e ora in cui il messaggio è stato inviato (opzionale)

    public ChatbotMessage(String userId, String domandaUtente, String rispostaChatbot, LocalDateTime timestamp) {
    }
}
