package it.unisa.diversifybe.DTO;

import lombok.*;

/**
 * Classe che rappresenta la richiesta per il recupero della password di un utente.
 * <p>
 * Questa classe contiene i dati necessari per eseguire il processo di recupero della password.
 * I dati includono l'email, il codice fiscale, la risposta alla domanda personale e la nuova password.
 * La gestione dei getter e setter è automatizzata grazie all'annotazione {@code @Data} di Lombok.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuperaPasswordRequest {

    /**
     * L'indirizzo email dell'utente.
     */
    private String email;

    /**
     * Il codice fiscale dell'utente.
     */
    private String codiceFiscale;

    /**
     * La risposta fornita dall'utente alla domanda personale.
     */
    private String personalAnswer;

    /**
     * La nuova password che l'utente desidera impostare.
     */
    private String newPassword;


}
