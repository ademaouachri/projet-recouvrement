package com.example.backend.Service;

import org.springframework.stereotype.Service;

@Service
public class MdpService {

    private final EmailService emailService;

    public MdpService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendMdp(String email, String mdp) {
        try {
            emailService.sendMotPasse(email, mdp);
            System.out.println("Email envoyé avec succès à: " + email);
        } catch (Exception e) {
            System.err.println(" Erreur lors de l'envoi de l'email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
