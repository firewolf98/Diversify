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
public class CommentoController {

    private CommentoService commentoService;

    /**
     * Endpoint per aggiungere un commento a un post.
     *
     * @param idPost   L'ID del post a cui aggiungere il commento.
     * @param commento Il commento da aggiungere.
     * @return Il commento salvato.
     */

    @PostMapping("/post/{idPost}")
    public ResponseEntity<Commento> aggiungiCommentoAPost(
            @PathVariable String idPost,
            @RequestBody Commento commento) {
        Commento nuovoCommento = commentoService.aggiungiCommentoAPost(idPost, commento);
        return new ResponseEntity<>(nuovoCommento, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiungere un subcommento a un commento principale.
     *
     * @param idCommento  L'ID del commento principale.
     * @param subcommento Il subcommento da aggiungere.
     * @return Il subcommento salvato.
     */
    @PostMapping("/subcommento/{idCommento}")
    public ResponseEntity<Subcommento> aggiungiSubcommentoACommento(
            @PathVariable String idCommento,
            @RequestBody Subcommento subcommento) {
        Subcommento nuovoSubcommento = commentoService.aggiungiSubcommentoACommento(idCommento, subcommento);
        return new ResponseEntity<>(nuovoSubcommento, HttpStatus.CREATED);
    }

    /**
     * Endpoint per ottenere tutti i commenti associati a un post.
     *
     * @param idPost L'ID del post.
     * @return La lista dei commenti.
     */
    @GetMapping("/post/{idPost}")
    public ResponseEntity<List<Commento>> trovaCommentiPerPost(@PathVariable String idPost) {
        List<Commento> commenti = commentoService.trovaCommentiPerPost(idPost);
        return ResponseEntity.ok(commenti);
    }
}