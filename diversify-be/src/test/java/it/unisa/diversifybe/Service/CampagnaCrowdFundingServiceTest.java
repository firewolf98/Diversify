package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Repository.CampagnaCrowdFundingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
 * - Campagne esistenti (lista non vuota).
 * - Nessuna campagna (lista vuota).
 * Metodo getCampagnaByIdCampagna:
 * - ID valido con campagna associata.
 * - ID valido senza campagna associata.
 * - ID non valido.
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
 * - Data valida con campagne associate.
 * - Data valida senza campagne associate.
 * Metodo getCampagneByDataPrevistaFine:
 * - Data valida con campagne associate.
 * - Data valida senza campagne associate.
 * Metodo createCampagna:
 * - Dati validi.
 * - Dati non validi.
 * Metodo updateCampagna:
 * - ID valido con dati validi.
 * - ID valido con dati non validi.
 * - ID non valido.
 * Metodo deleteCampagna:
 * - ID valido con campagna associata.
 * - ID valido senza campagna associata.
 * - ID non valido.
 */

class CampagnaCrowdFundingServiceTest {

    @InjectMocks
    private CampagnaCrowdFundingService service;

    @Mock
    private CampagnaCrowdFundingRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCampagne_ShouldReturnNonEmptyList() {
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(repository.findAll()).thenReturn(campagne);

        System.out.println("Testing getAllCampagne with non-empty list...");
        List<CampagnaCrowdFunding> result = service.getAllCampagne();

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllCampagne_ShouldReturnEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        System.out.println("Testing getAllCampagne with empty list...");
        List<CampagnaCrowdFunding> result = service.getAllCampagne();

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getCampagnaByIdCampagna_ShouldReturnCampagna() {
        String id = "1";
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");

        when(repository.findByIdCampagna(id)).thenReturn(List.of(campagna));

        System.out.println("Testing getCampagnaByIdCampagna with valid ID...");
        Optional<CampagnaCrowdFunding> result = service.getCampagnaByIdCampagna(id);

        System.out.println("Result: " + result.orElse(null));
        assertTrue(result.isPresent());
        assertEquals(campagna, result.get());
        verify(repository, times(1)).findByIdCampagna(id);
    }

    @Test
    void getCampagnaByIdCampagna_ShouldReturnEmpty() {
        String id = "1";

        when(repository.findByIdCampagna(id)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagnaByIdCampagna with no matching ID...");
        Optional<CampagnaCrowdFunding> result = service.getCampagnaByIdCampagna(id);

        System.out.println("Result: " + result.orElse(null));
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByIdCampagna(id);
    }

    @Test
    void getCampagnaByIdCampagna_InvalidId() {
        String id = null;

        System.out.println("Testing getCampagnaByIdCampagna with invalid ID...");
        assertThrows(IllegalArgumentException.class, () -> service.getCampagnaByIdCampagna(id));

        System.out.println("Exception thrown as expected for invalid ID.");
        verify(repository, never()).findByIdCampagna(anyString());
    }

    @Test
    void deleteCampagna_ShouldDeleteCampagna() {
        String id = "1";
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");

        when(repository.findByIdCampagna(id)).thenReturn(List.of(campagna));
        doNothing().when(repository).delete(campagna);

        System.out.println("Testing deleteCampagna with valid ID...");
        service.deleteCampagna(id);

        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, times(1)).delete(campagna);
    }

    @Test
    void getCampagneByTitolo_ShouldReturnCampagne() {
        String titolo = "Campaign 1";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", titolo, "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(repository.findByTitolo(titolo)).thenReturn(campagne);

        System.out.println("Testing getCampagneByTitolo with matching titolo...");
        List<CampagnaCrowdFunding> result = service.getCampagneByTitolo(titolo);

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findByTitolo(titolo);
    }

    @Test
    void getCampagneByTitolo_ShouldReturnEmptyList() {
        String titolo = "NonExistingTitle";

        when(repository.findByTitolo(titolo)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByTitolo with no matching titolo...");
        List<CampagnaCrowdFunding> result = service.getCampagneByTitolo(titolo);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByTitolo(titolo);
    }

    @Test
    void getCampagneByTitoloContaining_ShouldReturnCampagne() {
        String keyword = "Campaign";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy"),
                new CampagnaCrowdFunding("2", "Campaign 2", "Description 2", null, LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "France")
        );

        when(repository.findByTitoloContaining(keyword)).thenReturn(campagne);

        System.out.println("Testing getCampagneByTitoloContaining with matching keyword...");
        List<CampagnaCrowdFunding> result = service.getCampagneByTitoloContaining(keyword);

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findByTitoloContaining(keyword);
    }

    @Test
    void getCampagneByTitoloContaining_ShouldReturnEmptyList() {
        String keyword = "NonExistingKeyword";

        when(repository.findByTitoloContaining(keyword)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByTitoloContaining with no matching keyword...");
        List<CampagnaCrowdFunding> result = service.getCampagneByTitoloContaining(keyword);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByTitoloContaining(keyword);
    }

    @Test
    void getCampagneByStato_ShouldReturnCampagne() {
        String stato = "attiva";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), stato, "Italy")
        );

        when(repository.findByStato(stato)).thenReturn(campagne);

        System.out.println("Testing getCampagneByStato with matching stato...");
        List<CampagnaCrowdFunding> result = service.getCampagneByStato(stato);

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findByStato(stato);
    }

    @Test
    void getCampagneByStato_ShouldReturnEmptyList() {
        String stato = "nonEsistente";

        when(repository.findByStato(stato)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByStato with no matching stato...");
        List<CampagnaCrowdFunding> result = service.getCampagneByStato(stato);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByStato(stato);
    }

    @Test
    void getCampagneByDataInizio_ShouldReturnCampagne() {
        LocalDate dataInizio = LocalDate.now();
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, dataInizio, dataInizio.plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(repository.findByDataInizio(dataInizio)).thenReturn(campagne);

        System.out.println("Testing getCampagneByDataInizio with matching data...");
        List<CampagnaCrowdFunding> result = service.getCampagneByDataInizio(dataInizio);

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findByDataInizio(dataInizio);
    }

    @Test
    void getCampagneByDataInizio_ShouldReturnEmptyList() {
        LocalDate dataInizio = LocalDate.now();

        when(repository.findByDataInizio(dataInizio)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByDataInizio with no matching data...");
        List<CampagnaCrowdFunding> result = service.getCampagneByDataInizio(dataInizio);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByDataInizio(dataInizio);
    }

    @Test
    void getCampagneByDataPrevistaFine_ShouldReturnCampagne() {
        LocalDate dataFine = LocalDate.now().plusDays(30);
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), dataFine, new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy")
        );

        when(repository.findByDataPrevistaFine(dataFine)).thenReturn(campagne);

        System.out.println("Testing getCampagneByDataPrevistaFine with matching data...");
        List<CampagnaCrowdFunding> result = service.getCampagneByDataPrevistaFine(dataFine);

        System.out.println("Result: " + result);
        assertEquals(campagne, result);
        verify(repository, times(1)).findByDataPrevistaFine(dataFine);
    }

    @Test
    void getCampagneByDataPrevistaFine_ShouldReturnEmptyList() {
        LocalDate dataFine = LocalDate.now().plusDays(30);

        when(repository.findByDataPrevistaFine(dataFine)).thenReturn(Collections.emptyList());

        System.out.println("Testing getCampagneByDataPrevistaFine with no matching data...");
        List<CampagnaCrowdFunding> result = service.getCampagneByDataPrevistaFine(dataFine);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByDataPrevistaFine(dataFine);
    }

    @Test
    void createCampagna_ShouldSaveAndReturnCampagna() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding("1", "Valid Campaign", "Description", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");

        when(repository.save(campagna)).thenReturn(campagna);

        System.out.println("Testing createCampagna with valid data...");
        CampagnaCrowdFunding result = service.createCampagna(campagna);

        System.out.println("Result: " + result);
        assertEquals(campagna, result);
        verify(repository, times(1)).save(campagna);
    }

    @Test
    void createCampagna_ShouldThrowExceptionForInvalidData() {
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding(); // Oggetto con dati incompleti

        System.out.println("Testing createCampagna with invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.createCampagna(campagna));

        System.out.println("Exception: " + exception.getMessage());
        assertEquals("I dati della campagna non possono essere nulli o incompleti.", exception.getMessage());

        // Non dovrebbe mai invocare repository.save
        verify(repository, never()).save(any(CampagnaCrowdFunding.class));
    }


    @Test
    void updateCampagna_ShouldUpdateAndReturnCampagna() {
        String id = "1";
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding("1", "Old Campaign", "Old Description", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding("1", "Updated Campaign", "Updated Description", null, LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "France");

        when(repository.findByIdCampagna(id)).thenReturn(List.of(existingCampagna));
        when(repository.save(existingCampagna)).thenReturn(updatedCampagna);

        System.out.println("Testing updateCampagna with valid ID and data...");
        CampagnaCrowdFunding result = service.updateCampagna(id, updatedCampagna);

        System.out.println("Result: " + result);
        assertEquals(updatedCampagna, result);
        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, times(1)).save(existingCampagna);
    }

    @Test
    void updateCampagna_ShouldThrowExceptionForInvalidData() {
        String id = "1";
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding("1", "Old Campaign", "Old Description", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");
        CampagnaCrowdFunding invalidCampagna = new CampagnaCrowdFunding();

        when(repository.findByIdCampagna(id)).thenReturn(List.of(existingCampagna));
        when(repository.save(existingCampagna)).thenThrow(new IllegalArgumentException("Invalid updated data"));

        System.out.println("Testing updateCampagna with valid ID but invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.updateCampagna(id, invalidCampagna));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, times(1)).save(existingCampagna);
    }

    @Test
    void updateCampagna_ShouldThrowExceptionForInvalidId() {
        String id = "nonExistentId";
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding("1", "Updated Campaign", "Updated Description", null, LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "France");

        when(repository.findByIdCampagna(id)).thenReturn(Collections.emptyList());

        System.out.println("Testing updateCampagna with invalid ID...");
        Exception exception = assertThrows(RuntimeException.class, () -> service.updateCampagna(id, updatedCampagna));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, never()).save(any(CampagnaCrowdFunding.class));
    }

    @Test
    void deleteCampagna_ShouldDeleteExistingCampagna() {
        String id = "1";
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding("1", "Campaign 1", "Description", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "Italy");

        when(repository.findByIdCampagna(id)).thenReturn(List.of(existingCampagna));
        doNothing().when(repository).delete(existingCampagna);

        System.out.println("Testing deleteCampagna with existing campagna...");
        assertDoesNotThrow(() -> service.deleteCampagna(id));

        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, times(1)).delete(existingCampagna);
    }

    @Test
    void deleteCampagna_ShouldThrowExceptionForNonExistentCampagna() {
        String id = "1";

        when(repository.findByIdCampagna(id)).thenReturn(Collections.emptyList());

        System.out.println("Testing deleteCampagna with non-existent campagna...");
        Exception exception = assertThrows(RuntimeException.class, () -> service.deleteCampagna(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, times(1)).findByIdCampagna(id);
        verify(repository, never()).delete(any(CampagnaCrowdFunding.class));
    }

    @Test
    void deleteCampagna_ShouldThrowExceptionForInvalidId() {
        String id = null;

        System.out.println("Testing deleteCampagna with invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.deleteCampagna(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).findByIdCampagna(anyString());
        verify(repository, never()).delete(any(CampagnaCrowdFunding.class));
    }
}
