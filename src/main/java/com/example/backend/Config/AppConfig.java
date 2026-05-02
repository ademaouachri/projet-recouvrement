package com.example.backend.Config;

import com.example.backend.Model.Parameter;
import com.example.backend.Service.ParameterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class AppConfig {

    private final ParameterService parameterService;

    public AppConfig(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @Bean
    public CommandLineRunner initParameters() {
        return args -> {
            // 1. الإعدادات العادية (Mail, JWT, etc.)
            initParamIfAbsent("mailHost", "smtp.gmail.com");
            initParamIfAbsent("mailPort", "587");
            initParamIfAbsent("mailUsername", "hamauachri08@gmail.com");
            initParamIfAbsent("mailPassword", "uitdktketkurilji");
            initParamIfAbsent("jwtSecret", "thisIsMyVerySecretKeyForJwtWhichIsLongEnoughToAvoidWeakKeyException2026");
            initParamIfAbsent("jwtExpiration", "86400000");
            initParamIfAbsent("cleanupHoursLimit", "24");
            initParamIfAbsent("cleanupCron", "0 * * * * *");
            initParamIfAbsent("frontendUrl", "http://localhost:4200");

            // 2. إعدادات المراحل (Amiable & Commerciale) حسب النوع (IMP & SDB)
            // Phase Amiable
            initPhaseParamIfAbsent("phase amiable", "IMP", 60, 180);
            initPhaseParamIfAbsent("phase amiable", "SDB", 80, 100);

            // Phase Commerciale
            initPhaseParamIfAbsent("phase commerciale", "IMP", 61, 270);
            initPhaseParamIfAbsent("phase commerciale", "SDB", 10, 80);

            System.out.println("✅ [AppConfig] Tous les paramètres ont été initialisés.");
        };
    }

    // ميثود لزيادة الإعدادات البسيطة (Key/Value)
    private void initParamIfAbsent(String key, String defaultValue) {
        if (parameterService.getValueByKey(key) == null) {
            Parameter p = new Parameter();
            p.setKeyParam(key);
            p.setValueParam(defaultValue);
            parameterService.updateParam(p);
        }
    }

    // ✅ ميثود لزيادة إعدادات المراحل المركبة (بناءً على الصورة)
    private void initPhaseParamIfAbsent(String code, String type, int debut, int fin) {
        if (parameterService.getByCodeAndType(code, type) == null) {
            Parameter p = new Parameter();
            // نستخدم دمج الكود والنوع كـ Primary Key فريدة
            p.setKeyParam(code.replace(" ", "_") + "_" + type);
            p.setCodeParametre(code);
            p.setTypeParametre(type);
            p.setJourDebut(debut);
            p.setJourFin(fin);
            p.setValueParam(debut + " à " + fin + " jours");
            parameterService.updateParam(p);
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = parameterService.getValueByKey("mailHost");
        String port = parameterService.getValueByKey("mailPort");
        String user = parameterService.getValueByKey("mailUsername");
        String pass = parameterService.getValueByKey("mailPassword");

        mailSender.setHost(host != null ? host : "smtp.gmail.com");
        mailSender.setPort(port != null ? Integer.parseInt(port) : 587);
        mailSender.setUsername(user != null ? user : "hamauachri08@gmail.com");
        mailSender.setPassword(pass != null ? pass : "uitdktketkurilji");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }
}