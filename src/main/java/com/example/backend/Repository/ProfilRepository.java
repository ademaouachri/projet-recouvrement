package com.example.backend.Repository;

import com.example.backend.Model.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfilRepository  extends JpaRepository<Profil, UUID> {


}
