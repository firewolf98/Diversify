package it.unisa.diversifybe.Model;

import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Forum {
    private String id;
    private String titolo;
    private String descrizione;
    private List<Post> post;
}