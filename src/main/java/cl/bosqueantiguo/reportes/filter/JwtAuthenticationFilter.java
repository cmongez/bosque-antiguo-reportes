package cl.bosqueantiguo.reportes.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // CLAVE SECRETA UNIFICADA (debe coincidir con otros microservicios)
    private final String JWT_SECRET = "mySecretKeySuperSeguraDeEjemplomySecretKeySuperSeguraDeEjemplomySecretKeySuperSeguraDeEjemplo";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                // Validar y extraer claims del JWT
                Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                
                String username = claims.getSubject();
                String rol = (String) claims.get("rol");
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Crear autenticación con roles
                    List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + rol)
                    );
                    
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                
            } catch (Exception e) {
                // Token inválido - continúa sin autenticación
                logger.warn("Token JWT inválido: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // No filtrar rutas públicas ni documentación
        return path.startsWith("/api/v1/public/") 
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-ui")
            || path.startsWith("/swagger-resources");
    }
}