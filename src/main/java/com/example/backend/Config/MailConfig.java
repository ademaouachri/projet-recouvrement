package com.example.backend.Config;

import com.example.backend.Service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
@Configuration
public class MailConfig {

        @Autowired
        private ParameterService parameterService;

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

