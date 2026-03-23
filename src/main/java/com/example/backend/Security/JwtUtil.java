package com.example.backend.Security;

import com.example.backend.Model.Utilisateur;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value; // ✅ هذا الصح
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String SECRET;

    private Key key;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Utilisateur user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
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
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}