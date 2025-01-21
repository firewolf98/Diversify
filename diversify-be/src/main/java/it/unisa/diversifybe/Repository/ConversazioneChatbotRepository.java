package it.unisa.diversifybe.Repository;

import it.unisa.diversifybe.Model.ConversazioneChatbot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository per gestire le operazioni nel database delle conversazioni della chatbot.
 */
public interface ConversazioneChatbotRepository extends MongoRepository<ConversazioneChatbot, String> {

    /**
     * Trova una conversazione associata all'utente con l'ID specificato.
     *
     * @param idUtente l'ID dell'utente.
     * @return un'opzionale contenente la conversazione, se trovata.
     */
    Optional<ConversazioneChatbot> findByIdUtente(String idUtente);
}
