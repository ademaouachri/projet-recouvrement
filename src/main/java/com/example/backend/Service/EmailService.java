package com.example.backend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Code de vérification");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='background: white; padding: 20px; border-radius: 8px; text-align: center;'>" +

                // LOGO
                "<img src='cid:logoImage' style='width:100px; margin-bottom:20px;' />" +

                "<h2 style='color: #333;'>🔐 Code de vérification</h2>" +
                "<p style='font-size: 16px;'>Votre code de vérification est :</p>" +
                "<h1 style='color: #007BFF; letter-spacing: 4px;'>" + otp + "</h1>" +
                "<p style='color: #777;'>Ne partagez ce code avec personne.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);

        helper.addInline(
                "logoImage",
                new ClassPathResource("static/logo.png"));

        mailSender.send(message);
    }

    public void sendMotPasse(String toEmail,String mdp)throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Votre nouveau mot de passe");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='background: white; padding: 20px; border-radius: 8px; text-align: center;'>" +

                // LOGO (اختياري)
                "<img src='cid:logoImage' style='width:100px; margin-bottom:20px;' />" +

                "<h2 style='color: #333;'>🔑  mot de passe</h2>" +
                "<p style='font-size: 16px;'>Votre mot de passe est :</p>" +
                "<h1 style='color: #007BFF; letter-spacing: 2px;'>" + mdp + "</h1>" +
                "<p style='color: #777;'>Ne partagez ce mot de passe avec personne.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);
        helper.addInline(
                "logoImage",
                new ClassPathResource("static/logo.png"));

        mailSender.send(message);


    }

}
