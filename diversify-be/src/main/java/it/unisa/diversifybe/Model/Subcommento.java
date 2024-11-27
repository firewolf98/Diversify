package it.unisa.diversifybe.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Subcommenti")
public class Subcommento {
    private String id_autore;
    private String contenuto;
    private Date data_creazione;
}
