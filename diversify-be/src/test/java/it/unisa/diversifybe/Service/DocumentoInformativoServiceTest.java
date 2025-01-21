package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Model.Paese;
import it.unisa.diversifybe.Repository.DocumentoInformativoRepository;
import it.unisa.diversifybe.Repository.PaeseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentoInformativoServiceTest {
    /**
     * **Partizioni per il metodo `findByIdPaese`:**
     * - **ID Paese:**
     *   - Valido: ID esistente nel database.
     *   - Non valido:
     *     - Null o vuoto.
     *     - ID inesistente.
     * - **Esito della richiesta:**
     *   - Successo: Lista di documenti restituita.
     *   - Fallimento:
     *     - Nessun documento trovato (lista vuota).
     */

    @Mock
    private PaeseRepository paeseRepository;

    @InjectMocks
    private DocumentoInformativoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdPaese_ShouldReturnDocuments_WhenIdPaeseIsValid() {
        String idPaese = "Italy";
        List<DocumentoInformativo> documenti = List.of(
                new DocumentoInformativo("doc1", "Titolo 1", "Descrizione 1", "Contenuto 1", idPaese, "http://image1.com", "http://video1.com"),
                new DocumentoInformativo("doc2", "Titolo 2", "Descrizione 2", "Contenuto 2", idPaese, "http://image2.com", "http://video2.com")
        );

        Paese paese = new Paese("1", idPaese, "Italy", List.of(), new ArrayList<>(), new ArrayList<>(), "http://flag.com", documenti);

        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        List<DocumentoInformativo> result = service.findByIdPaese(idPaese);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Titolo 1", result.getFirst().getTitolo());
        verify(paeseRepository, times(1)).findAll();
    }

    @Test
    void findByIdPaese_ShouldReturnEmptyList_WhenIdPaeseHasNoDocuments() {
        String idPaese = "Italy";
        Paese paese = new Paese("1", idPaese, "Italy", List.of(), new ArrayList<>(), new ArrayList<>(), "http://flag.com", new ArrayList<>());

        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        List<DocumentoInformativo> result = service.findByIdPaese(idPaese);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paeseRepository, times(1)).findAll();
    }

    @Test
    void findByIdPaese_ShouldReturnEmptyList_WhenIdPaeseDoesNotExist() {
        String idPaese = "NonExistentId";
        Paese paese = new Paese("1", "Italy", "Italy", List.of(), new ArrayList<>(), new ArrayList<>(), "http://flag.com", new ArrayList<>());

        when(paeseRepository.findAll()).thenReturn(List.of(paese));

        List<DocumentoInformativo> result = service.findByIdPaese(idPaese);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paeseRepository, times(1)).findAll();
    }

    @Test
    void findByIdPaese_ShouldThrowException_WhenIdPaeseIsNull() {
        String idPaese = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findByIdPaese(idPaese));

        assertEquals("ID Paese non può essere nullo o vuoto.", exception.getMessage());
        verify(paeseRepository, never()).findAll();
    }

    @Test
    void findByIdPaese_ShouldThrowException_WhenIdPaeseIsEmpty() {
        String idPaese = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findByIdPaese(idPaese));

        assertEquals("ID Paese non può essere nullo o vuoto.", exception.getMessage());
        verify(paeseRepository, never()).findAll();
    }


}
