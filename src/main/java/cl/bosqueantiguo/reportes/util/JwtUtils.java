package cl.bosqueantiguo.reportes.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    // CLAVE SECRETA UNIFICADA (debe coincidir con otros microservicios)
    private final String JWT_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long JWT_EXPIRATION = 86400000; // 24 horas en ms

    // Generar token JWT
    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        
        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
            .signWith(getSigningKey())
            .compact();
    }

    // Extraer username del token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extraer rol del token
    public String extractRol(String token) {
        return (String) extractClaims(token).get("rol");
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Validar token
    public boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer todos los claims del token
    private Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // Obtener clave de firma
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }
}
