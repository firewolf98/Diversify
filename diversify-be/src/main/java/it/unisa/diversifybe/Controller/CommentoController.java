package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.Commento;
import it.unisa.diversifybe.Model.Subcommento;
import it.unisa.diversifybe.Service.CommentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commenti")
@CrossOrigin
public class CommentoController {

    private CommentoService commentoService;

    /**
     * Endpoint per aggiungere un commento a un post.
     *
     * @param idPost   L'ID del post a cui aggiungere il commento.
     * @param commento Il commento da aggiungere.
     * @return Una risposta contenente il commento salvato o un'eccezione in caso di dati non validi.
     * @throws IllegalArgumentException se l'ID del post o il commento sono nulli o non validi.
     */
    @PostMapping("/post/{idPost}")
    public ResponseEntity<Commento> aggiungiCommentoAPost(
            @PathVariable String idPost,
            @RequestBody Commento commento) {
        if (idPost == null || idPost.isBlank()) {
            throw new IllegalArgumentException("L'ID del post non può essere nullo o vuoto.");
        }
        if (commento == null || commento.getContenuto() == null || commento.getContenuto().isBlank()) {
            throw new IllegalArgumentException("Il commento non può essere nullo o privo di contenuto.");
        }

        Commento nuovoCommento = commentoService.aggiungiCommentoAPost(idPost, commento);
        return new ResponseEntity<>(nuovoCommento, HttpStatus.CREATED);
    }


    /**
     * Endpoint per aggiungere un subcommento a un commento principale.
     *
     * @param idCommento  L'ID del commento principale.
     * @param subcommento Il subcommento da aggiungere.
     * @return Una risposta contenente il subcommento salvato o un'eccezione in caso di dati non validi.
     * @throws IllegalArgumentException se l'ID del commento o il subcommento sono nulli o non validi.
     */
    @PostMapping("/subcommento/{idCommento}")
    public ResponseEntity<Subcommento> aggiungiSubcommentoACommento(
            @PathVariable String idCommento,
            @RequestBody Subcommento subcommento) {
        if (idCommento == null || idCommento.isBlank()) {
            throw new IllegalArgumentException("L'ID del commento non può essere nullo o vuoto.");
        }
        if (subcommento == null || subcommento.getContenuto() == null || subcommento.getContenuto().isBlank()) {
            throw new IllegalArgumentException("Il subcommento non può essere nullo o privo di contenuto.");
        }

        Subcommento nuovoSubcommento = commentoService.aggiungiSubcommentoACommento(idCommento, subcommento);
        return new ResponseEntity<>(nuovoSubcommento, HttpStatus.CREATED);
    }

    /**
     * Endpoint per ottenere tutti i commenti associati a un post.
     *
     * @param idPost L'ID del post.
     * @return La lista dei commenti.
     * @throws IllegalArgumentException se l'ID del post è nullo o vuoto.
     */
    @GetMapping("/post/{idPost}")
    public ResponseEntity<List<Commento>> trovaCommentiPerPost(@PathVariable String idPost) {
        if (idPost == null || idPost.isBlank()) {
            throw new IllegalArgumentException("L'ID del post non può essere nullo o vuoto.");
        }

        List<Commento> commenti = commentoService.trovaCommentiPerPost(idPost);
        return ResponseEntity.ok(commenti);
    }


    /**
     * Endpoint per ottenere tutti i subcommenti associati a un commento principale.
     *
     * @param idCommento L'ID del commento principale.
     * @return Una risposta contenente una lista di subcommenti o una lista vuota se nessun subcommento è trovato.
     * @throws IllegalArgumentException se l'ID del commento è nullo o vuoto.
     */
    @GetMapping("/subcommento/{idCommento}")
    public ResponseEntity<List<Subcommento>> trovaSubcommentiPerCommento(@PathVariable String idCommento) {
        if (idCommento == null || idCommento.isBlank()) {
            throw new IllegalArgumentException("L'ID del commento non può essere nullo o vuoto.");
        }
        List<Subcommento> subcommenti = commentoService.trovaSubcommentiPerCommento(idCommento);
        return ResponseEntity.ok(subcommenti); // Restituisce una lista vuota se nessun subcommento è trovato
    }


}