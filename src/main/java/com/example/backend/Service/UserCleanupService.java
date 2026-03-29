package com.example.backend.Service;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Configuration
@EnableScheduling
public class UserCleanupService implements SchedulingConfigurer {

    private final UtilisateurRepository utilisateurRepository;
    private final ParameterService parameterService;

    public UserCleanupService(UtilisateurRepository utilisateurRepository,
                              ParameterService parameterService) {
        this.utilisateurRepository = utilisateurRepository;
        this.parameterService = parameterService;
    }


    @Transactional
    public void removeUnverifiedUsers() {
        int minutes = getCleanupHours(); // On utilise 'minutes' selon ton dernier test
        LocalDateTime expirationLimit = LocalDateTime.now().minusMinutes(minutes);

        List<Utilisateur> usersToDelete = utilisateurRepository
                .findByEnabledFalseAndCreatedAtBefore(expirationLimit);

        if (!usersToDelete.isEmpty()) {
            System.out.println("⚠️ " + usersToDelete.size() + " utilisateur(s) à supprimer.");
            utilisateurRepository.deleteAllInBatch(usersToDelete);
            System.out.println("✅ Utilisateurs supprimés avec succès.");
        }
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                this::removeUnverifiedUsers,
                triggerContext -> {
                    String cron = getCronExpression();
                    return new CronTrigger(cron)
                            .nextExecutionTime(triggerContext)
                            .toInstant();
                }
        );
    }


    private int getCleanupHours() {
        String hoursStr = parameterService.getValueByKey("cleanupHoursLimit");
        try {
            if (hoursStr != null) {
                return Integer.parseInt(hoursStr.trim());
            }
        } catch (NumberFormatException e) {
            System.err.println("❌ Erreur: cleanupHoursLimit n'est pas un nombre valide. Utilisation de 24 par défaut.");
        }
        return 24;
    }


    private String getCronExpression() {
        String cron = parameterService.getValueByKey("cleanupCron");
        if (cron != null && !cron.isEmpty()) {
            return cron;
        }
        return "0 0 0 * * *";
    }
}