package com.example.backend.Controller;

import com.example.backend.Model.ImportErrorLog;
import com.example.backend.Repository.ImportErrorLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-errors")
public class ImportErrorLogController {

    private final ImportErrorLogRepository errorLogRepository;

    public ImportErrorLogController(ImportErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    @GetMapping
    public List<ImportErrorLog> getAllErrors() {
        return errorLogRepository.findAll();
    }


    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllErrors() {
        errorLogRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}