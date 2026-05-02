package com.example.backend.Repository;

import com.example.backend.Model.ImportErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImportErrorLogRepository extends JpaRepository<ImportErrorLog, UUID> {

    // ✅ Supprimer les erreurs par nom de fichier
    void deleteByFileName(String fileName);
}