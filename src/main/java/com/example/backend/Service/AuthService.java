package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import com.example.backend.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, Object> login(String email, String password) {
        Utilisateur user = utilisateurRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        if (password == null || user.getMotDePasse() == null || !passwordEncoder.matches(password, user.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        List<String> permissions = new ArrayList<>();
        if (user.getProfil() != null) {
            if (user.getProfil().getActivite() == 1) permissions.add("PERM_ACTIVITE");
            if (user.getProfil().getAgence() == 1) permissions.add("PERM_AGENCE");
            if (user.getProfil().getZone() == 1) permissions.add("PERM_ZONE");
            if (user.getProfil().getRegion() == 1) permissions.add("PERM_REGION");
            if (user.getProfil().getPalier() == 1) permissions.add("PERM_PALIER");
            if (user.getProfil().getSegment() == 1) permissions.add("PERM_SEGMENT");
            if (user.getProfil().getMarche() == 1) permissions.add("PERM_MARCHE");
            if (user.getProfil().getCentreAffaire() == 1) permissions.add("PERM_CENTRE_AFFAIRE");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getProfil().getStructure().name(), permissions);
        return Map.of(
                "token", token,
                "structure", user.getProfil().getStructure(),
                "permissions", permissions
        );
    }
}