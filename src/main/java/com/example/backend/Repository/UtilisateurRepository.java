package com.example.backend.Repository;

import com.example.backend.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);

    Utilisateur findByMatricule(String matricule);


}
