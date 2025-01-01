package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.ChatbotMessage;
import it.unisa.diversifybe.Model.ConversazioneChatbot;
import it.unisa.diversifybe.Repository.ConversazioneChatbotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatbotServiceTest {

    @InjectMocks
    private ChatbotService chatbotService;

    @Mock
    private ConversazioneChatbotRepository conversazioneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserConversations_UserWithMessages() {
        String userId = "testUser";
        LocalDateTime timestamp = LocalDateTime.now();

        // Configura una conversazione esistente
        ConversazioneChatbot conversazione = new ConversazioneChatbot();
        conversazione.setIdUtente(userId);
        List<ConversazioneChatbot.Messaggio> messaggi = new ArrayList<>();
        messaggi.add(new ConversazioneChatbot.Messaggio("Hello?", "Hi there!", timestamp));
        conversazione.setMessaggi(messaggi);

        when(conversazioneRepository.findByIdUtente(userId)).thenReturn(Optional.of(conversazione));

        // Esegui il test
        System.out.println("Testing getUserConversations for user with messages...");
        List<ChatbotMessage> result = chatbotService.getUserConversations(userId);

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        System.out.println("First message: " + result.getFirst());
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain 1 message");
        assertEquals("Hello?", result.getFirst().getQuestion(), "Question should match");
        assertEquals("Hi there!", result.getFirst().getAnswer(), "Answer should match");
        verify(conversazioneRepository, times(1)).findByIdUtente(userId);
    }

    @Test
    void getUserConversations_UserWithoutMessages() {
        String userId = "testUser";

        when(conversazioneRepository.findByIdUtente(userId)).thenReturn(Optional.empty());

        // Esegui il test
        System.out.println("Testing getUserConversations for user without messages...");
        List<ChatbotMessage> result = chatbotService.getUserConversations(userId);

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be an empty list");
        verify(conversazioneRepository, times(1)).findByIdUtente(userId);
    }

    @Test
    void saveMessage_NewConversation() {
        String userId = "testUser";
        LocalDateTime timestamp = LocalDateTime.now();
        ChatbotMessage chatbotMessage = new ChatbotMessage(userId, "Hello?", "Hi there!", timestamp);

        when(conversazioneRepository.findByIdUtente(userId)).thenReturn(Optional.empty());

        // Esegui il test
        System.out.println("Testing saveMessage for a new conversation...");
        chatbotService.saveMessage(chatbotMessage);

        // Verifica il comportamento
        System.out.println("New conversation saved for user: " + userId);
        verify(conversazioneRepository, times(1)).findByIdUtente(userId);
        verify(conversazioneRepository, times(1)).save(any(ConversazioneChatbot.class));
    }

    @Test
    void saveMessage_ExistingConversation() {
        String userId = "testUser";
        LocalDateTime timestamp = LocalDateTime.now();
        ChatbotMessage chatbotMessage = new ChatbotMessage(userId, "Hello?", "Hi there!", timestamp);

        // Configura una conversazione esistente
        ConversazioneChatbot conversazione = new ConversazioneChatbot();
        conversazione.setIdUtente(userId);
        conversazione.setMessaggi(new ArrayList<>());

        when(conversazioneRepository.findByIdUtente(userId)).thenReturn(Optional.of(conversazione));

        // Esegui il test
        System.out.println("Testing saveMessage for an existing conversation...");
        chatbotService.saveMessage(chatbotMessage);

        // Verifica il comportamento
        System.out.println("Message added to existing conversation for user: " + userId);
        verify(conversazioneRepository, times(1)).findByIdUtente(userId);
        verify(conversazioneRepository, times(1)).save(conversazione);
        assertEquals(1, conversazione.getMessaggi().size(), "The conversation should have 1 message");
    }

    @Test
    void saveMessage_InvalidMessage() {
        // Configura un messaggio invalido con campi vuoti
        ChatbotMessage chatbotMessage = new ChatbotMessage("testUser", "", "", LocalDateTime.now());

        System.out.println("Testing saveMessage with invalid data...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatbotService.saveMessage(chatbotMessage),
                "Saving an invalid message should throw an exception");

        System.out.println("Exception message: " + exception.getMessage());
        assertEquals("Invalid message data: question and answer must not be null or empty", exception.getMessage());
        verify(conversazioneRepository, never()).save(any(ConversazioneChatbot.class));
    }

}
