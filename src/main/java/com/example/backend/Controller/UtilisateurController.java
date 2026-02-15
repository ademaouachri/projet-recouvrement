package com.example.backend.Controller;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.UtilisateurService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
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
        utilisateurService.registerUser(user);
        return ResponseEntity.ok("OTP envoyé (voir console) ✅");
    }

    @GetMapping("/getAllUtilisateurs")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email et OTP sont requis ❌");
        }

        String result = utilisateurService.verifyUserOtp(email, otp);
        if ("OK".equals(result)) {
            return ResponseEntity.ok("Compte activé ✅");
        } else {
            return ResponseEntity.badRequest().body("Erreur : " + result + " ❌");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Utilisateur updateUtilisateur(@PathVariable UUID id, @Valid @RequestBody Utilisateur user) {
        return utilisateurService.updateUtilisateur(id, user);
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<String> activateOrDeactivateUtilisateur(
            @PathVariable UUID id,
            @RequestParam Boolean active) {

        String message = utilisateurService.activateOrDeactivateUtilisateur(id, active);

        return ResponseEntity.ok(message);
    }


}
