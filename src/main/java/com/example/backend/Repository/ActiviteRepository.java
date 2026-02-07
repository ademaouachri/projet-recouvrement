package com.example.backend.Repository;

import com.example.backend.Model.Activite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, UUID> {
}
