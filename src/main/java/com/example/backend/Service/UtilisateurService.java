package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final OtpService otpService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, OtpService otpService) {
        this.utilisateurRepository = utilisateurRepository;
        this.otpService = otpService;
    }

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

    public java.util.Optional<Utilisateur> getUtilisateurById(UUID id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur updateUtilisateur(UUID id, Utilisateur userDetails) {
        return utilisateurRepository.findById(id).map(user -> {
            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setEmail(userDetails.getEmail());
            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    public void deleteUtilisateur(UUID id) {
        utilisateurRepository.deleteById(id);
    }
}
