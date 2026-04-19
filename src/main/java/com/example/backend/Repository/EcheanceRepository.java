package com.example.backend.Repository;

import com.example.backend.Model.Echeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EcheanceRepository extends JpaRepository<Echeance, UUID> {
}