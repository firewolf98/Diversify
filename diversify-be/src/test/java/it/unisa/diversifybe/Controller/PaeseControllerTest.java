package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Service.PaeseService;
import it.unisa.diversifybe.Service.DocumentoInformativoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaeseControllerTest {

    /**
     * Categorie individuate secondo la metodologia Category Partition:
     * Metodo getAllPaesi:
     * - Lista di Paesi non vuota.
     * - Lista di Paesi vuota.
     * Metodo getPaeseById:
     * - ID valido con Paese associato.
     * - ID valido senza Paese associato.
     * - ID non valido (e.g., null o vuoto).
     * Metodo createPaese:
     * - Dati validi.
     * - Dati non validi (e.g., campi obbligatori mancanti).
     * - Paese già esistente (duplicato).
     * Metodo updatePaese:
     * - ID valido con dati validi.
     * - ID valido con dati non validi.
     * - ID valido senza Paese associato.
     * - ID non valido.
     * Metodo deletePaese:
     * - ID valido con Paese associato.
     * - ID valido senza Paese associato.
     * - ID non valido.
     * Metodo findPaesiByCampagna:
     * - ID campagna valido con Paesi associati.
     * - ID campagna valido senza Paesi associati.
     * - ID campagna non valido.
     * Metodo findPaesiByBenchmark:
     * - Benchmark valido con Paesi associati.
     * - Benchmark valido senza Paesi associati.
     * - Benchmark non valido (e.g., null o non esistente).
     * Metodo getDocumentiInformativiByPaese:
     * - ID Paese valido con documenti associati.
     * - ID Paese valido senza documenti associati.
     * - ID Paese non valido.
     */


    @InjectMocks
    private PaeseController controller;

    @Mock
    private PaeseService paeseService;

    @Mock
    private DocumentoInformativoService documentoInformativoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per getAllPaesi
     */
    @Test
    void getAllPaesi_ShouldReturnPaesiList() {
        List<Paese> paesi = Arrays.asList(
                new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of()),
                new Paese("2", "FR", "France", List.of(), List.of(), List.of(), "link2", List.of())
        );


        when(paeseService.getAllPaesi()).thenReturn(paesi);

        System.out.println("Testing getAllPaesi with a non-empty list...");
        List<Paese> result = controller.getAllPaesi();

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(paeseService, times(1)).getAllPaesi();
    }

    @Test
    void getAllPaesi_ShouldReturnEmptyList() {
        when(paeseService.getAllPaesi()).thenReturn(Collections.emptyList());

        System.out.println("Testing getAllPaesi with an empty list...");
        List<Paese> result = controller.getAllPaesi();

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paeseService, times(1)).getAllPaesi();
    }

    /**
     * Test per getPaeseById.
     */
    @Test
    void getPaeseById_ShouldReturnPaese() {
        Paese paese = new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());
        when(paeseService.getPaeseById("1")).thenReturn(Optional.of(paese));

        System.out.println("Testing getPaeseById with a valid ID...");
        ResponseEntity<Paese> response = controller.getPaeseById("1");

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paese, response.getBody());
        verify(paeseService, times(1)).getPaeseById("1");
    }

    @Test
    void getPaeseById_ShouldReturnNotFound() {
        when(paeseService.getPaeseById("1")).thenReturn(Optional.empty());

        System.out.println("Testing getPaeseById with a non-existent ID...");
        ResponseEntity<Paese> response = controller.getPaeseById("1");

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(paeseService, times(1)).getPaeseById("1");
    }

    @Test
    void getPaeseById_ShouldThrowExceptionForInvalidId() {
        System.out.println("Testing getPaeseById with an invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.getPaeseById(null));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, never()).getPaeseById(anyString());
    }

    /**
     * Test per createPaese.
     */
    @Test
    void createPaese_ShouldCreateAndReturnPaese() {
        Paese paese =new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(paeseService.createPaese(paese)).thenReturn(paese);

        System.out.println("Testing createPaese with valid data...");
        Paese result = controller.createPaese(paese);

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertEquals(paese, result);
        verify(paeseService, times(1)).createPaese(paese);
    }

    @Test
    void createPaese_ShouldThrowExceptionForInvalidData() {
        Paese invalidPaese = new Paese();

        when(paeseService.createPaese(invalidPaese)).thenThrow(new IllegalArgumentException("Invalid Paese data"));

        System.out.println("Testing createPaese with invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.createPaese(invalidPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, times(1)).createPaese(invalidPaese);
    }

    @Test
    void createPaese_ShouldThrowExceptionForDuplicatePaese() {
        Paese duplicatePaese = new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(paeseService.createPaese(duplicatePaese)).thenThrow(new IllegalArgumentException("Paese already exists"));

        System.out.println("Testing createPaese with duplicate data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.createPaese(duplicatePaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, times(1)).createPaese(duplicatePaese);
    }

    /**
     * Test per updatePaese.
     */
    @Test
    void updatePaese_ShouldUpdateAndReturnPaese() {
        String id = "1";
        Paese updatedPaese = new Paese("1", "IT", "Updated Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(paeseService.updatePaese(eq(id), any(Paese.class))).thenReturn(Optional.of(updatedPaese));

        System.out.println("Testing updatePaese with valid ID and data...");
        ResponseEntity<Paese> response = controller.updatePaese(id, updatedPaese);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPaese, response.getBody());
        verify(paeseService, times(1)).updatePaese(eq(id), eq(updatedPaese));
    }

    @Test
    void updatePaese_ShouldThrowExceptionForInvalidData() {
        String id = "1";
        Paese invalidPaese = new Paese();

        when(paeseService.updatePaese(eq(id), any(Paese.class))).thenThrow(new IllegalArgumentException("Invalid Paese data"));

        System.out.println("Testing updatePaese with valid ID but invalid data...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.updatePaese(id, invalidPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, times(1)).updatePaese(eq(id), eq(invalidPaese));
    }

    @Test
    void updatePaese_ShouldReturnNotFound() {
        String id = "1";
        Paese updatedPaese =new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        when(paeseService.updatePaese(eq(id), any(Paese.class))).thenReturn(Optional.empty());

        System.out.println("Testing updatePaese with valid ID but no associated Paese...");
        ResponseEntity<Paese> response = controller.updatePaese(id, updatedPaese);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(paeseService, times(1)).updatePaese(eq(id), eq(updatedPaese));
    }

    @Test
    void updatePaese_ShouldThrowExceptionForInvalidId() {
        String id = null;
        Paese updatedPaese = new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of());

        System.out.println("Testing updatePaese with invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.updatePaese(id, updatedPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, never()).updatePaese(anyString(), any(Paese.class));
    }

    /**
     * Test per deletePaese.
     */
    @Test
    void deletePaese_ShouldDeletePaese() {
        String id = "1";

        doNothing().when(paeseService).deletePaese(eq(id));

        System.out.println("Testing deletePaese with valid ID and associated Paese...");
        ResponseEntity<Void> response = controller.deletePaese(id);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(paeseService, times(1)).deletePaese(eq(id));
    }

    @Test
    void deletePaese_ShouldThrowExceptionForNonExistentPaese() {
        String id = "1";

        doThrow(new RuntimeException("Paese not found with ID: " + id)).when(paeseService).deletePaese(eq(id));

        System.out.println("Testing deletePaese with valid ID but no associated Paese...");
        Exception exception = assertThrows(RuntimeException.class, () -> controller.deletePaese(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, times(1)).deletePaese(eq(id));
    }

    @Test
    void deletePaese_ShouldThrowExceptionForInvalidId() {
        String id = null;

        System.out.println("Testing deletePaese with invalid ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.deletePaese(id));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, never()).deletePaese(anyString());
    }

    /**
     * Test per findPaesiByBenchmark.
     */
    @Test
    void findPaesiByBenchmark_ShouldReturnPaesiList() {
        String benchmark = "ValidBenchmark";
        List<Paese> paesi = Arrays.asList(
                new Paese("1", "IT", "Italy", List.of(), List.of(), List.of(), "link1", List.of()),
                new Paese("2", "FR", "France", List.of(), List.of(), List.of(), "link2", List.of())
        );


        when(paeseService.findPaesiByBenchmark(benchmark)).thenReturn(paesi);

        System.out.println("Testing findPaesiByBenchmark with a valid benchmark...");
        List<Paese> result = controller.findPaesiByBenchmark(benchmark);

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(paeseService, times(1)).findPaesiByBenchmark(benchmark);
    }

    @Test
    void findPaesiByBenchmark_ShouldReturnEmptyList() {
        String benchmark = "ValidBenchmark";

        when(paeseService.findPaesiByBenchmark(benchmark)).thenReturn(Collections.emptyList());

        System.out.println("Testing findPaesiByBenchmark with no associated countries...");
        List<Paese> result = controller.findPaesiByBenchmark(benchmark);

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paeseService, times(1)).findPaesiByBenchmark(benchmark);
    }

    @Test
    void findPaesiByBenchmark_ShouldThrowExceptionForInvalidBenchmark() {
        String benchmark = null;

        System.out.println("Testing findPaesiByBenchmark with an invalid benchmark...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.findPaesiByBenchmark(benchmark));

        System.out.println("Exception: " + exception.getMessage());
        verify(paeseService, never()).findPaesiByBenchmark(anyString());
    }

    /**
     * Test per getDocumentiInformativiByPaese.
     */
    @Test
    void getDocumentiInformativiByPaese_ShouldReturnDocumentiList() {
        String idPaese = "1";
        List<DocumentoInformativo> documenti = Arrays.asList(
                new DocumentoInformativo("Doc1", "Titolo 1", "Descrizione 1", "Contenuto 1", idPaese, "link1", "video1"),
                new DocumentoInformativo("Doc2", "Titolo 2", "Descrizione 2", "Contenuto 2", idPaese, "link2", "video2")
        );


        when(documentoInformativoService.findByIdPaese(idPaese)).thenReturn(documenti);

        System.out.println("Testing getDocumentiInformativiByPaese with a valid country ID...");
        @SuppressWarnings("unchecked")
        ResponseEntity<List<DocumentoInformativo>> response = (ResponseEntity<List<DocumentoInformativo>>) controller.getDocumentiInformativiByPaese(idPaese);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(documenti, response.getBody());
        verify(documentoInformativoService, times(1)).findByIdPaese(idPaese);
    }

    @Test
    void getDocumentiInformativiByPaese_ShouldReturnNotFound() {
        String idPaese = "1";

        when(documentoInformativoService.findByIdPaese(idPaese)).thenReturn(Collections.emptyList());

        System.out.println("Testing getDocumentiInformativiByPaese with no associated documents...");
        @SuppressWarnings("unchecked")
        ResponseEntity<List<DocumentoInformativo>> response = (ResponseEntity<List<DocumentoInformativo>>) controller.getDocumentiInformativiByPaese(idPaese);

        System.out.println("Response: " + response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(documentoInformativoService, times(1)).findByIdPaese(idPaese);
    }

    @Test
    void getDocumentiInformativiByPaese_ShouldThrowExceptionForInvalidId() {
        String idPaese = null;

        System.out.println("Testing getDocumentiInformativiByPaese with an invalid country ID...");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.getDocumentiInformativiByPaese(idPaese));

        System.out.println("Exception: " + exception.getMessage());
        verify(documentoInformativoService, never()).findByIdPaese(anyString());
    }

}
