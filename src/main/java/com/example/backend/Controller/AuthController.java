package com.example.backend.Controller;

import com.example.backend.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            if (password == null) {
                password = request.get("motDePasse"); // Support for both names
            }
            
            if (password == null || email == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email et mot de passe sont obligatoires"));
            }
            
            return ResponseEntity.ok(authService.login(email, password));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
            "email", auth.getName(),
            "authorities", auth.getAuthorities()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // En mode stateless, le client doit simplement supprimer le token.
        // On retourne un message de succès pour confirmer.
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie. Veuillez supprimer le token côté client."));
    }
}
