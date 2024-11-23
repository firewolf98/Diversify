package it.unisa.diversifybe.Model;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String ID;
    private String titolo;
    private String id_autore;
    private String contenuto;
    private Date data_creazione;
    private int like;
    private List<Commento> commenti;
}
