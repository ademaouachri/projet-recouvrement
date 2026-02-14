package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Palier;
import com.example.backend.Model.Profil;
import com.example.backend.Model.Utilisateur;

import com.example.backend.Repository.PalierRepository;
import com.example.backend.Repository.ProfilRepository;
import com.example.backend.Repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ProfilRepository profilRepository;
    private final OtpService otpService;
    private final PalierRepository palierRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, OtpService otpService,
            ProfilRepository profilRepository, PalierRepository palierRepository) {

        this.utilisateurRepository = utilisateurRepository;
        this.otpService = otpService;
        this.palierRepository = palierRepository;
        this.profilRepository = profilRepository;
    }

    public void registerUser(Utilisateur user) {
        if (utilisateurRepository.existsByMatricule(user.getMatricule().trim())) {
            throw new IllegalArgumentException("Le matricule est déjà utilisé !");
        }

        // Profil
        if (user.getProfil() != null) {
            if (user.getProfil().getId() != null) {
                Profil existingProfil = profilRepository.findById(user.getProfil().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Profil non trouvé avec l'id : " + user.getProfil().getId()));
                user.setProfil(existingProfil);
            }


        }

        // Paliers
        if (user.getPaliers() != null && !user.getPaliers().isEmpty()) {
            List<Palier> attachedPaliers = new ArrayList<>();
            for (Palier p : user.getPaliers()) {
                Palier existingPalier = palierRepository.findById(p.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Palier non trouvé avec l'id : " + p.getId()));
                attachedPaliers.add(existingPalier);
            }
            user.setPaliers(attachedPaliers);
        }

        try {
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setEnabled(false);
            Utilisateur savedUser = utilisateurRepository.save(user);
            otpService.sendOtp(savedUser.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
        }
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
            user.setProfil(userDetails.getProfil());
            user.setPaliers(userDetails.getPaliers());
            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
    }

    public void deleteUtilisateur(UUID id) {
        utilisateurRepository.deleteById(id);
    }
}
