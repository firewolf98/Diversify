package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.DTO.ChatbotMessage;
import it.unisa.diversifybe.Service.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatbotControllerTest {

    /*
    *   Metodo getConversations:
    *   Caso Utente Trovato con Conversazioni: Simula un utente esistente con dati associati.
    *   Caso Utente Trovato Senza Conversazioni: Simula un utente esistente ma senza conversazioni.
    *   Caso Utente Non Trovato: Gestisce errori nel servizio (ad esempio, utente inesistente).
    *
    *   Metodo saveMessage:
    *   Messaggio Valido: Verifica che un messaggio correttamente formato sia salvato senza errori.
    *   Messaggio Non Valido: Simula un errore nel salvataggio (messaggio vuoto o dati errati).
    */

    @InjectMocks
    private ChatbotController chatbotController;

    @Mock
    private ChatbotService chatbotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getConversations_UserExistsWithMessages() {
        //configurazione dei dati di test
        String userId = "testUser";
        LocalDateTime timestamp = LocalDateTime.now();

        List<ChatbotMessage> messages = new ArrayList<>();
        messages.add(new ChatbotMessage(userId, "Hello?", "Hi there!", timestamp));
        messages.add(new ChatbotMessage(userId, "How are you?", "I’m doing fine, thank you!", timestamp));

        //configura il mock del service
        when(chatbotService.getUserConversations(userId)).thenReturn(messages);

        //esegue il metodo del controller
        System.out.println("Testing getConversations with existing user and messages...");
        ResponseEntity<List<ChatbotMessage>> response = chatbotController.getConversations(userId);

        //stampa l'output del test
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        //verifica i risultati con le assertion
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(2, response.getBody().size(), "Response body should contain 2 messages");
        assertEquals(messages, response.getBody(), "Response body should match the expected messages");

        //verifica che il metodo del service sia stato chiamato una volta
        verify(chatbotService, times(1)).getUserConversations(userId);
    }

    @Test
    void getConversations_UserExistsWithoutMessages() {
        String userId = "testUser";

        when(chatbotService.getUserConversations(userId)).thenReturn(Collections.emptyList());

        System.out.println("Testing getConversations with existing user but no messages...");
        ResponseEntity<List<ChatbotMessage>> response = chatbotController.getConversations(userId);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(chatbotService, times(1)).getUserConversations(userId);
    }

    @Test
    void getConversations_UserDoesNotExist() {
        String userId = "unknownUser";

        when(chatbotService.getUserConversations(userId)).thenThrow(new RuntimeException("User not found"));

        System.out.println("Testing getConversations with non-existent user...");
        ResponseEntity<List<ChatbotMessage>> response = chatbotController.getConversations(userId);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(chatbotService, times(1)).getUserConversations(userId);
    }

    @Test
    void saveMessage_ValidMessage() {
        ChatbotMessage chatbotMessage = new ChatbotMessage("testUser", "Hi there!", "Hello!", LocalDateTime.now());

        doNothing().when(chatbotService).saveMessage(chatbotMessage);

        System.out.println("Testing saveMessage with valid message...");
        ResponseEntity<Void> response = chatbotController.saveMessage(chatbotMessage);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(chatbotService, times(1)).saveMessage(chatbotMessage);
    }

    @Test
    void saveMessage_InvalidMessage() {
        ChatbotMessage chatbotMessage = new ChatbotMessage("testUser", "", "", LocalDateTime.now());

        doThrow(new RuntimeException("Invalid message data")).when(chatbotService).saveMessage(chatbotMessage);

        System.out.println("Testing saveMessage with invalid message...");
        ResponseEntity<Void> response = chatbotController.saveMessage(chatbotMessage);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(chatbotService, times(1)).saveMessage(chatbotMessage);
    }
}
