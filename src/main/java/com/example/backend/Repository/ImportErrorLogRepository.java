package com.example.backend.Repository;

import com.example.backend.Model.ImportErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportErrorLogRepository extends JpaRepository<ImportErrorLog, String> {
}
