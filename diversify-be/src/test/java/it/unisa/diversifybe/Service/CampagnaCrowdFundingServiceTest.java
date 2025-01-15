package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.CampagnaCrowdFundingRepository;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private PaeseRepository paeseRepository;

    @Mock
    private CampagnaCrowdFundingRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCampagne_ShouldReturnNonEmptyList() {
        // Creazione della campagna
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding(
                "1", "Campaign 1", "Categoria 1", "Description 1",
                LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1000"), new BigDecimal("500"),
                "attiva", "ItalyImage", "link.com", "Italy");

        // Creazione del paese contenente la campagna
        Paese paese = new Paese(
                "1", "IT", "Italy", List.of(), List.of(campagna),
                List.of(), "linkToFlag", List.of());

        // Configurazione del mock per paeseRepository
        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        System.out.println("Testing getAllCampagne with non-empty list...");

        // Esecuzione del metodo
        List<CampagnaCrowdFunding> result = service.getAllCampagne();

        System.out.println("Result: " + result);

        // Verifica dei risultati
        assertEquals(1, result.size());

        assertEquals(campagna, result.getFirst());
        verify(paeseRepository, times(1)).findAll();
    }


    @Test
    void getAllCampagne_ShouldReturnEmptyList() {
        // Configurazione del mock per paeseRepository
        when(paeseRepository.findAll()).thenReturn(Collections.emptyList());

        System.out.println("Testing getAllCampagne with empty list...");

        // Esecuzione del metodo
        List<CampagnaCrowdFunding> result = service.getAllCampagne();

        System.out.println("Result: " + result);

        // Verifica del risultato
        assertTrue(result.isEmpty()); // Verifica che la lista sia vuota

        // Verifica delle interazioni con il mock
        verify(paeseRepository, times(1)).findAll();
    }


    @Test
    void getCampagnaByIdCampagna_ShouldReturnCampagna() {
        String id = "1";
        // Creazione della campagna
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding("1", "Campaign 1", "Categoria 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "ItalyImage","link.com", "Italy");

        // Creazione del paese con la campagna associata
        Paese paese = new Paese("1", "IT", "Italy", List.of(), Collections.singletonList(campagna), List.of(), "link1", List.of());

        // Mock del comportamento del repository Paese
        when(paeseRepository.findAll()).thenReturn(Collections.singletonList(paese));

        System.out.println("Testing getCampagnaByIdCampagna with valid ID...");
        Optional<CampagnaCrowdFunding> result = service.getCampagnaByIdCampagna(id);

        System.out.println("Result: " + result.orElse(null));
        assertTrue(result.isPresent());
        assertEquals(campagna, result.get());
        verify(paeseRepository, times(1)).findAll(); // Verifica che il metodo findAll sia stato chiamato
    }

    @Test
    void getCampagnaByIdCampagna_ShouldReturnEmpty() {
        String id = "1";

        // Creazione di un paese con nessuna campagna corrispondente
        when(paeseRepository.findAll()).thenReturn(Collections.emptyList()); // Nessun paese da restituire

        System.out.println("Testing getCampagnaByIdCampagna with no matching ID...");
        Optional<CampagnaCrowdFunding> result = service.getCampagnaByIdCampagna(id);

        System.out.println("Result: " + result.orElse(null));
        assertTrue(result.isEmpty());
        verify(paeseRepository, times(1)).findAll(); // Verifica che il metodo findAll sia stato chiamato
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
    void getCampagneByTitolo_ShouldReturnCampagne() {
        String titolo = "Campaign 1";
        List<CampagnaCrowdFunding> campagne = List.of(
                new CampagnaCrowdFunding("1", titolo, "Categoria 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "ItalyImage","link.com", "Italy")
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
                new CampagnaCrowdFunding("1", "Campaign 1", "Categoria 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "ItalyImage","link.com" ,"Italy"),
                new CampagnaCrowdFunding("2", "Campaign 2", "Categoria 2", "Description 2", LocalDate.now(), LocalDate.now().plusDays(60), new BigDecimal("2000"), new BigDecimal("1500"), "attiva", "FranceImage","link.com" , "France"),
                new CampagnaCrowdFunding("3", "Campaign 3", "Categoria 3", "Description 3", LocalDate.now(), LocalDate.now().plusDays(45), new BigDecimal("3000"), new BigDecimal("2500"), "attiva", "GermanyImage","link.com" , "Germany")
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
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), LocalDate.now().plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), stato, "ItalyImage","link.com" , "Italy")
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
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, dataInizio, dataInizio.plusDays(30), new BigDecimal("1000"), new BigDecimal("500"), "attiva", "ItalyImage","link.com" , "Italy")
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
                new CampagnaCrowdFunding("1", "Campaign 1", "Description 1", null, LocalDate.now(), dataFine, new BigDecimal("1000"), new BigDecimal("500"), "attiva", "ItalyImage","link.com" , "Italy")
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
        // Dati di test
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding(
                "1", "Valid Campaign", "Description", null,
                LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1000"), new BigDecimal("500"),
                "attiva", "ItalyImage", "link.com", "Italy");

        Paese paese = new Paese(
                "1", "IT", "Italy", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), "linkToFlag", new ArrayList<>());

        // Configurazione dei mock
        when(paeseRepository.findByNome("Italy")).thenReturn(List.of(paese));
        when(repository.save(campagna)).thenReturn(campagna);
        when(paeseRepository.save(paese)).thenReturn(paese);

        System.out.println("Testing createCampagna with valid data...");

        // Esecuzione del metodo
        CampagnaCrowdFunding result = service.createCampagna(campagna);

        System.out.println("Result: " + result);

        // Verifica del risultato
        assertEquals(campagna, result);
        assertTrue(paese.getCampagneCrowdfunding().contains(campagna));

        // Verifica delle interazioni con i repository
        verify(paeseRepository, times(1)).findByNome("Italy");
        verify(paeseRepository, times(1)).save(paese);
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

        // Dati della campagna esistente
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding(
                "1", "Old Campaign", "Old Description", null,
                LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1000"), new BigDecimal("500"),
                "attiva", "ItalyImage", "link.com", "Italy");

        // Dati della campagna aggiornata
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding(
                "1", "Updated Campaign", "Updated Description", null,
                LocalDate.now(), LocalDate.now().plusDays(60),
                new BigDecimal("2000"), new BigDecimal("1500"),
                "attiva", "FranceImage", "link.com", "France");

        // Creazione del paese contenente la campagna
        Paese paese = new Paese(
                "1", "IT", "Italy", List.of(), List.of(existingCampagna),
                List.of(), "linkToFlag", List.of());

        // Configurazione del mock per paeseRepository
        when(paeseRepository.findAll()).thenReturn(List.of(paese));
        when(paeseRepository.save(paese)).thenReturn(paese);

        System.out.println("Testing updateCampagna with valid ID and data...");

        // Esecuzione del metodo
        CampagnaCrowdFunding result = service.updateCampagna(id, updatedCampagna);

        System.out.println("Result: " + result);

        // Verifica dei risultati
        assertEquals(updatedCampagna.getTitolo(), result.getTitolo());
        assertEquals(updatedCampagna.getDescrizione(), result.getDescrizione());
        assertEquals(updatedCampagna.getDataPrevistaFine(), result.getDataPrevistaFine());
        assertEquals(updatedCampagna.getPaese(), result.getPaese());
        verify(paeseRepository, times(1)).findAll();
        verify(paeseRepository, times(1)).save(paese);
    }


    @Test
    void updateCampagna_ShouldThrowExceptionForInvalidData() {
        String id = "1";

        // Campagna esistente
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding(
                "1", "Old Campaign", "Old Description", null,
                LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1000"), new BigDecimal("500"),
                "attiva", "ItalyImage", "link.com", "Italy");

        // Campagna invalida (vuota)
        CampagnaCrowdFunding invalidCampagna = new CampagnaCrowdFunding();

        // Paese contenente la campagna esistente
        Paese paese = new Paese(
                "1", "IT", "Italy", List.of(), List.of(existingCampagna),
                List.of(), "linkToFlag", List.of());

        // Configurazione del mock per il repository
        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        System.out.println("Testing updateCampagna with valid ID but invalid data...");

        // Esecuzione e verifica
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.updateCampagna(id, invalidCampagna));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseRepository, never()).save(paese); // Non dovrebbe salvare il paese
    }


    @Test
    void updateCampagna_ShouldThrowExceptionForInvalidId() {
        String id = "nonExistentId";

        // Campagna da aggiornare
        CampagnaCrowdFunding updatedCampagna = new CampagnaCrowdFunding(
                "1", "Updated Campaign", "Updated Description", null,
                LocalDate.now(), LocalDate.now().plusDays(60),
                new BigDecimal("2000"), new BigDecimal("1500"),
                "attiva", "FranceImage", "link.com", "France");

        // Paese senza campagne
        Paese paese = new Paese(
                "1", "IT", "Italy", List.of(), List.of(),
                List.of(), "linkToFlag", List.of());

        // Configurazione del mock per il repository
        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        System.out.println("Testing updateCampagna with invalid ID...");

        // Esecuzione e verifica
        Exception exception = assertThrows(RuntimeException.class, () -> service.updateCampagna(id, updatedCampagna));

        System.out.println("Exception: " + exception.getMessage());
        assertEquals("Campagna non trovata con ID: nonExistentId", exception.getMessage());
        verify(paeseRepository, times(1)).findAll();
        verify(paeseRepository, never()).save(any(Paese.class)); // Non dovrebbe salvare nulla
    }

    @Test
    void deleteCampagna_ShouldDeleteExistingCampagna() {
        String id = "1";

        // Campagna esistente
        CampagnaCrowdFunding existingCampagna = new CampagnaCrowdFunding(
                "1", "Campaign 1", "Description", null,
                LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1000"), new BigDecimal("500"),
                "attiva", "ItalyImage", "link.com", "Italy");

        // Paese contenente la campagna esistente (lista mutabile)
        Paese paese = new Paese(
                "1", "IT", "Italy", List.of(), new ArrayList<>(List.of(existingCampagna)),
                List.of(), "linkToFlag", List.of());

        // Configurazione del mock per il repository
        when(paeseRepository.findAll()).thenReturn(List.of(paese));
        when(paeseRepository.save(paese)).thenReturn(paese); // Configurazione per il metodo `save`

        System.out.println("Testing deleteCampagna with existing campagna...");

        // Esecuzione del metodo
        assertDoesNotThrow(() -> service.deleteCampagna(id));

        // Verifica delle interazioni
        verify(paeseRepository, times(1)).findAll();
        verify(paeseRepository, times(1)).save(paese); // Il Paese aggiornato deve essere salvato
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
