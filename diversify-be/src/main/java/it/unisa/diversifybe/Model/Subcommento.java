package it.unisa.diversifybe.Model;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subcommento {
    private String id_autore;
    private String contenuto;
    private Date data_creazione;
}
