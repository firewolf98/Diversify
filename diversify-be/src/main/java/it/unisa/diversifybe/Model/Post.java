package it.unisa.diversifybe.Model;

import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String ID;
    private String titolo;
    private String idAutore;
    private String contenuto;
    private Date dataCreazione;
    private int like;
    private List<Commento> commenti;
}
