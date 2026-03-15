package com.example.backend.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String SECRET = "votre_cle_secrete_tres_longue_et_securisee_de_32_caracteres";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION = 86400000; // 24h

    public String generateToken(String username, String structure, List<String> permissions) {
        return Jwts.builder()
                .setSubject(username)
                .claim("structure", structure)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public List<String> extractPermissions(String token) {
        return getClaims(token).get("permissions", List.class);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractStructure(String token) {
        return getClaims(token).get("structure", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}