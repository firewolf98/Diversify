package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.ChatbotRequest;
import it.unisa.diversifybe.DTO.ChatbotResponse;
import it.unisa.diversifybe.Model.ConversazioneChatbot;
import it.unisa.diversifybe.Repository.ConversazioneChatbotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * La classe {@code ChatbotService} gestisce la logica di business relativa alla chatbot.
 * Riceve messaggi degli utenti, genera risposte e salva le conversazioni nel database.
 */
@Service
public class ChatbotService {

    @Autowired
    private ConversazioneChatbotRepository conversazioneChatbotRepository;

    /**
     * Elabora un messaggio dell'utente, genera una risposta e salva la conversazione.
     *
     * @param userId        l'identificativo dell'utente.
     * @param userMessage   il messaggio ricevuto dall'utente.
     * @return il messaggio di risposta generato dalla chatbot.
     */
    public String processMessage(String userId, String userMessage) {
        // Genera una risposta basata sul messaggio dell'utente
        String chatbotResponseMessage = generateChatbotResponse(userMessage);

        // Recupera o crea una nuova conversazione
        ConversazioneChatbot conversazione = findOrCreateConversation(userId);

        // Crea un nuovo messaggio
        ConversazioneChatbot.Messaggio messaggio = new ConversazioneChatbot.Messaggio();
        messaggio.setDomandaUtente(userMessage);
        messaggio.setRispostaChatbot(chatbotResponseMessage);
        messaggio.setTimestamp(LocalDateTime.now());

        // Aggiunge il messaggio alla conversazione
        conversazione.getMessaggi().add(messaggio);

        // Aggiorna la data dell'ultima interazione
        conversazione.setDataUltimaInterazione(LocalDateTime.now());

        // Salva la conversazione aggiornata
        conversazioneChatbotRepository.save(conversazione);

        // Restituisce il messaggio generato
        return chatbotResponseMessage;
    }

    /**
     * Genera una risposta simulata per un dato messaggio dell'utente.
     * Questo metodo può essere espanso per integrare modelli di NLP o altre logiche avanzate.
     *
     * @param userMessage il messaggio ricevuto dall'utente.
     * @return la risposta generata.
     */
    private String generateChatbotResponse(String userMessage) {
        // Implementazione base di simulazione della risposta
        return "Risposta generica per: " + userMessage;
    }

    /**
     * Recupera una conversazione esistente o ne crea una nuova per un dato utente.
     * Se l'ultima interazione supera le 6 ore, crea una nuova conversazione.
     *
     * @param userId l'identificativo dell'utente.
     * @return un oggetto {@link ConversazioneChatbot} esistente o nuovo.
     */
    private ConversazioneChatbot findOrCreateConversation(String userId) {
        // Recupera una conversazione esistente
        ConversazioneChatbot conversazione = conversazioneChatbotRepository.findTopByIdUtenteOrderByDataUltimaInterazioneDesc(userId);

        // Verifica la data dell'ultima interazione
        if (conversazione != null && conversazione.getDataUltimaInterazione() != null) {
            LocalDateTime ultimaInterazione = conversazione.getDataUltimaInterazione();
            if (ultimaInterazione.plusHours(6).isAfter(LocalDateTime.now())) {
                return conversazione;
            }
        }

        // Crea una nuova conversazione
        conversazione = new ConversazioneChatbot();
        conversazione.setIdUtente(userId);
        conversazione.setIdConversazione(UUID.randomUUID().toString());
        conversazione.setMessaggi(new ArrayList<>());
        conversazione.setDataUltimaInterazione(LocalDateTime.now());

        return conversazione;
    }
}
