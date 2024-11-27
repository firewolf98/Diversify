package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Commenti")
public class Commento {
    private String id_autore;
    private String contenuto;
    private Date data_creazione;
    private int like;
    private List<Subcommento> subcommenti;
}