package com.example.backend.Security;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.ParameterService; // ✅ زيد الـ Service متاعك
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    private ParameterService parameterService; // ✅ injecti الـ Service هنا


    private Key getSigningKey() {
        String secret = parameterService.getValueByKey("jwtSecret");

        if (secret == null)
            secret = "thisIsMyVerySecretKeyForJwtWhichIsLongEnoughToAvoidWeakKeyException2026";
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Utilisateur user) {
        String expStr = parameterService.getValueByKey("jwtExpiration");
        long expirationTime = 86400000; // القيمة الافتراضية

        if (expStr != null) {
            expirationTime = Long.parseLong(expStr);
        }

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()) // ✅ نعيطو للميثود اللي تجيب الـ Key
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
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}