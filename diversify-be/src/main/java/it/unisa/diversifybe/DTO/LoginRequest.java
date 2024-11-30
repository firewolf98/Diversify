package it.unisa.diversifybe.DTO;

import lombok.*;
import java.util.List;

@Data
public class LoginRequest {
    private String username;
    private String passwordHash;
}