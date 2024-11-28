package it.unisa.diversifybe.Model;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PaeseDAO extends MongoRepository<Paese, String> {

    // Trova un paese in base al nome
    List<Paese> findByNome(String nome);

    // Trova paesi che contengono una specifica keyword nel nome
    List<Paese> findByNomeContaining(String keyword);

    // Trova paesi che hanno un ID specifico per il forum
    List<Paese> findByForum(String forumID);

    // Trova paesi che contengono una specifica campagna di crowdfunding
    List<Paese> findByCampagne_crowdfundingContaining(String campagnaID);

    // Trova paesi che hanno un benchmark di un certo tipo
    List<Paese> findByBenchmark_Tipo(String tipo);

    // Trova paesi con un documento informativo che include un titolo specifico
    List<Paese> findByDocumenti_informativi_Titolo(String titoloDocumento);
}
