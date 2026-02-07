package com.example.backend.Repository;

import com.example.backend.Model.Marche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MarcheRepository extends JpaRepository<Marche, UUID> {
}
