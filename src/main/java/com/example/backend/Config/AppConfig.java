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
            initParamIfAbsent("mailHost", "smtp.gmail.com");
            initParamIfAbsent("mailPort", "587");
            initParamIfAbsent("mailUsername", "hamauachri08@gmail.com");
            initParamIfAbsent("mailPassword", "uitdktketkurilji");
            initParamIfAbsent("jwtSecret", "thisIsMyVerySecretKeyForJwtWhichIsLongEnoughToAvoidWeakKeyException2026");
            initParamIfAbsent("jwtExpiration", "86400000");
            initParamIfAbsent("cleanupHoursLimit", "24");
            initParamIfAbsent("cleanupCron", "0 * * * * *");
            System.out.println("✅ [AppConfig] Paramètres initialisés.");
        };
    }

    private void initParamIfAbsent(String key, String defaultValue) {
        if (parameterService.getValueByKey(key) == null) {
            Parameter p = new Parameter();
            p.setKeyParam(key);
            p.setValueParam(defaultValue);
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

        // ✅ إضافة الـ Fallback باش ما يخرجش "Cannot parse null string" في أول Run
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