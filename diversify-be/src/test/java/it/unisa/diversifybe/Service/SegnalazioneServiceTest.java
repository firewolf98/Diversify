package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Segnalazione;
import it.unisa.diversifybe.Repository.SegnalazioneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SegnalazioneServiceTest {

    @InjectMocks
    private SegnalazioneService segnalazioneService;

    @Mock
    private SegnalazioneRepository segnalazioneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     *   Metodo getAllSegnalazioni:
     *   Caso Lista Popolata: Simula la presenza di due segnalazioni nella lista e verifica che la dimensione sia corretta.
     *   Caso Lista Vuota: Simula una lista vuota e verifica che il risultato sia effettivamente una lista vuota.
     *
     *   Metodo getSegnalazioneById:
     *   Caso Segnalazione Trovata: Simula il recupero di una segnalazione con un ID specifico, verifica che i dati siano corretti
     *   e che l'oggetto restituito sia presente.
     *   Caso Segnalazione Non Trovata: Simula un ID inesistente e verifica che il risultato sia vuoto.
     *
     *   Metodo createSegnalazione:
     *   Caso Creazione Riuscita: Simula la creazione di una nuova segnalazione, verifica che l'oggetto restituito non sia nullo
     *   e che il metodo di salvataggio sia stato invocato una volta.
     *
     *   Metodo updateSegnalazione:
     *   Caso Aggiornamento Riuscito: Simula l'aggiornamento di una segnalazione esistente, verifica che l'oggetto restituito non sia nullo
     *   e che il metodo di salvataggio sia stato invocato una volta.
     *
     *   Metodo deleteSegnalazione:
     *   Caso Eliminazione Riuscita: Simula l'eliminazione di una segnalazione esistente con un ID specifico, verifica che il metodo
     *   di eliminazione sia stato invocato una volta.
     *
     *   Metodo getSegnalazioniByIdSegnalante:
     *   Caso Lista Popolata: Simula un utente con due segnalazioni associate, verifica che la dimensione della lista sia corretta.
     *   Caso Lista Vuota: Simula un utente senza segnalazioni associate e verifica che il risultato sia una lista vuota.
     */

    @Test
    void getAllSegnalazioni_ListPopulated() {
        // Configura una lista di segnalazioni popolata
        List<Segnalazione> segnalazioni = Arrays.asList(new Segnalazione(), new Segnalazione());
        when(segnalazioneRepository.findAll()).thenReturn(segnalazioni);

        // Esegui il test
        System.out.println("Testing getAllSegnalazioni with populated list...");
        List<Segnalazione> result = segnalazioneService.getAllSegnalazioni();

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        assertEquals(2, result.size(), "The list should contain 2 segnalazioni");
        verify(segnalazioneRepository, times(1)).findAll();
    }

    @Test
    void getAllSegnalazioni_ListEmpty() {
        // Configura una lista vuota
        when(segnalazioneRepository.findAll()).thenReturn(List.of());

        // Esegui il test
        System.out.println("Testing getAllSegnalazioni with empty list...");
        List<Segnalazione> result = segnalazioneService.getAllSegnalazioni();

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        assertTrue(result.isEmpty(), "The result should be an empty list");
        verify(segnalazioneRepository, times(1)).findAll();
    }

    @Test
    void getSegnalazioneById_Found() {
        // Configura una segnalazione trovata
        String id = "1";
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setIdSegnalazione(id);
        when(segnalazioneRepository.findById(id)).thenReturn(Optional.of(segnalazione));

        // Esegui il test
        System.out.println("Testing getSegnalazioneById with a found segnalazione...");
        Optional<Segnalazione> result = segnalazioneService.getSegnalazioneById(id);

        // Verifica i risultati
        System.out.println("Result present: " + result.isPresent());
        assertTrue(result.isPresent(), "The result should not be empty");
        assertEquals(id, result.get().getIdSegnalazione(), "The IDs should match");
        verify(segnalazioneRepository, times(1)).findById(id);
    }

    @Test
    void getSegnalazioneById_NotFound() {
        // Configura una segnalazione non trovata
        String id = "1";
        when(segnalazioneRepository.findById(id)).thenReturn(Optional.empty());

        // Esegui il test
        System.out.println("Testing getSegnalazioneById with no segnalazione found...");
        Optional<Segnalazione> result = segnalazioneService.getSegnalazioneById(id);

        // Verifica i risultati
        System.out.println("Result present: " + result.isPresent());
        assertFalse(result.isPresent(), "The result should be empty");
        verify(segnalazioneRepository, times(1)).findById(id);
    }

    @Test
    void createSegnalazione_Success() {
        // Configura una segnalazione da creare
        Segnalazione segnalazione = new Segnalazione();
        when(segnalazioneRepository.save(segnalazione)).thenReturn(segnalazione);

        // Esegui il test
        System.out.println("Testing createSegnalazione...");
        Segnalazione result = segnalazioneService.createSegnalazione(segnalazione);

        // Verifica i risultati
        System.out.println("Created segnalazione: " + result);
        assertNotNull(result, "The created segnalazione should not be null");
        verify(segnalazioneRepository, times(1)).save(segnalazione);
    }

    @Test
    void updateSegnalazione_Success() {
        // Configura una segnalazione da aggiornare
        Segnalazione segnalazione = new Segnalazione();
        when(segnalazioneRepository.save(segnalazione)).thenReturn(segnalazione);

        // Esegui il test
        System.out.println("Testing updateSegnalazione...");
        Segnalazione result = segnalazioneService.updateSegnalazione(segnalazione);

        // Verifica i risultati
        System.out.println("Updated segnalazione: " + result);
        assertNotNull(result, "The updated segnalazione should not be null");
        verify(segnalazioneRepository, times(1)).save(segnalazione);
    }

    @Test
    void deleteSegnalazione_Success() {
        // Configura gli ID per il test
        String id = "1";
        String idSegnalato = "2";

        // Simula il comportamento del repository
        doNothing().when(segnalazioneRepository).deleteById(id);

        // Esegui il test
        System.out.println("Testing deleteSegnalazione...");
        segnalazioneService.deleteSegnalazione(id, idSegnalato);

        // Verifica il comportamento
        System.out.println("Segnalazione deleted for ID: " + id + ", Segnalato ID: " + idSegnalato);
        verify(segnalazioneRepository, times(1)).deleteById(id);
    }


    @Test
    void getSegnalazioniByIdSegnalante_ListPopulated() {
        // Configura una lista di segnalazioni per un utente specifico
        String idSegnalante = "user1";
        List<Segnalazione> segnalazioni = Arrays.asList(new Segnalazione(), new Segnalazione());
        when(segnalazioneRepository.findByIdSegnalante(idSegnalante)).thenReturn(segnalazioni);

        // Esegui il test
        System.out.println("Testing getSegnalazioniByIdSegnalante with populated list...");
        List<Segnalazione> result = segnalazioneService.getSegnalazioniByIdSegnalante(idSegnalante);

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        assertEquals(2, result.size(), "The list should contain 2 segnalazioni");
        verify(segnalazioneRepository, times(1)).findByIdSegnalante(idSegnalante);
    }

    @Test
    void getSegnalazioniByIdSegnalante_ListEmpty() {
        // Configura una lista vuota per un utente specifico
        String idSegnalante = "user1";
        when(segnalazioneRepository.findByIdSegnalante(idSegnalante)).thenReturn(List.of());

        // Esegui il test
        System.out.println("Testing getSegnalazioniByIdSegnalante with empty list...");
        List<Segnalazione> result = segnalazioneService.getSegnalazioniByIdSegnalante(idSegnalante);

        // Verifica i risultati
        System.out.println("Result size: " + result.size());
        assertTrue(result.isEmpty(), "The result should be an empty list");
        verify(segnalazioneRepository, times(1)).findByIdSegnalante(idSegnalante);
    }
}