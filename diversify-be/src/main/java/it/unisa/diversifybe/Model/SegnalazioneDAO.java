package it.unisa.diversifybe.Model;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

public interface SegnalazioneDAO extends MongoRepository<Segnalazione, Object> {
    Segnalazione findById(int id);
    List<Segnalazione> findByIdSegnalante(String idSegnalante);
    List<Segnalazione> findByIdSegnalato(String idSegnalato);
    List<Segnalazione> findByMotivazione(String motivazione);
    List<Segnalazione> findByDataSegnalazione(Date data);
    List<Segnalazione> findByTipoSegnalazione(String tipo);
    List<Segnalazione> findByDataSegnalazioneGreaterThanEqual(Date data);
}
