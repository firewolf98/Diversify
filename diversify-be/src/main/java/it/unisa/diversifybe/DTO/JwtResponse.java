package it.unisa.diversifybe.DTO;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer"; // Tipo di token

    public JwtResponse(String token) {
        this.token = token;
    }
}