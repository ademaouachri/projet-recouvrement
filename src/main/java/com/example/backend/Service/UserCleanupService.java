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

    // Injection par constructeur
    public UserCleanupService(UtilisateurRepository utilisateurRepository,
                              ParameterService parameterService) {
        this.utilisateurRepository = utilisateurRepository;
        this.parameterService = parameterService;
    }

    @Transactional
    public void removeUnverifiedUsers() {
        // Lecture directe depuis la DB (On fait confiance à l'init)
        int hours = Integer.parseInt(parameterService.getValueByKey("cleanupHoursLimit"));
        LocalDateTime expirationLimit = LocalDateTime.now().minusMinutes(hours);

        List<Utilisateur> usersToDelete = utilisateurRepository
                .findByEnabledFalseAndCreatedAtBefore(expirationLimit);

        if (!usersToDelete.isEmpty()) {
            System.out.println("⚠️ Nettoyage : " + usersToDelete.size() + " utilisateur(s) non vérifié(s) à supprimer.");
            utilisateurRepository.deleteAllInBatch(usersToDelete);
            System.out.println("✅ Nettoyage terminé avec succès.");
        }
    }

    /**
     * Configuration dynamique de la tâche planifiée (Scheduled Task)
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                this::removeUnverifiedUsers,
                triggerContext -> {
                    // قراءة الـ Cron من الـ DB
                    String cron = parameterService.getValueByKey("cleanupCron");

                    // ✅ الحماية: إذا الـ DB فارغة (null)، نستعملو قيمة افتراضية باش ما يوقفش البروجي
                    if (cron == null || cron.trim().isEmpty()) {
                        cron = "0 0 0 * * *"; // كل يوم نص ليل
                    }

                    return new CronTrigger(cron)
                            .nextExecutionTime(triggerContext)
                            .toInstant();
                }
        );
    }
}