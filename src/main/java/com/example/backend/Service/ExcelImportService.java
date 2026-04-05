package com.example.backend.Service;

import com.example.backend.Model.Client;
import com.example.backend.Model.ImportErrorLog;
import com.example.backend.Repository.ClientRepository;
import com.example.backend.Repository.ImportErrorLogRepository;
import com.example.backend.DTO.ImportResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ExcelImportService {

    private final ClientRepository repository;
    private final ImportErrorLogRepository errorLogRepository;

    public ExcelImportService(ClientRepository repository, ImportErrorLogRepository errorLogRepository) {
        this.repository = repository;
        this.errorLogRepository = errorLogRepository;
    }

    public ImportResult processExcelFile(MultipartFile file) {
        ImportResult result = new ImportResult();
        int rowNum = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                rowNum++;
                if (rowNum == 1) continue; // skip header

                // ─── 1. التثبت من أن السطر ليس فارغاً ───────────────────────
                if (isRowEmpty(row)) continue;

                try {
                    // قراءة البيانات (تأكد من مطابقة أرقام الأعمدة لملفك)
                    String cli = getCellValueAsString(row.getCell(0));
                    String cin = getCellValueAsString(row.getCell(10));
                    String fullName = getCellValueAsString(row.getCell(9));
                    String structure = getCellValueAsString(row.getCell(31));

                    // باقي الحقول... (نفس الكود السابق)

                    // ─── 2. Validation ──────────────────────────────────────
                    List<String> missing = new ArrayList<>();
                    if (cli.isEmpty()) missing.add("CLI");
                    if (cin.isEmpty()) missing.add("CIN");
                    if (fullName.isEmpty()) missing.add("FULL_NAME");
                    if (structure.isEmpty()) missing.add("STRUCTURE");

                    if (!missing.isEmpty()) {
                        saveError("Validation", "Champs manquants: " + String.join(", ", missing), rowNum, result);
                        continue;
                    }

                    // ─── 3. منطق الحفظ (Upsert) ──────────────────────────────
                    // ... (Logic الـ Optional بالـ CLI والـ CIN اللي خدمناه قبيلة)

                    // repository.save(client);
                    result.setSuccessCount(result.getSuccessCount() + 1);

                } catch (Exception e) {
                    saveError("General", "Erreur ligne " + rowNum + ": " + e.getMessage(), rowNum, result);
                }
            }
            result.setTotalRows(rowNum - 1);
            result.setSuccess(true);
        } catch (Exception e) {
            result.getErrors().add("Erreur Critique: " + e.getMessage());
        }
        return result;
    }

    // ─── Helpers المطورين ──────────────────────────────────────────────────

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        Cell firstCell = row.getCell(0); 
        return firstCell == null || getCellValueAsString(firstCell).isEmpty();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                // لتفادي ظهور الأرقام مثل 1.23E10
                return String.format("%.0f", cell.getNumericCellValue()).trim();
            case FORMULA:
                try { return cell.getStringCellValue(); }
                catch (Exception e) { return String.valueOf(cell.getNumericCellValue()); }
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private void saveError(String field, String message, int rowNum, ImportResult result) {
        result.getErrors().add("Ligne " + rowNum + " (" + field + "): " + message);
        ImportErrorLog log = new ImportErrorLog(field, message, rowNum);
        errorLogRepository.save(log);
    }
}