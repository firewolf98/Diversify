package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.ChatbotRequest;
import it.unisa.diversifybe.DTO.ChatbotResponse;
import it.unisa.diversifybe.Service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller che gestisce le operazioni relative alla chatbot.
 * Espone gli endpoint per inviare messaggi alla chatbot e ottenere risposte.
 */
@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio {@link ChatbotService}.
     *
     * @param chatbotService il servizio che gestisce la logica di elaborazione dei messaggi della chatbot.
     */
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Endpoint per inviare un messaggio al chatbot e ottenere una risposta.
     *
     * @param chatbotRequest i dati del messaggio inviato dall'utente alla chatbot, che includono un ID utente e il messaggio.
     * @return una risposta HTTP con il messaggio generato dalla chatbot.
     */
    @PostMapping("/message")
    public ResponseEntity<ChatbotResponse> handleMessage(@RequestBody ChatbotRequest chatbotRequest) {
        try {
            // Elabora il messaggio ricevuto utilizzando il servizio chatbot
            String chatbotResponseMessage = chatbotService.processMessage(chatbotRequest.getUserId(), chatbotRequest.getMessage());

            // Restituisce il messaggio generato dalla chatbot
            ChatbotResponse chatbotResponse = new ChatbotResponse(chatbotResponseMessage);
            return ResponseEntity.ok(chatbotResponse);

        } catch (Exception e) {
            // Gestisce eventuali errori imprevisti durante l'elaborazione del messaggio
            return ResponseEntity.status(500).body(new ChatbotResponse("Errore interno durante l'elaborazione del messaggio: " + e.getMessage()));
        }
    }
}
