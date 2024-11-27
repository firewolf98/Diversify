package it.unisa.diversifybe.Model;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;

public interface ForumDAO extends MongoRepository<Forum, String>{
    // Aggiunge un nuovo forum
    void addForum(Forum forum);

    // Recupera un forum tramite il suo ID
    Forum getForumById(String id);

    // Conta quanti post sono presenti in un forum
    int getAllPost(Forum forum);

    // Aggiorna un forum esistente
    void updateForum(Forum forum);

    // Cancella un forum tramite il suo ID
    void deleteForum(String id);
}