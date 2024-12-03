package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByIdAutore(String idAutore); // Cerca tutti i post di un autore specifico
    List<Post> findByTitoloContainingIgnoreCase(String titolo); // Cerca per titolo contenente una parola
    List<Post> findByDataCreazione(Date dataCreazione); // Cerca per data esatta di creazione
    List<Post> findByDataCreazioneGreaterThanEqual(Date dataCreazione); // Post creati da una certa data in poi
    List<Post> findByDataCreazioneLessThanEqual(Date dataCreazione); // Post creati fino a una certa data
    List<Post> findByLikeGreaterThanEqual(int like); // Post con almeno un certo numero di like
}
