package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    // Recupera tutti i forum
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    // Recupera un forum tramite ID
    public Optional<Forum> getForumById(String idForum) {
        return forumRepository.findById(idForum);
    }

    // Aggiunge un nuovo forum (solo amministratori)
    public Forum addForum(Forum forum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono aggiungere un forum.");
        }
        return forumRepository.save(forum);
    }

    // Modifica un forum esistente (solo amministratori)
    public Forum updateForum(String idForum, Forum updatedForum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono modificare un forum.");
        }

        return forumRepository.findById(idForum).map(forum -> {
            forum.setTitolo(updatedForum.getTitolo());
            forum.setDescrizione(updatedForum.getDescrizione());
            forum.setPost(updatedForum.getPost());
            return forumRepository.save(forum);
        }).orElseThrow(() -> new IllegalArgumentException("Forum non trovato con ID: " + idForum));
    }

    // Elimina un forum (solo amministratori)
    public void deleteForum(String idForum, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Solo gli amministratori possono eliminare un forum.");
        }
        forumRepository.deleteById(idForum);
    }
}
