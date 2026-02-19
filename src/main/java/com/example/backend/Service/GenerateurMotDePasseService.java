package com.example.backend.Service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GenerateurMotDePasseService {

        private static final String CHOIX =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        private static final SecureRandom random = new SecureRandom();

        public static String generer() {
            int length = 8 + random.nextInt(5); // longueur entre 8 et 12

            String pass = "";

            for (int i = 0; i < length; i++) {
                int index = random.nextInt(CHOIX.length());
                pass = pass + CHOIX.charAt(index);
            }

            return pass;
        }


}

