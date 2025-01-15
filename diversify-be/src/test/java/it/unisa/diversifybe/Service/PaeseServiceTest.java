package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Benchmark;
import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaeseServiceTest {

    /**
     * Categorie e Partizioni per il testing di PaeseService:
     * Metodo getAllPaesi:
     * - Lista di paesi non vuota.
     * - Lista di paesi vuota.
     * Metodo getPaeseById:
     * - ID valido con paese associato.
     * - ID valido senza paese associato.
     * - ID non valido (e.g., null o vuoto).
     * Metodo createPaese:
     * - Dati validi.
     * - Dati non validi (e.g., campi obbligatori mancanti).
     * - Paese già esistente (duplicato).
     * Metodo updatePaese:
     * - ID valido con dati validi.
     * - ID valido con dati non validi.
     * - ID valido senza paese associato.
     * - ID non valido (e.g., null o vuoto).
     * Metodo deletePaese:
     * - ID valido con paese associato.
     * - ID valido senza paese associato.
     * - ID non valido (e.g., null o vuoto).
     * Metodo findPaesiByCampagna:
     * - ID campagna valido con paesi associati.
     * - ID campagna valido senza paesi associati.
     * - ID campagna non valido (e.g., null o vuoto).
     * Metodo findPaesiByBenchmark:
     * - ID benchmark valido con paesi associati.
     * - ID benchmark valido senza paesi associati.
     * - ID benchmark non valido (e.g., null o vuoto).
     */


    @InjectMocks
    private PaeseService service;

    @Mock
    private PaeseRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per getAllPaesi.
     */
    @Test
    void getAllPaesi_ShouldReturnPaesiList() {
        List<Paese> paesi = Arrays.asList(
                new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of()),
                new Paese("2", "FR", "France", List.of(), List.of(), List.of(), "link2", List.of())
        );


        when(repository.findAll()).thenReturn(paesi);

        System.out.println("Testing getAllPaesi with a non-empty list...");
        List<Paese> result = service.getAllPaesi();

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllPaesi_ShouldReturnEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        System.out.println("Testing getAllPaesi with an empty list...");
        List<Paese> result = service.getAllPaesi();

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    /**
     * Test per getPaeseById.
     */
    @Test
    void getPaeseById_ShouldReturnPaese() {
        String id = "1";
        Paese paese = new Paese(id, "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(repository.findById(id)).thenReturn(Optional.of(paese));

        System.out.println("Testing getPaeseById with a valid ID...");
        Optional<Paese> result = service.getPaeseById(id);

        System.out.println("Result: " + result);
        assertTrue(result.isPresent());
        assertEquals(paese, result.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void getPaeseById_ShouldReturnEmptyOptional() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.empty());

        System.out.println("Testing getPaeseById with a valid ID but no associated country...");
        Optional<Paese> result = service.getPaeseById(id);

        System.out.println("Result: " + result);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void getPaeseById_ShouldThrowExceptionForInvalidId() {
        String id = null;

        System.out.println("Testing getPaeseById with an invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.getPaeseById(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).findById(anyString());
    }

    /**
     * Test per createPaese.
     */
    @Test
    void createPaese_ShouldSaveAndReturnPaese() {
        Paese paese = new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(repository.save(paese)).thenReturn(paese);

        System.out.println("Testing createPaese with valid data...");
        Paese result = service.createPaese(paese);

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertEquals(paese, result);
        verify(repository, times(1)).save(paese);
    }

    @Test
    void createPaese_ShouldThrowExceptionForInvalidData() {
        Paese paese = new Paese();

        System.out.println("Testing createPaese with invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.createPaese(paese));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).save(any(Paese.class));
    }

    @Test
    void createPaese_ShouldThrowExceptionForDuplicatePaese() {
        Paese paese =new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(repository.existsById(paese.getIdPaese())).thenReturn(true);

        System.out.println("Testing createPaese with duplicate data...");
        Exception exception = assertThrows(IllegalStateException.class, () -> service.createPaese(paese));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).save(any(Paese.class));
    }
    /**
     * Test per updatePaese.
     */
    @Test
    void updatePaese_ShouldReturnUpdatedPaese() {
        String id = "1";
        Paese existingPaese = new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());
        Paese updatedPaese = new Paese("1", "IT", "Updated Italy", List.of(), List.of(), List.of(), " updated.link1.com", List.of());

        when(repository.findById(id)).thenReturn(Optional.of(existingPaese));
        when(repository.save(any(Paese.class))).thenReturn(updatedPaese);

        System.out.println("Testing updatePaese with valid ID and data...");
        Optional<Paese> result = service.updatePaese(id, updatedPaese);

        System.out.println("Result: " + result);
        assertTrue(result.isPresent());
        assertEquals(updatedPaese.getNome(), result.get().getNome());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(updatedPaese);
    }

    @Test
    void updatePaese_ShouldThrowExceptionForInvalidData() {
        String id = "1";
        Paese updatedPaese = new Paese();

        System.out.println("Testing updatePaese with valid ID but invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.updatePaese(id, updatedPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).save(any(Paese.class));
    }

    @Test
    void updatePaese_ShouldReturnEmptyForNonExistentPaese() {
        String id = "1";
        Paese updatedPaese = new Paese("1", "IT", "Updated Italy", List.of(), List.of(), List.of(), "updatedlink1.com", List.of());

        when(repository.findById(id)).thenReturn(Optional.empty());

        System.out.println("Testing updatePaese with valid ID but no associated country...");
        Optional<Paese> result = service.updatePaese(id, updatedPaese);

        System.out.println("Result: " + result);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Paese.class));
    }

    @Test
    void updatePaese_ShouldThrowExceptionForInvalidId() {
        String id = null;
        Paese updatedPaese = new Paese("1", "IT", "Updated Italy", List.of(), List.of(), List.of(), "link1.update", List.of());

        System.out.println("Testing updatePaese with invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.updatePaese(id, updatedPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).findById(anyString());
        verify(repository, never()).save(any(Paese.class));
    }

    /**
     * Test per deletePaese.
     */
    @Test
    void deletePaese_ShouldDeletePaese() {
        String id = "1";

        when(repository.existsById(id)).thenReturn(true);

        System.out.println("Testing deletePaese with valid ID and associated country...");
        assertDoesNotThrow(() -> service.deletePaese(id));

        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deletePaese_ShouldThrowExceptionForInvalidId() {
        String id = null;

        System.out.println("Testing deletePaese with invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.deletePaese(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).existsById(anyString());
        verify(repository, never()).deleteById(anyString());
    }

    /**
     * Test per findPaesiByCampagna.
     */
    @Test
    void findPaesiByCampagna_ShouldReturnPaesiList() {
        // Creazione di un oggetto CampagnaCrowdFunding
        CampagnaCrowdFunding campagna = new CampagnaCrowdFunding();

        // Impostazione dei valori dei campi
        campagna.setIdCampagna("campaign001");
        campagna.setTitolo("Save the Forests");
        campagna.setCategoria("Environment");
        campagna.setDescrizione("A campaign to protect and restore forests worldwide.");
        campagna.setDataInizio(LocalDate.of(2025, 1, 1));
        campagna.setDataPrevistaFine(LocalDate.of(2025, 12, 31));
        campagna.setSommaDaRaccogliere(new BigDecimal("50000"));
        campagna.setSommaRaccolta(new BigDecimal("10000"));
        campagna.setStato("attiva");
        campagna.setImmagine("https://example.com/forest-campaign.jpg");
        campagna.setPaese("Italy");

        List<Paese> paesi = Arrays.asList(
                new Paese("1", "IT", "Italy", List.of(), Collections.singletonList(campagna), List.of(), "link1", List.of()),
                new Paese("2", "FR", "France", List.of(), Collections.singletonList(campagna), List.of(), "link2", List.of())
        );


        when(repository.findAll()).thenReturn(paesi);

        System.out.println("Testing findPaesiByCampagna with valid campaign ID and associated countries...");
        List<Paese> result = service.findPaesiByCampagna(campagna.getIdCampagna());

        System.out.println("Result: " + result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findPaesiByCampagna_ShouldReturnEmptyList() {
        String idCampagna = "campagna1";
        when(repository.findAll()).thenReturn(Collections.emptyList());

        System.out.println("Testing findPaesiByCampagna with valid campaign ID but no associated countries...");
        List<Paese> result = service.findPaesiByCampagna(idCampagna);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findPaesiByCampagna_ShouldThrowExceptionForInvalidId() {
        String idCampagna = null;

        System.out.println("Testing findPaesiByCampagna with invalid campaign ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findPaesiByCampagna(idCampagna));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).findAll();
    }

    /**
     * Test per findPaesiByBenchmark.
     */
    @Test
    void findPaesiByBenchmark_ShouldReturnPaesiList() {
        String idBenchmark = "benchmark1";
        Benchmark benchmark = new Benchmark(idBenchmark, "Economico", "Alta", "Valutazione economica globale");

        List<Paese> paesi = Arrays.asList(
                new Paese("1", "IT", "Italy", List.of(), List.of(),Collections.singletonList(benchmark), "link1", List.of()),
                new Paese("2", "FR", "France", List.of(), List.of(), Collections.singletonList(benchmark), "link2", List.of())
        );

        when(repository.findAll()).thenReturn(paesi);

        System.out.println("Testing findPaesiByBenchmark with valid benchmark ID and associated countries...");
        List<Paese> result = service.findPaesiByBenchmark(idBenchmark);

        System.out.println("Result: " + result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findPaesiByBenchmark_ShouldReturnEmptyList() {
        String idBenchmark = "benchmark1";
        when(repository.findAll()).thenReturn(Collections.emptyList());

        System.out.println("Testing findPaesiByBenchmark with valid benchmark ID but no associated countries...");
        List<Paese> result = service.findPaesiByBenchmark(idBenchmark);

        System.out.println("Result: " + result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findPaesiByBenchmark_ShouldThrowExceptionForInvalidId() {
        String idBenchmark = null;

        System.out.println("Testing findPaesiByBenchmark with invalid benchmark ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findPaesiByBenchmark(idBenchmark));

        System.out.println("Exception: " + exception.getMessage());
        verify(repository, never()).findAll();
    }
}
