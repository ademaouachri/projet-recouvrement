package com.example.backend.Service;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final EmailService emailService;

    public OtpService(EmailService emailService) {
        this.emailService = emailService;
    }

    // Génère un code OTP de 4 chiffres
    public String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(otp);
    }

    // Envoie l'OTP par email
    public void sendOtp(String email, String otp) {
        try {
            emailService.sendOtpEmail(email, otp);
            System.out.println("Email envoyé avec succès à: " + email);
        } catch (Exception e) {
            System.err.println(" Erreur lors de l'envoi de l'email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean verifyOtp(String inputOtp, String actualOtp) {

        return inputOtp.equals(actualOtp);
    }
}
