package it.unisa.diversifybe.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * La classe {@code JwtUtils} fornisce metodi utilitari per la gestione dei token JWT.
 * I metodi consentono di generare, validare e estrarre informazioni da un token JWT,
 * utilizzando una chiave segreta e un tempo di scadenza configurabile.
 */

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT per l'utente specificato.
     * <p>
     * Il token contiene il nome utente come soggetto, la data di emissione e una data di scadenza calcolata
     * in base al valore di {@code jwtExpirationMs}.
     * </p>
     *
     * @param username il nome utente da inserire come soggetto del token.
     * @return il token JWT firmato come stringa.
     */

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    /**
     * Estrae il nome utente dal token JWT.
     * <p>
     * Il nome utente è memorizzato nel corpo del token come "subject".
     * </p>
     *
     * @param token il token JWT da cui estrarre il nome utente.
     * @return il nome utente (soggetto) contenuto nel token.
     */

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Verifica se il token JWT è valido.
     * <p>
     * La validazione include la verifica della firma e della data di scadenza.
     * </p>
     *
     * @param token il token JWT da validare.
     * @return {@code true} se il token è valido, altrimenti {@code false}.
     */

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}