package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.ConversazioneChatbot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Il repository per gestire le operazioni di persistenza su {@link ConversazioneChatbot}.
 */
@Repository
public interface ConversazioneChatbotRepository extends MongoRepository<ConversazioneChatbot, String> {

    /**
     * Trova l'ultima conversazione di un dato utente, ordinata per data dell'ultima interazione (decrescente).
     *
     * @param idUtente l'ID dell'utente.
     * @return l'ultima conversazione per l'utente.
     */
    ConversazioneChatbot findTopByIdUtenteOrderByDataUltimaInterazioneDesc(String idUtente);
}
