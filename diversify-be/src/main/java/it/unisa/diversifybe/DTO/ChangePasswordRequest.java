package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * Rappresenta la richiesta per cambiare la password di un utente.
 * Contiene il nome utente, la password corrente e la nuova password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    /**
     * Il nome utente dell'utente che richiede il cambio password.
     */
    private String username;

    /**
     * La password corrente dell'utente per la verifica.
     */
    private String currentPassword;

    /**
     * La nuova password che verrà impostata.
     */
    private String newPassword;
}