package com.example.backend.Repository;

import com.example.backend.Model.Palier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PalierRepository extends JpaRepository<Palier, UUID> {
}
