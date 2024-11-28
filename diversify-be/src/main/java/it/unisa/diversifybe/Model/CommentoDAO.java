package it.unisa.diversifybe.Model;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

public interface CommentoDAO extends MongoRepository<Commento,String> {
    List<Commento> findById_autore(String id_autore);
    List<Commento> findByContenutoContaining(String keyword);
    List<Commento> findByData_creazioneIs(Date data_creazione);
    List<Commento> findByData_creazioneGreaterThanEqual(Date data_creazione);
    List<Commento> findByData_creazioneSmallerThanEqual(Date data_creazione);
}