package it.unisa.diversifybe.Model;


import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Segnalazione {

    private String id;
    private String idSegnalante;
    private String idSegnalato;
    private String motivazione;
    private Date dataSegnalazione;
    private String tipoSegnalazione;

}
