package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ForumController {

    @Autowired
    private ForumService forumService;

    // Recupera tutti i forum
    @GetMapping("/api/forums")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    // Recupera un forum tramite ID
    @GetMapping("/api/forums/{idForum}")
    public ResponseEntity<Forum> getForumById(@PathVariable String idForum) {
        return forumService.getForumById(idForum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiunge un nuovo forum (solo amministratori)
    @PostMapping("/api/forums")
    public ResponseEntity<Forum> addForum(@RequestBody Forum forum, @RequestParam boolean ruolo) {
        try {
            return ResponseEntity.ok(forumService.addForum(forum, ruolo));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        }
    }

    // Modifica un forum esistente (solo amministratori)
    @PutMapping("/api/forums/{idForum}")
    public ResponseEntity<Forum> updateForum(@PathVariable String idForum, @RequestBody Forum updatedForum, @RequestParam boolean ruolo) {
        try {
            return ResponseEntity.ok(forumService.updateForum(idForum, updatedForum, ruolo));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Elimina un forum (solo amministratori)
    @PostMapping("/api/forums/delete/{idForum}")
    public ResponseEntity<Void> deleteForumAlternative(@PathVariable String idForum, @RequestParam boolean ruolo) {
        try {
            forumService.deleteForum(idForum, ruolo);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
    @GetMapping("/api/forums/by-paese/{paese}")
    public ResponseEntity<List<Forum>> getForumsByPaese(@PathVariable String paese) {
        List<Forum> forums = forumService.findForumsByPaese(paese);
        if (forums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forums);
    }
}
