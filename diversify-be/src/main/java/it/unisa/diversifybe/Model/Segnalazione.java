package it.unisa.diversifybe.Model;

import java.util.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Segnalazioni")
public class Segnalazione {
    @Id
    private String id;
    private String idSegnalante;
    private String idSegnalato;
    private String motivazione;
    private Date dataSegnalazione;
    private String tipoSegnalazione;
}