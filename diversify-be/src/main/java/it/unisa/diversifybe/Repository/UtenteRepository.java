package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UtenteRepository extends MongoRepository<Utente, String> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByRispostaHash(String rispostaHash);

}
