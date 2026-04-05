package com.example.backend.Controller;

import com.example.backend.DTO.ImportResult;
import com.example.backend.Service.ExcelImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientExcelController {

    private final ExcelImportService excelImportService;

    public ClientExcelController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping("/import-excel")
    public ResponseEntity<ImportResult> importExcel(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            ImportResult error = new ImportResult();
            error.setSuccess(false);
            error.getErrors().add("❌ Le fichier est vide");
            return ResponseEntity.badRequest().body(error);
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            ImportResult error = new ImportResult();
            error.setSuccess(false);
            error.getErrors().add("❌ Veuillez uploader un fichier Excel (.xlsx)");
            return ResponseEntity.badRequest().body(error);
        }

        ImportResult result = excelImportService.processExcelFile(file);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
    }
}