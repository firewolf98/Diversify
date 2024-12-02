package it.unisa.diversifybe.DTO;

import lombok.*;
import java.util.List;

@Data
public class RegisterRequest {
    private String username;
    private String passwordHash;
    private String email;
}
