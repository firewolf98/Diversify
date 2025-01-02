package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.DocumentoInformativo;
import it.unisa.diversifybe.Repository.DocumentoInformativoRepository;
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


    @InjectMocks
    private DocumentoInformativoService documentoInformativoService;

    @Mock
    private DocumentoInformativoRepository documentoInformativoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdPaese_ShouldReturnDocumentsWithCompleteFields() {
        String idPaese = "validPaeseId";
        List<DocumentoInformativo> documents = List.of(
                new DocumentoInformativo("doc1", "Titolo 1", "Descrizione 1", "Contenuto 1", idPaese, "http://image.link/1", "http://video.link/1"),
                new DocumentoInformativo("doc2", "Titolo 2", "Descrizione 2", "Contenuto 2", idPaese, "http://image.link/2", "http://video.link/2")
        );

        when(documentoInformativoRepository.findByIdPaese(idPaese)).thenReturn(documents);

        List<DocumentoInformativo> result = documentoInformativoService.findByIdPaese(idPaese);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Titolo 1", result.get(0).getTitolo());
        assertEquals("http://image.link/1", result.get(0).getLinkImmagine());
        assertEquals("http://video.link/1", result.get(0).getLinkVideo());
        assertEquals("Titolo 2", result.get(1).getTitolo());
        assertEquals("http://image.link/2", result.get(1).getLinkImmagine());
        assertEquals("http://video.link/2", result.get(1).getLinkVideo());

        verify(documentoInformativoRepository, times(1)).findByIdPaese(idPaese);
    }


    /**
     * Test per `findByIdPaese` con ID Paese valido ma nessun documento trovato.
     */
    @Test
    void findByIdPaese_ShouldReturnEmptyListForValidIdNoDocuments() {
        String idPaese = "validPaeseId";

        when(documentoInformativoRepository.findByIdPaese(idPaese)).thenReturn(new ArrayList<>());

        List<DocumentoInformativo> result = documentoInformativoService.findByIdPaese(idPaese);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(documentoInformativoRepository, times(1)).findByIdPaese(idPaese);
    }

    /**
     * Test per `findByIdPaese` con ID Paese nullo.
     */
    @Test
    void findByIdPaese_ShouldThrowIllegalArgumentExceptionForNullId() {
        String idPaese = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> documentoInformativoService.findByIdPaese(idPaese));

        assertEquals("ID Paese non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(documentoInformativoRepository);
    }

    /**
     * Test per `findByIdPaese` con ID Paese vuoto.
     */
    @Test
    void findByIdPaese_ShouldThrowIllegalArgumentExceptionForEmptyId() {
        String idPaese = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> documentoInformativoService.findByIdPaese(idPaese));

        assertEquals("ID Paese non può essere nullo o vuoto.", exception.getMessage());
        verifyNoInteractions(documentoInformativoRepository);
    }

    /**
     * Test per `findByIdPaese` con ID Paese inesistente.
     */
    @Test
    void findByIdPaese_ShouldReturnEmptyListForNonExistentId() {
        String idPaese = "nonExistentId";

        when(documentoInformativoRepository.findByIdPaese(idPaese)).thenReturn(new ArrayList<>());

        List<DocumentoInformativo> result = documentoInformativoService.findByIdPaese(idPaese);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(documentoInformativoRepository, times(1)).findByIdPaese(idPaese);
    }
}
