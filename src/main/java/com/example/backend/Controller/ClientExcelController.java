package com.example.backend.Controller;

import com.example.backend.DTO.ImportResult;
import com.example.backend.Model.Client;
import com.example.backend.Repository.ClientRepository;
import com.example.backend.Service.ExcelImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientExcelController {

    private final ExcelImportService excelImportService;
    private final ClientRepository clientRepository;  // ✅ Ajouter cette ligne

    // ✅ Modifier le constructeur
    public ClientExcelController(ExcelImportService excelImportService, ClientRepository clientRepository) {
        this.excelImportService = excelImportService;
        this.clientRepository = clientRepository;
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

    // ✅ Ajouter cette méthode
    @GetMapping("/{cli}/montant-total")
    public ResponseEntity<BigDecimal> getMontantTotal(@PathVariable String cli) {
        Client client = clientRepository.findById(cli)
                .orElseThrow(() -> new RuntimeException("Client non trouvé : " + cli));

        BigDecimal montantTotal = client.getMontantTotal();

        return ResponseEntity.ok(montantTotal);
    }
}