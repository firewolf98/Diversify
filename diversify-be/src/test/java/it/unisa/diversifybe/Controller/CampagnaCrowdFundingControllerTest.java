package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Service.CampagnaCrowdFundingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Categorie individuate secondo la metodologia Category Partition:
 * Metodo getAllCampagne:
 * - Lista di campagne non vuota.
 * - Lista di campagne vuota.
 * Metodo getCampagneByPaese:
 * - Paese con campagne associate.
 * - Paese senza campagne associate.
 * Metodo getCampagnaByIdCampagna:
 * - ID valido con campagna associata.
 * - ID non valido senza campagna associata.
 * Metodo getCampagneByTitolo:
 * - Titolo esatto con campagne associate.
 * - Titolo esatto senza campagne associate.
 * Metodo getCampagneByTitoloContaining:
 * - Keyword presente in uno o più titoli.
 * - Keyword non presente in alcun titolo.
 * Metodo getCampagneByStato:
 * - Stato valido con campagne associate.
 * - Stato valido senza campagne associate.
 * Metodo getCampagneByDataInizio:
 * - Data di inizio valida con campagne associate.
 * - Data di inizio valida senza campagne associate.
 * Metodo getCampagneByDataPrevistaFine:
 * - Data di fine valida con campagne associate.
 * - Data di fine valida senza campagne associate.
 * Metodo createCampagna:
 * - Dati validi e ruolo valido.
 * - Dati validi ma ruolo non valido.
 * - Dati non validi.
 * Metodo updateCampagna:
 * - ID valido con dati validi e ruolo valido.
 * - ID valido con dati validi ma ruolo non valido.
 * - ID non valido.
 * Metodo deleteCampagna:
 * - ID valido e ruolo valido.
 * - ID valido ma ruolo non valido.
 */

class CampagnaCrowdFundingControllerTest {

    @InjectMocks
    private CampagnaCrowdFundingController controller;

    @Mock
    private CampagnaCrowdFundingService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCampagne_ShouldReturnCampagneList() {
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy"),
                new CampagnaCrowdFunding("2", "Campaign 2", "Description 2", null, LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "France")
        );

        when(service.getAllCampagne()).thenReturn(campagne);

        System.out.println("Testing getAllCampagne with a non-empty list...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getAllCampagne();

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Country: " + c.getPaese()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getAllCampagne();
    }

    @Test
    void getAllCampagne_ShouldReturnEmptyList() {
        when(service.getAllCampagne()).thenReturn(Collections.emptyList());

        System.out.println("Testing getAllCampagne with an empty list...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getAllCampagne();

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + (response.getBody().isEmpty() ? "Empty list" : response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getAllCampagne();
    }

    @Test
    void getCampagneByPaese_ShouldReturnCampagneList() {
        String paese = "Italy";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", paese)
        );

        when(service.findCampagneByPaese(paese)).thenReturn(campagne);

        System.out.println("Testing getCampagneByPaese...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByPaese(paese);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Country: " + c.getPaese()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).findCampagneByPaese(paese);
    }

    @Test
    void getCampagneByPaese_ShouldReturnNotFound() {
        String paese = "Unknown";
        when(service.findCampagneByPaese(paese)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByPaese with no results...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByPaese(paese);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified country.");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, times(1)).findCampagneByPaese(paese);
    }


    @Test
    void getCampagnaByIdCampagna_ShouldReturnCampagna() {
        String idCampagna = "1";
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");

        when(service.getCampagnaByIdCampagna(idCampagna)).thenReturn(Optional.of(campagna));

        System.out.println("Testing getCampagnaByIdCampagna...");
        ResponseEntity<CampagnaCrowdFunding> response = controller.getCampagnaByIdCampagna(idCampagna);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagna, response.getBody());
        verify(service, times(1)).getCampagnaByIdCampagna(idCampagna);
    }

    @Test
    void getCampagnaByIdCampagna_ShouldReturnNotFound() {
        String idCampagna = "invalidId";

        when(service.getCampagnaByIdCampagna(idCampagna)).thenReturn(Optional.empty());

        System.out.println("Testing getCampagnaByIdCampagna with invalid ID...");
        ResponseEntity<CampagnaCrowdFunding> response = controller.getCampagnaByIdCampagna(idCampagna);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified country.");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, times(1)).getCampagnaByIdCampagna(idCampagna);
    }

    @Test
    void getCampagneByTitolo_ShouldReturnCampagneList() {
        String titolo = "Campaign 1";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", titolo, "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(service.getCampagneByTitolo(titolo)).thenReturn(campagne);

        System.out.println("Testing getCampagneByTitolo with campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByTitolo(titolo);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Country: " + c.getPaese()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getCampagneByTitolo(titolo);
    }

    @Test
    void getCampagneByTitolo_ShouldReturnEmptyList() {
        String titolo = "NonExistentTitle";

        when(service.getCampagneByTitolo(titolo)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByTitolo with no campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByTitolo(titolo);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified title.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getCampagneByTitolo(titolo);
    }

    @Test
    void getCampagneByTitoloContaining_ShouldReturnCampagneList() {
        String keyword = "Campaign";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy"),
                new CampagnaCrowdFunding("2", "Another Campaign", "Description 2", null, LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "France")
        );

        when(service.getCampagneByTitoloContaining(keyword)).thenReturn(campagne);

        System.out.println("Testing getCampagneByTitoloContaining with campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByTitoloContaining(keyword);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Country: " + c.getPaese()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getCampagneByTitoloContaining(keyword);
    }

    @Test
    void getCampagneByTitoloContaining_ShouldReturnEmptyList() {
        String keyword = "NonExistentKeyword";

        when(service.getCampagneByTitoloContaining(keyword)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByTitoloContaining with no campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByTitoloContaining(keyword);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified keyword.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getCampagneByTitoloContaining(keyword);
    }

    @Test
    void getCampagneByStato_ShouldReturnCampagneList() {
        String stato = "attiva";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), stato, "Italy")
        );

        when(service.getCampagneByStato(stato)).thenReturn(campagne);

        System.out.println("Testing getCampagneByStato with campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByStato(stato);

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Status: " + c.getStato()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getCampagneByStato(stato);
    }

    @Test
    void getCampagneByStato_ShouldReturnEmptyList() {
        String stato = "nonexistent";

        when(service.getCampagneByStato(stato)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByStato with no campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByStato(stato);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified status.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getCampagneByStato(stato);
    }

    @Test
    void getCampagneByDataInizio_ShouldReturnCampagneList() {
        LocalDate dataInizio = LocalDate.of(2024, 1, 1);
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, dataInizio, dataInizio.plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(service.getCampagneByDataInizio(dataInizio)).thenReturn(campagne);

        System.out.println("Testing getCampagneByDataInizio with campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByDataInizio(dataInizio.toString());

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", Start Date: " + c.getDataInizio()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getCampagneByDataInizio(dataInizio);
    }

    @Test
    void getCampagneByDataInizio_ShouldReturnEmptyList() {
        LocalDate dataInizio = LocalDate.of(2024, 1, 1);

        when(service.getCampagneByDataInizio(dataInizio)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByDataInizio with no campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByDataInizio(dataInizio.toString());

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified start date.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getCampagneByDataInizio(dataInizio);
    }

    @Test
    void getCampagneByDataPrevistaFine_ShouldReturnCampagneList() {
        LocalDate dataPrevistaFine = LocalDate.of(2024, 12, 31);
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), dataPrevistaFine, new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(service.getCampagneByDataPrevistaFine(dataPrevistaFine)).thenReturn(campagne);

        System.out.println("Testing getCampagneByDataPrevistaFine with campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByDataPrevistaFine(dataPrevistaFine.toString());

        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Response status: " + response.getStatusCode());
        response.getBody().forEach(c -> System.out.println("Campaign ID: " + c.getIdCampagna() + ", Title: " + c.getTitolo() + ", End Date: " + c.getDataPrevistaFine()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campagne, response.getBody());
        verify(service, times(1)).getCampagneByDataPrevistaFine(dataPrevistaFine);
    }

    @Test
    void getCampagneByDataPrevistaFine_ShouldReturnEmptyList() {
        LocalDate dataPrevistaFine = LocalDate.of(2024, 12, 31);

        when(service.getCampagneByDataPrevistaFine(dataPrevistaFine)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByDataPrevistaFine with no campaigns found...");
        ResponseEntity<List<CampagnaCrowdFunding>> response = controller.getCampagneByDataPrevistaFine(dataPrevistaFine.toString());

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: No campaigns found for the specified end date.");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getCampagneByDataPrevistaFine(dataPrevistaFine);
    }

    @Test
    void deleteCampagna_ValidIdAndRole() {
        String id = "1";
        boolean ruolo = true;

        doNothing().when(service).deleteCampagna(id);

        System.out.println("Testing deleteCampagna with valid ID and role...");
        ResponseEntity<Void> response = controller.deleteCampagna(id, ruolo);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteCampagna(id);
    }

    @Test
    void deleteCampagna_ValidIdInvalidRole() {
        String id = "1";
        boolean ruolo = false;

        System.out.println("Testing deleteCampagna with valid ID and invalid role...");
        ResponseEntity<Void> response = controller.deleteCampagna(id, ruolo);

        System.out.println("Response status: " + response.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(service, never()).deleteCampagna(anyString());
    }

}
