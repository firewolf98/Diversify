package it.unisa.diversifybe.Service;

import it.unisa.diversifybe.DTO.JwtResponse;
import it.unisa.diversifybe.DTO.LoginRequest;
import it.unisa.diversifybe.DTO.RegisterRequest;
import it.unisa.diversifybe.Model.Utente;
import it.unisa.diversifybe.Repository.UtenteRepository;
import it.unisa.diversifybe.Utilities.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JwtUtils jwtUtils; // Utils per la generazione del JWT

    public String registerUser(RegisterRequest registerRequest) throws NoSuchAlgorithmException {

        Optional<Utente> existingUserByUsername = utenteRepository.findByUsername(registerRequest.getUsername());
        if (existingUserByUsername.isPresent()) {
            return "Username già in uso!";
        }


        Optional<Utente> existingUserByEmail = utenteRepository.findByEmail(registerRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            return "Email già in uso!";
        }


        String hashedPassword = hashPassword(registerRequest.getPasswordHash());

        Utente utente = new Utente();
        utente.setUsername(registerRequest.getUsername());
        utente.setEmail(registerRequest.getEmail());
        utente.setPasswordHash(hashedPassword);

        utenteRepository.save(utente);

        return "Utente registrato con successo!";
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) throws NoSuchAlgorithmException {
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(loginRequest.getUsername());

        if (utenteOptional.isEmpty()) {
            return null; // Username non trovato
        }

        Utente utente = utenteOptional.get();

        if (utente.getPasswordHash().equals(hashPassword(loginRequest.getPasswordHash()))) {
            // Genera e restituisci il token JWT
            String jwt = jwtUtils.generateJwtToken(utente.getUsername());
            return new JwtResponse(jwt);
        }

        return null; // Password errata
    }

    // Metodo per effettuare l'hashing della password con SHA-256
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}