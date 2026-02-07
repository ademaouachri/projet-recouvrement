package com.example.backend.Repository;

import com.example.backend.Model.CentreAffaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CentreAffaireRepository extends JpaRepository<CentreAffaire, UUID> {
}
