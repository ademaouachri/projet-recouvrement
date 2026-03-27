package com.example.backend.Service;

import com.example.backend.DTO.LoginResponse;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import com.example.backend.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(String email, String password) {
        //
        Utilisateur user = utilisateurRepository.findByEmail(email);
        if (user == null)
            throw new RuntimeException("Utilisateur non trouvé");


        if (password == null || user.getMotDePasse() == null ||
                !passwordEncoder.matches(password, user.getMotDePasse()))
            throw new RuntimeException("Email ou mot de passe incorrect");


        if (!user.getEnabled() || !user.getUtilisateurActive())
            throw new RuntimeException("Compte non activé");


        String token = jwtUtil.generateToken(user);
        return new LoginResponse(
                token,
                user.getProfil().getCodeProfil(),
                user.getProfil().getStructure().name()
        );
    }
}