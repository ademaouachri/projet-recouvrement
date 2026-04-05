package com.example.backend.Security;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.ParameterService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final ParameterService parameterService;

    public JwtUtil(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    private Key getSigningKey() {
        // Lecture directe : on fait confiance à l'initialisation de AppConfig
        String secret = parameterService.getValueByKey("jwtSecret");
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Utilisateur user) {
        // Conversion directe du String en Long
        long expirationTime = Long.parseLong(parameterService.getValueByKey("jwtExpiration"));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // بقية الميثودات (extractEmail, isTokenValid, getClaims) تبقى كما هي...

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}