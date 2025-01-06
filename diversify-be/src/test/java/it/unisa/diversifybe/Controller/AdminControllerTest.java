package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    /*
     *   Metodo banUser:
     *   - Caso Utente Esistente: Verifica che un utente valido venga bannato con successo.
     *   - Caso Utente Non Esistente: Simula un errore durante il tentativo di bannare un utente non valido.
     *
     *   Metodo createForum:
     *   - Forum Valido: Verifica che un forum correttamente formato venga creato con successo.
     *   - Forum Non Valido: Simula un errore durante la creazione di un forum con dati incompleti o non validi.
     *
     *   Metodo updateForum:
     *   - ID Forum Valido: Verifica che un forum esistente venga aggiornato con successo.
     *   - ID Forum Non Valido: Simula un errore durante il tentativo di aggiornare un forum inesistente.
     *   - Forum Non Valido: Simula un errore durante l'aggiornamento con dati incompleti o non validi.
     *
     *   Metodo createCampaign:
     *   - Campagna Valida: Verifica che una campagna correttamente formata venga creata con successo.
     *   - Campagna Non Valida: Simula un errore durante la creazione di una campagna con dati incompleti o non validi.
     *
     *   Metodo updateCampaign:
     *   - ID Campagna Valido: Verifica che una campagna esistente venga aggiornata con successo.
     *   - ID Campagna Non Valido: Simula un errore durante il tentativo di aggiornare una campagna inesistente.
     *   - Campagna Non Valida: Simula un errore durante l'aggiornamento con dati incompleti o non validi.
     */

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void banUser_UserExists() {
        String username = "testUser";

        doNothing().when(adminService).banUser(username);

        System.out.println("Testing banUser with existing user...");
        ResponseEntity<Void> response = adminController.banUser(username);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService, times(1)).banUser(username);
    }

    @Test
    void banUser_UserDoesNotExist() {
        String username = "unknownUser";

        doThrow(new RuntimeException("User not found")).when(adminService).banUser(username);

        System.out.println("Testing banUser with non-existing user...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.banUser(username));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).banUser(username);
    }

    @Test
    void createForum_ValidData() {
        Forum forum = new Forum();
        forum.setTitolo("Test Forum");
        forum.setDescrizione("This is a test forum.");
        forum.setPaese("Italy");
        boolean ruolo = true;

        when(adminService.createForumAsAdmin(forum, ruolo)).thenReturn(forum);

        System.out.println("Testing createForum with valid data...");
        ResponseEntity<Forum> response = adminController.createForum(forum, ruolo);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: Forum Title: " + response.getBody().getTitolo() + ", Country: " + response.getBody().getPaese());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(forum, response.getBody());
        verify(adminService, times(1)).createForumAsAdmin(forum, ruolo);
    }

    @Test
    void createForum_InvalidData() {
        Forum forum = new Forum();
        forum.setTitolo(null);
        boolean ruolo = true;

        when(adminService.createForumAsAdmin(forum, ruolo)).thenThrow(new RuntimeException("Invalid forum data"));

        System.out.println("Testing createForum with invalid data...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.createForum(forum, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).createForumAsAdmin(forum, ruolo);
    }

    @Test
    void updateForum_ValidData() {
        String id = "123";
        Forum updatedForum = new Forum();
        updatedForum.setTitolo("Updated Forum");
        updatedForum.setDescrizione("This forum has been updated.");
        updatedForum.setPaese("Germany");
        boolean ruolo = true;

        when(adminService.updateForumAsAdmin(id, updatedForum, ruolo)).thenReturn(updatedForum);

        System.out.println("Testing updateForum with valid data...");
        ResponseEntity<Forum> response = adminController.updateForum(id, updatedForum, ruolo);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: Forum Title: " + response.getBody().getTitolo() + ", Country: " + response.getBody().getPaese());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedForum, response.getBody());
        verify(adminService, times(1)).updateForumAsAdmin(id, updatedForum, ruolo);
    }


    @Test
    void updateForum_InvalidId() {
        String id = "invalidId";
        Forum updatedForum = new Forum();
        boolean ruolo = true;

        when(adminService.updateForumAsAdmin(id, updatedForum, ruolo)).thenThrow(new RuntimeException("Invalid forum ID"));

        System.out.println("Testing updateForum with invalid ID...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.updateForum(id, updatedForum, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).updateForumAsAdmin(id, updatedForum, ruolo);
    }

    @Test
    void updateForum_InvalidData() {
        String id = "123";
        Forum updatedForum = new Forum();
        boolean ruolo = true;

        when(adminService.updateForumAsAdmin(id, updatedForum, ruolo)).thenThrow(new RuntimeException("Invalid forum data"));

        System.out.println("Testing updateForum with invalid data...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.updateForum(id, updatedForum, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).updateForumAsAdmin(id, updatedForum, ruolo);
    }

    @Test
    void createCampaign_ValidData() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding();
        campagna.setTitolo("Test Campaign");
        campagna.setDescrizione("This is a test campaign.");
        campagna.setPaese("Italy");
        campagna.setSommaDaRaccogliere(new BigDecimal("10000"));
        campagna.setDataInizio(LocalDate.of(2024, 1, 1));
        campagna.setDataPrevistaFine(LocalDate.of(2024, 12, 31));
        boolean ruolo = true;

        when(adminService.createCampaignAsAdmin(campagna, ruolo)).thenReturn(campagna);

        System.out.println("Testing createCampaign with valid data...");
        ResponseEntity<CampagnaCrowdFunding> response = adminController.createCampaign(campagna, ruolo);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: Campaign Title: " + response.getBody().getTitolo() + ", Country: " + response.getBody().getPaese());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(campagna, response.getBody());
        verify(adminService, times(1)).createCampaignAsAdmin(campagna, ruolo);
    }

    @Test
    void createCampaign_InvalidData() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding();
        boolean ruolo = true;


        when(adminService.createCampaignAsAdmin(campagna, ruolo)).thenThrow(new RuntimeException("Invalid campaign data"));

        System.out.println("Testing createCampaign with invalid data...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.createCampaign(campagna, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).createCampaignAsAdmin(campagna, ruolo);
    }

    @Test
    void updateCampaign_ValidData() {
        String id = "123";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding();
        updatedCampagna.setTitolo("Updated Campaign");
        updatedCampagna.setDescrizione("This campaign has been updated.");
        updatedCampagna.setPaese("France");
        updatedCampagna.setStato("terminata");
        boolean ruolo = true;

        when(adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo)).thenReturn(updatedCampagna);

        System.out.println("Testing updateCampaign with valid data...");
        ResponseEntity<CampagnaCrowdFunding> response = adminController.updateCampaign(id, updatedCampagna, ruolo);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: Campaign Title: " + response.getBody().getTitolo() + ", Country: " + response.getBody().getPaese());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCampagna, response.getBody());
        verify(adminService, times(1)).updateCampaignAsAdmin(id, updatedCampagna, ruolo);
    }

    @Test
    void updateCampaign_InvalidId() {
        String id = "invalidId";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding();
        boolean ruolo = true;

        when(adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo)).thenThrow(new RuntimeException("Invalid campaign ID"));

        System.out.println("Testing updateCampaign with invalid ID...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.updateCampaign(id, updatedCampagna, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).updateCampaignAsAdmin(id, updatedCampagna, ruolo);
    }

    @Test
    void updateCampaign_InvalidData() {
        String id = "123";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding();
        boolean ruolo = true;

        when(adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo)).thenThrow(new RuntimeException("Invalid campaign data"));

        System.out.println("Testing updateCampaign with invalid data...");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.updateCampaign(id, updatedCampagna, ruolo));

        System.out.println("Exception message: " + exception.getMessage());
        verify(adminService, times(1)).updateCampaignAsAdmin(id, updatedCampagna, ruolo);
    }
}
