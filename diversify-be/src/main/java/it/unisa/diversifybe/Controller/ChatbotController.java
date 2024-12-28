package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.ChatbotMessage;
import it.unisa.diversifybe.Service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller che gestisce le operazioni relative alla chatbot.
 * Espone gli endpoint per recuperare e salvare conversazioni.
 */
@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    /**
     * Costruttore del controller. Viene iniettata la dipendenza del servizio {@link ChatbotService}.
     *
     * @param chatbotService il servizio che gestisce la logica di salvataggio e recupero delle conversazioni.
     */
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Endpoint per recuperare le conversazioni di un utente specifico.
     *
     * @param userId l'ID dell'utente di cui recuperare le conversazioni.
     * @return una lista di messaggi rappresentanti le conversazioni dell'utente.
     */
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatbotMessage>> getConversations(@PathVariable String userId) {
        try {
            // Recupera le conversazioni dal servizio
            List<ChatbotMessage> conversations = chatbotService.getUserConversations(userId);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            // Gestisce eventuali errori durante il recupero delle conversazioni
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint per salvare i messaggi di una conversazione della chatbot.
     *
     * @param chatbotMessage il messaggio della conversazione da salvare.
     * @return una risposta HTTP che indica il successo o il fallimento dell'operazione.
     */
    @PostMapping("/conversations")
    public ResponseEntity<Void> saveMessage(@RequestBody ChatbotMessage chatbotMessage) {
        try {
            // Salva il messaggio utilizzando il servizio
            chatbotService.saveMessage(chatbotMessage);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Gestisce eventuali errori durante il salvataggio del messaggio
            return ResponseEntity.status(500).build();
        }
    }
}
