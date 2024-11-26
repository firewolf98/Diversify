package it.unisa.diversifybe.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomandaPersonale {
    private String tipo_domanda;
    private String risposta_personale_sha256;
}
