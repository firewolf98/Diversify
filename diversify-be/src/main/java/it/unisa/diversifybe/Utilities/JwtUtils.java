package it.unisa.diversifybe.Utilities;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * La classe {@code JwtUtils} fornisce metodi utilitari per la gestione di token JWT
 * (JSON Web Token), inclusa la generazione e la validazione dei token.
 * <p>
 * La chiave segreta e il tempo di scadenza dei token sono configurabili tramite
 * le proprietà applicative.
 * </p>
 *
 * <p>
 * Uso tipico:
 * <ul>
 *   <li>Generazione di un token: {@link #generateToken(String)}</li>
 *   <li>Validazione e parsing di un token: {@link #validateToken(String)}</li>
 * </ul>
 * </p>
 *
 * <p><b>Dipendenze:</b> Questa classe utilizza la libreria <i>jjwt</i> per la gestione dei token.</p>
 */

@Component
public class JwtUtils {

    private final Key key;
    private final long expirationTime;

    /**
     * Costruttore per {@code JwtUtils}.
     *
     * @param secret         La chiave segreta utilizzata per firmare i token JWT.
     *                       Deve essere configurata tramite la proprietà {@code jwt.secret}.
     * @param expirationTime Il tempo di scadenza del token in millisecondi.
     *                       Deve essere configurato tramite la proprietà {@code jwt.expiration-time}.
     */
    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration-time}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    /**
     * Genera un token JWT con un soggetto specifico e un tempo di scadenza predefinito.
     *
     * @param username Il nome utente o identificatore da includere come soggetto del token.
     * @return Una stringa contenente il token JWT generato.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT e restituisce il soggetto se il token è valido.
     * <p>
     * Se il token è scaduto, invalido o manomesso, questo metodo restituirà {@code null}.
     * </p>
     *
     * @param token Il token JWT da validare.
     * @return Il soggetto (ad esempio, il nome utente) se il token è valido;
     *         {@code null} in caso contrario.
     */
    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // Gestione dell'errore di validazione (es. log, eccezioni personalizzate)
            return null;
        }
    }
}