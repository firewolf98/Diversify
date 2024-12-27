package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.ChatbotMessage;
import it.unisa.diversifybe.Model.ConversazioneChatbot;
import it.unisa.diversifybe.Repository.ConversazioneChatbotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire la logica delle operazioni sulle conversazioni della chatbot.
 */
@Service
public class ChatbotService {

    private final ConversazioneChatbotRepository conversazioneRepository;

    @Autowired
    public ChatbotService(ConversazioneChatbotRepository conversazioneRepository) {
        this.conversazioneRepository = conversazioneRepository;
    }

    /**
     * Recupera tutte le conversazioni di un utente specificato dall'ID.
     *
     * @param userId l'ID dell'utente di cui si vogliono recuperare le conversazioni.
     * @return una lista di {@link ChatbotMessage} rappresentanti la conversazione.
     */
    public List<ChatbotMessage> getUserConversations(String userId) {
        // Recupera la conversazione dall'utente
        Optional<ConversazioneChatbot> conversazioneOpt = conversazioneRepository.findByIdUtente(userId);

        if (conversazioneOpt.isPresent()) {
            ConversazioneChatbot conversazione = conversazioneOpt.get();
            List<ChatbotMessage> messages = new ArrayList<>();

            // Converte i messaggi della conversazione in ChatMessage
            for (ConversazioneChatbot.Messaggio messaggio : conversazione.getMessaggi()) {
                ChatbotMessage chatMessage = new ChatbotMessage(
                        userId,
                        messaggio.getDomandaUtente(),
                        messaggio.getRispostaChatbot(),
                        messaggio.getTimestamp()
                );
                messages.add(chatMessage);
            }

            return messages;
        }

        // Se nessuna conversazione trovata, restituisce una lista vuota
        return new ArrayList<>();
    }

    /**
     * Salva un messaggio nella conversazione della chatbot per un utente.
     * Se l'utente non ha una conversazione esistente, ne crea una nuova.
     *
     * @param chatbotMessage il messaggio da salvare.
     */
    public void saveMessage(ChatbotMessage chatbotMessage) {
        // Cerca la conversazione esistente per l'utente
        Optional<ConversazioneChatbot> conversazioneOpt = conversazioneRepository.findByIdUtente(chatbotMessage.getUserId());

        ConversazioneChatbot conversazione;
        if (conversazioneOpt.isPresent()) {
            conversazione = conversazioneOpt.get();
        } else {
            // Se non esiste, crea una nuova conversazione
            conversazione = new ConversazioneChatbot();
            conversazione.setIdUtente(chatbotMessage.getUserId());
            conversazione.setMessaggi(new ArrayList<>());
        }

        // Aggiunge il nuovo messaggio
        ConversazioneChatbot.Messaggio nuovoMessaggio = new ConversazioneChatbot.Messaggio();
        nuovoMessaggio.setDomandaUtente(chatbotMessage.getQuestion());
        nuovoMessaggio.setRispostaChatbot(chatbotMessage.getAnswer());
        nuovoMessaggio.setTimestamp(LocalDateTime.now());

        conversazione.getMessaggi().add(nuovoMessaggio);
        conversazione.setDataUltimaInterazione(LocalDateTime.now());

        // Salva la conversazione aggiornata
        conversazioneRepository.save(conversazione);
    }

}
