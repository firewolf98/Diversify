package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Segnalazione;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Repository.SegnalazioneRepository;
import it.unisa.diversifybe.Repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Lombok genera automaticamente il costruttore con il parametro obbligatorio (SegnalazioneRepository)
public class SegnalazioneService {

    private final SegnalazioneRepository segnalazioneRepository;
    private final UtenteRepository utenteRepository;

    /**
     * Ottieni tutte le segnalazioni.
     *
     * @return la lista di tutte le segnalazioni nel database.
     */
    public List<Segnalazione> getAllSegnalazioni() {
        return segnalazioneRepository.findAll();
    }

    /**
     * Ottieni una segnalazione per ID.
     *
     * @param id l'ID della segnalazione.
     * @return la segnalazione corrispondente, se esiste.
     */
    public Optional<Segnalazione> getSegnalazioneById(String id) {
        return segnalazioneRepository.findById(id);
    }

    /**
     * Crea una nuova segnalazione.
     *
     * @param segnalazione la segnalazione da creare.
     * @return la segnalazione creata.
     */
    public Segnalazione createSegnalazione(Segnalazione segnalazione) {
        if (segnalazione.getIdSegnalazione() == null || segnalazione.getIdSegnalazione().isEmpty()) {
            segnalazione.setIdSegnalazione(UUID.randomUUID().toString());
        }
        if (segnalazione.getDataSegnalazione() == null) {
            segnalazione.setDataSegnalazione(new Date());
        }
        return segnalazioneRepository.save(segnalazione);
    }

    /**
     * Aggiorna una segnalazione esistente.
     *
     * @param segnalazione la segnalazione da aggiornare.
     * @return la segnalazione aggiornata.
     */
    public Segnalazione updateSegnalazione(Segnalazione segnalazione) {
        return segnalazioneRepository.save(segnalazione);
    }

    /**
     * Elimina una segnalazione per ID.
     *
     * @param id l'ID della segnalazione da eliminare.
     * @param idUserToBan l'ID della persona da bannare.
     */
    public void deleteSegnalazione(String id, String idUserToBan) {
        segnalazioneRepository.deleteById(id);
        Optional<Utente> utente = utenteRepository.findById(idUserToBan);
        Utente update = utente.get();
        update.setBanned(true);
        utenteRepository.save(update);
    }

    /**
     * Ottieni tutte le segnalazioni fatte da un utente specifico.
     *
     * @param idSegnalante l'ID dell'utente che ha fatto le segnalazioni.
     * @return la lista di segnalazioni fatte dall'utente specificato.
     */
    public List<Segnalazione> getSegnalazioniByIdSegnalante(String idSegnalante) {
        return segnalazioneRepository.findByIdSegnalante(idSegnalante);
    }
}