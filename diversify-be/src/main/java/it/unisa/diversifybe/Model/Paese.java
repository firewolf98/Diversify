package it.unisa.diversifybe.Model;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paese {
    private String ID;
    private String nome;
    private String forum;
    private List<String> campagne_crowdfunding;
    private List<Benchmark> benchmark;
    private String link_immagine_bandiera;
    private List<DocumentoInformativo> documenti_informativi;
}