package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * DTO per rappresentare il messaggio inviato dall'utente al chatbot.
 */
@Data
public class ChatbotRequest {

    private String userId;
    private String message;

    /**
     * Costruttore con parametro.
     *
     * @param userId  l'ID dell'utente.
     * @param message il messaggio inviato dall'utente.
     */
    public ChatbotRequest(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}