package com.example.backend.Controller;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Utilisateur user) {
        utilisateurService.registerUser(user);
        return ResponseEntity.ok("OTP envoyé (voir console) ✅");
    }

    @GetMapping("/getAllUtilisateurs")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody java.util.Map<String, String> request) {
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
}
