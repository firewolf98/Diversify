package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * DTO per rappresentare la risposta generata dal chatbot.
 */
@Data
public class ChatbotResponse {

    private String response;

    /**
     * Costruttore con parametro.
     *
     * @param response la risposta generata dal chatbot.
     */
    public ChatbotResponse(String response) {
        this.response = response;
    }
}
