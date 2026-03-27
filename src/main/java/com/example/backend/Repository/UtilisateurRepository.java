package com.example.backend.Repository;

import com.example.backend.Model.Profil;
import com.example.backend.Model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);

    Utilisateur findByMatricule(String matricule);
   boolean existsByProfilId(UUID id);
    Optional<Utilisateur> findByActivationToken(String token);


    List<Utilisateur> findByEnabledFalseAndCreatedAtBefore(LocalDateTime dateTime);


}
