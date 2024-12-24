package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Classe che rappresenta un Benchmark.
 *
 * Questa classe viene utilizzata per definire un benchmark utilizzato
 * per valutare determinati criteri o parametri relativi al Paese.
 */

@Data
@Document(collection = "Benchmark")
public class Benchmark {

    private String idBenchmark;
    /**
     * Il tipo di benchmark.
     * Questo campo rappresenta la categoria o il tipo del benchmark, come ad esempio
     * un tipo di metrica o un criterio di valutazione.
     */

    private String tipo;

    /**
     * La gravità associata al benchmark.
     * Questo campo rappresenta l'importanza o l'urgenza associata al benchmark. Può essere
     * utilizzato per classificare i benchmark in base alla loro rilevanza.
     */

    private String gravita;

    /**
     * La descrizione del benchmark.
     * Questo campo fornisce una spiegazione dettagliata o una definizione del benchmark,
     * descrivendo in modo completo di cosa si tratta e come viene utilizzato.
     */

    private String descrizione;
}