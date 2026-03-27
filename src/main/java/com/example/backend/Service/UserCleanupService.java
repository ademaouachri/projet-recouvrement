package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCleanupService {

    private final UtilisateurRepository utilisateurRepository;

    public UserCleanupService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void removeUnverifiedUsers() {
        LocalDateTime expirationLimit = LocalDateTime.now().minusMinutes(1);


        List<Utilisateur> usersToDelete = utilisateurRepository
                .findByEnabledFalseAndCreatedAtBefore(expirationLimit);

        if (!usersToDelete.isEmpty()) {
            System.out.println("⚠️ Found " + usersToDelete.size() + " users to cleanup.");
            utilisateurRepository.deleteAllInBatch(usersToDelete);

            System.out.println("✅ [Cleanup Success] Users removed.");
        }
    }
    }
