package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    /*
     *   Metodo banUser:
     *   - Utente Esistente: L'utente viene trovato ed è stato bannato con successo.
     *   - Utente Non Esistente: Lancia un'eccezione.
     *
     *   Metodo createForumAsAdmin:
     *   - Ruolo Valido: L'amministratore crea un forum con successo.
     *   - Ruolo Non Valido: Lancia un'eccezione.
     *
     *   Metodo updateForumAsAdmin:
     *   - Ruolo Valido: L'amministratore aggiorna un forum con successo.
     *   - Ruolo Non Valido: Lancia un'eccezione.
     *
     *   Metodo createCampaignAsAdmin:
     *   - Ruolo Valido: L'amministratore crea una campagna con successo.
     *   - Ruolo Non Valido: Lancia un'eccezione.
     *
     *    Metodo updateCampaignAsAdmin:
     *   - Ruolo Valido: L'amministratore aggiorna una campagna con successo.
     *   - Ruolo Non Valido: Lancia un'eccezione.
     */

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private ForumService forumService;

    @Mock
    private CampagnaCrowdFundingService campagnaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void banUser_UserExists() {
        String username = "testUser";
        Utente utente = new Utente();
        utente.setUsername(username);

        when(utenteRepository.findByUsername(username)).thenReturn(Optional.of(utente));

        System.out.println("Testing banUser with existing user...");
        adminService.banUser(username);

        System.out.println("User banned successfully. Output: " + utente.isBanned());
        assertTrue(utente.isBanned());
        verify(utenteRepository, times(1)).save(utente);
    }

    @Test
    void banUser_UserDoesNotExist() {
        String username = "unknownUser";

        when(utenteRepository.findByUsername(username)).thenReturn(Optional.empty());

        System.out.println("Testing banUser with non-existing user...");
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> adminService.banUser(username));

        System.out.println("Expected exception thrown. Output: " + exception.getMessage());
        verify(utenteRepository, never()).save(any(Utente.class));
    }

    @Test
    void createForumAsAdmin_ValidRole() {
        Forum forum = new Forum();
        forum.setTitolo("Test Forum");
        forum.setDescrizione("This is a test forum.");
        forum.setPaese("Italy");
        boolean ruolo = true;

        when(forumService.addForum(any(Forum.class), eq(true))).thenAnswer(invocation -> {
            Forum inputForum = invocation.getArgument(0);
            inputForum.setIdForum("forum123");
            return inputForum;
        });

        System.out.println("Testing createForumAsAdmin with valid role...");
        Forum createdForum = adminService.createForumAsAdmin(forum, ruolo);

        System.out.println("Forum created successfully. Output: " + createdForum);
        assertNotNull(createdForum);
        assertEquals("forum123", createdForum.getIdForum());
        assertEquals("Test Forum", createdForum.getTitolo());
        assertEquals("Italy", createdForum.getPaese());
        verify(forumService, times(1)).addForum(forum, true);
    }



    @Test
    void createForumAsAdmin_InvalidRole() {
        Forum forum = new Forum();
        boolean ruolo = false;

        System.out.println("Testing createForumAsAdmin with invalid role...");
        SecurityException exception = assertThrows(SecurityException.class, () -> adminService.createForumAsAdmin(forum, ruolo));

        System.out.println("Expected exception thrown. Output: " + exception.getMessage());
        verify(forumService, never()).addForum(any(Forum.class), anyBoolean());
    }

    @Test
    void updateForumAsAdmin_ValidRole() {
        String id = "forum123";
        Forum updatedForum = new Forum();
        updatedForum.setTitolo("Updated Forum");
        updatedForum.setDescrizione("This forum has been updated.");
        updatedForum.setPaese("Germany");
        boolean ruolo = true;

        when(forumService.updateForum(eq(id), any(Forum.class), eq(true))).thenAnswer(invocation -> {
            Forum inputForum = invocation.getArgument(1);
            inputForum.setIdForum(id);
            return inputForum;
        });

        System.out.println("Testing updateForumAsAdmin with valid role...");
        Forum resultForum = adminService.updateForumAsAdmin(id, updatedForum, ruolo);

        System.out.println("Forum updated successfully. Output: " + resultForum);
        assertNotNull(resultForum);
        assertEquals("forum123", resultForum.getIdForum());
        assertEquals("Updated Forum", resultForum.getTitolo());
        assertEquals("Germany", resultForum.getPaese());
        verify(forumService, times(1)).updateForum(id, updatedForum, true);
    }

    @Test
    void updateForumAsAdmin_InvalidRole() {
        String id = "123";
        Forum updatedForum = new Forum();
        boolean ruolo = false;

        System.out.println("Testing updateForumAsAdmin with invalid role...");
        SecurityException exception = assertThrows(SecurityException.class, () -> adminService.updateForumAsAdmin(id, updatedForum, ruolo));

        System.out.println("Expected exception thrown. Output: " + exception.getMessage());
        verify(forumService, never()).updateForum(anyString(), any(Forum.class), anyBoolean());
    }

    @Test
    void createCampaignAsAdmin_ValidRole() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding();
        campagna.setTitolo("Test Campaign");
        campagna.setContenuto("This is a test campaign.");
        campagna.setPaese("Italy");
        campagna.setSommaDaRaccogliere(new BigDecimal("10000"));
        campagna.setDataInizio(LocalDate.of(2024, 1, 1));
        campagna.setDataPrevistaFine(LocalDate.of(2024, 12, 31));
        boolean ruolo = true;

        when(campagnaService.createCampagna(any(CampagnaCrowdFunding.class))).thenAnswer(invocation -> {
            CampagnaCrowdFunding inputCampagna = invocation.getArgument(0);
            inputCampagna.setIdCampagna("campaign123");
            inputCampagna.setSommaRaccolta(new BigDecimal("0"));
            inputCampagna.setStato("attiva");
            return inputCampagna;
        });

        System.out.println("Testing createCampaignAsAdmin with valid role...");
        CampagnaCrowdFunding createdCampaign = adminService.createCampaignAsAdmin(campagna, ruolo);

        System.out.println("Campaign created successfully. Output: " + createdCampaign);
        assertNotNull(createdCampaign);
        assertEquals("campaign123", createdCampaign.getIdCampagna());
        assertEquals("Test Campaign", createdCampaign.getTitolo());
        assertEquals("Italy", createdCampaign.getPaese());
        assertEquals(new BigDecimal("0"), createdCampaign.getSommaRaccolta());
        assertEquals("attiva", createdCampaign.getStato());
        verify(campagnaService, times(1)).createCampagna(campagna);
    }



    @Test
    void createCampaignAsAdmin_InvalidRole() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding();
        boolean ruolo = false;

        System.out.println("Testing createCampaignAsAdmin with invalid role...");
        SecurityException exception = assertThrows(SecurityException.class, () -> adminService.createCampaignAsAdmin(campagna, ruolo));

        System.out.println("Expected exception thrown. Output: " + exception.getMessage());
        verify(campagnaService, never()).createCampagna(any(CampagnaCrowdFunding.class));
    }

    @Test
    void updateCampaignAsAdmin_ValidRole() {
        String id = "campaign123";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding();
        updatedCampagna.setTitolo("Updated Campaign");
        updatedCampagna.setContenuto("This campaign has been updated.");
        updatedCampagna.setPaese("France");
        updatedCampagna.setStato("terminata");
        boolean ruolo = true;

        when(campagnaService.updateCampagna(eq(id), any(CampagnaCrowdFunding.class))).thenAnswer(invocation -> {
            CampagnaCrowdFunding inputCampagna = invocation.getArgument(1);
            inputCampagna.setIdCampagna(id);
            return inputCampagna;
        });

        System.out.println("Testing updateCampaignAsAdmin with valid role...");
        CampagnaCrowdFunding resultCampaign = adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo);

        System.out.println("Campaign updated successfully. Output: " + resultCampaign);
        assertNotNull(resultCampaign);
        assertEquals("campaign123", resultCampaign.getIdCampagna());
        assertEquals("Updated Campaign", resultCampaign.getTitolo());
        assertEquals("France", resultCampaign.getPaese());
        assertEquals("terminata", resultCampaign.getStato());
        verify(campagnaService, times(1)).updateCampagna(id, updatedCampagna);
    }

    @Test
    void updateCampaignAsAdmin_InvalidRole() {
        String id = "campaign123";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding();
        updatedCampagna.setTitolo("Updated Campaign");
        updatedCampagna.setContenuto("This campaign has been updated.");
        updatedCampagna.setPaese("France");
        boolean ruolo = false;

        System.out.println("Testing updateCampaignAsAdmin with invalid role...");
        SecurityException exception = assertThrows(SecurityException.class, () -> adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo));

        System.out.println("Expected exception thrown. Output: " + exception.getMessage());
        verify(campagnaService, never()).updateCampagna(anyString(), any(CampagnaCrowdFunding.class));
    }


}
