package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private OtpService otpService;

    public void registerUser(Utilisateur user) {
        // Générer OTP et enregistrer
        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setEnabled(false);
        utilisateurRepository.save(user);

        otpService.sendOtp(user.getEmail(), otp);
    }

    public String verifyUserOtp(String email, String otpInput) {
        if (email == null || otpInput == null) {
            return "Email ou OTP manquant";
        }

        String cleanEmail = email.trim();
        String cleanOtp = otpInput.trim();

        Utilisateur user = utilisateurRepository.findByEmail(cleanEmail);
        if (user == null) {
            return "Utilisateur introuvable avec l'email : " + cleanEmail;
        }

        if (otpService.verifyOtp(cleanOtp, user.getOtp())) {
            user.setEnabled(true);
            user.setOtp(null);
            utilisateurRepository.save(user);
            return "OK";
        }
        return "Code OTP incorrect pour " + cleanEmail;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
}
