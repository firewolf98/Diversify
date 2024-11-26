package it.unisa.diversifybe.Model;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface SubcommentoDAO extends MongoRepository<Subcommento,String> {
    List<Subcommento> findById_autore(String id_autore);
    List<Subcommento> findByContenutoContaining(String keyword);
    List<Subcommento> findByData_creazioneIs(Date data_creazione);
    List<Subcommento> findByData_creazioneGreaterThanEqual(Date data_creazione);
    List<Subcommento> findByData_creazioneSmallerThanEqual(Date data_creazione);
}