package it.unisa.diversifybe.Model;

import lombok.*;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Commento {

    private String id_autore;
    private String contenuto;
    private Date data_creazione;
    private int like;
    private List<Subcommento> subcommenti;

}
