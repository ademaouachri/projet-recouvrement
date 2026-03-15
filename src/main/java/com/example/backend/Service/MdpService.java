package com.example.backend.Service;

import org.springframework.stereotype.Service;

@Service
public class MdpService {

    private final EmailService emailService;

    public MdpService(EmailService emailService) {
        this.emailService = emailService;
    }

    // ⬅️ بدّلنا mdp بـ token
    public void sendActivationLink(String email, String token) {
        try {
            emailService.sendActivationEmail(email, token);
            System.out.println("Email d'activation envoyé à: " + email);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
