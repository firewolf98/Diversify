package it.unisa.diversifybe.DTO;

/**
 * Classe di trasferimento dati (DTO) per la risposta contenente il token JWT.
 *
 * Questa classe viene utilizzata per inviare il token JWT in risposta a una richiesta di login
 * riuscita.
 */


public class JwtResponse {

    private String token;

    /**
     * Costruttore della classe JwtResponse.
     *
     * Crea una nuova istanza di JwtResponse con il token JWT fornito.
     *
     * @param token il token JWT da includere nella risposta.
     */

    public JwtResponse(String token) {
        this.token = token;
    }

    /**
     * Restituisce il token JWT.
     *
     * Questo metodo viene utilizzato per recuperare il token JWT dalla risposta.
     *
     * @return il token JWT come stringa.
     */

    public String getToken() {
        return token;
    }

    /**
     * Imposta il token JWT.
     *
     * Questo metodo viene utilizzato per impostare o aggiornare il token JWT nella risposta.
     *
     * @param token il nuovo token JWT da impostare.
     */

    public void setToken(String token) {
        this.token = token;
    }
}
