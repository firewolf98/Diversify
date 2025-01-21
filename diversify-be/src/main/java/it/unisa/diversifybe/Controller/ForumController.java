package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class ForumController {

    @Autowired
    private ForumService forumService;

    /**
     * Recupera tutti i forum disponibili.
     *
     * @return una {@link ResponseEntity} contenente la lista di tutti i forum.
     */
    @GetMapping("/api/forums")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    /**
     * Recupera un forum in base al suo ID.
     *
     * @param idForum l'ID del forum da recuperare.
     * @return una {@link ResponseEntity} contenente il forum se trovato,
     * altrimenti un errore 404 (Not Found) se il forum non esiste o l'ID è nullo o vuoto.
     */
    @GetMapping("/api/forums/{idForum}")
    public ResponseEntity<Forum> getForumById(@PathVariable String idForum) {
        if (idForum == null || idForum.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        return forumService.getForumById(idForum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Aggiunge un nuovo forum. Operazione riservata agli amministratori.
     *
     * @param forum l'oggetto {@link Forum} da aggiungere.
     * @param ruolo un booleano che indica se l'utente è un amministratore.
     * @return una {@link ResponseEntity} contenente il forum aggiunto in caso di successo,
     * oppure un errore 403 (Forbidden) se l'utente non è autorizzato
     * o 400 (Bad Request) se il forum è nullo o contiene campi non validi.
     */
    @PostMapping("/api/forums")
    public ResponseEntity<Forum> addForum(@RequestBody Forum forum, @RequestParam boolean ruolo) {
        try {
            if (forum == null) {
                return ResponseEntity.badRequest().body(null); // Errore 400: Bad Request
            }
            return ResponseEntity.ok(forumService.addForum(forum, ruolo));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null); // Errore 403: Forbidden
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Errore 400: Bad Request
        }
    }

    /**
     * Aggiorna un forum esistente in base al suo ID. Operazione riservata agli amministratori.
     *
     * @param idForum l'ID del forum da aggiornare.
     * @param updatedForum l'oggetto {@link Forum} contenente i dati aggiornati.
     * @param ruolo un booleano che indica se l'utente è un amministratore.
     * @return una {@link ResponseEntity} contenente il forum aggiornato in caso di successo,
     * oppure un errore 403 (Forbidden) se l'utente non è autorizzato,
     * o 404 (Not Found) se il forum con l'ID specificato non esiste.
     * @throws IllegalArgumentException se l'ID del forum è nullo o vuoto.
     */
    @PutMapping("/api/forums/{idForum}")
    public ResponseEntity<Forum> updateForum(@PathVariable String idForum, @RequestBody Forum updatedForum, @RequestParam boolean ruolo) {
        if (idForum == null || idForum.isBlank()) {
            throw new IllegalArgumentException("ID forum non può essere nullo o vuoto.");
        }
        try {
            return ResponseEntity.ok(forumService.updateForum(idForum, updatedForum, ruolo));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un forum in base al suo ID. Operazione riservata agli amministratori.
     *
     * @param idForum l'ID del forum da eliminare.
     * @param ruolo un booleano che indica se l'utente è un amministratore.
     * @return una {@link ResponseEntity} vuota con stato 204 (No Content) in caso di successo,
     * oppure un errore 403 (Forbidden) se l'utente non è autorizzato.
     * @throws IllegalArgumentException se l'ID del forum è nullo o vuoto.
     */
    @PostMapping("/api/forums/delete/{idForum}")
    public ResponseEntity<Void> deleteForumAlternative(@PathVariable String idForum, @RequestParam boolean ruolo) {
        if (idForum == null || idForum.isBlank()) {
            throw new IllegalArgumentException("ID forum non può essere nullo o vuoto.");
        }
        try {
            forumService.deleteForum(idForum, ruolo);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Recupera i forum associati a un paese specifico.
     *
     * @param paese il paese per cui recuperare i forum.
     * @return una {@link ResponseEntity} contenente la lista dei forum se trovati,
     * oppure un errore 404 (Not Found) se non esistono forum associati al paese specificato.
     * @throws IllegalArgumentException se il parametro paese è nullo o vuoto.
     */
    @GetMapping("/api/forums/by-paese/{paese}")
    public ResponseEntity<List<Forum>> getForumsByPaese(@PathVariable String paese) {
        if (paese == null || paese.isBlank()) {
            throw new IllegalArgumentException("Il campo paese non può essere nullo o vuoto.");
        }
        List<Forum> forums = forumService.findForumsByPaese(paese);
        if (forums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forums);
    }

}
