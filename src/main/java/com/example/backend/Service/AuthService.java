package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import com.example.backend.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, Object> login(String email, String password) {
        // تحقق من المستخدم
        Utilisateur user = utilisateurRepository.findByEmail(email);
        if (user == null)
            throw new RuntimeException("Utilisateur non trouvé");

        // تحقق من كلمة المرور
        if (password == null || user.getMotDePasse() == null ||
                !passwordEncoder.matches(password, user.getMotDePasse()))
            throw new RuntimeException("Email ou mot de passe incorrect");

        // تحقق من الـ account
        if (!user.getEnabled() || !user.getUtilisateurActive())
            throw new RuntimeException("Compte non activé");

        // ✅ generateToken بالـ user مباشرة
        String token = jwtUtil.generateToken(user);

        return Map.of(
                "token", token,
                "profil", user.getProfil().getCodeProfil(),
                "structure", user.getProfil().getStructure().name()
        );
    }
}