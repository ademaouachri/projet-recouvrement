package com.example.backend.Repository;

import com.example.backend.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);

    Utilisateur findByMatricule(String matricule);


}
