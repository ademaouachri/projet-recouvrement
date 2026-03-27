package com.example.backend.Controller;

import com.example.backend.DTO.SetPasswordRequest;
import com.example.backend.DTO.VerifyOtpRequest;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Utilisateur user) {
        try {
            utilisateurService.registerUser(user);
            return ResponseEntity.ok("OTP envoyé ✅");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllUtilisateurs")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyOtpRequest request) {
        try {
            utilisateurService.verifyUserOtp(request.getEmail(), request.getOtp());
            return ResponseEntity.ok("Compte vérifié ✅");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable UUID id, @Valid @RequestBody Utilisateur user) {
        try {
            return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<String> activateOrDeactivateUtilisateur(
            @PathVariable UUID id,
            @RequestParam Boolean active) {
        try {
            String message = utilisateurService.activateOrDeactivateUtilisateur(id, active);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody SetPasswordRequest request) {
        try {
            utilisateurService.setPassword(request.getToken(), request.getPassword());
            return ResponseEntity.ok("Mot de passe défini ✅");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}