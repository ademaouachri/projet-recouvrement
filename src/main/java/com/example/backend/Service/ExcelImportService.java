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
import java.util.ArrayList;
import java.util.List;

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
                if (rowNum == 1) continue; // Skip Header

                if (isRowEmpty(row)) continue;

                try {
                    // --- 1. قراءة البيانات (تثبت من الـ Index متاع الأعمدة في الـ Excel متاعك) ---
                    String cli = getCellValueAsString(row.getCell(0));
                    String cin = getCellValueAsString(row.getCell(10));
                    String fullName = getCellValueAsString(row.getCell(9));
                    String structure = getCellValueAsString(row.getCell(31));
                    String tel = getCellValueAsString(row.getCell(12));
                    String mail = getCellValueAsString(row.getCell(16)); // فرضنا Mail في العمود 16
                    String address = getCellValueAsString(row.getCell(17));
                    String activity = getCellValueAsString(row.getCell(3));
                    String region = getCellValueAsString(row.getCell(4));
                    String marche = getCellValueAsString(row.getCell(5));
                    String segment = getCellValueAsString(row.getCell(6));
                    String zone = getCellValueAsString(row.getCell(7));
                    String agency = getCellValueAsString(row.getCell(1));
                    String centres_affaire= getCellValueAsString(row.getCell(2));


                    // --- 2. الـ Validation الصارم (الإجباري) ---
                    List<String> missing = new ArrayList<>();
                    if (cli.isEmpty()) missing.add("CLI");
                    if (cin.isEmpty()) missing.add("CIN");
                    if (fullName.isEmpty()) missing.add("FULL_NAME");
                    if (tel.isEmpty()) missing.add("TEL");
                    if (mail.isEmpty()) missing.add("MAIL");
                    if (address.isEmpty()) missing.add("ADDRESS");
                    if (structure.isEmpty()) missing.add("STRUCTURE");
                    if (activity.isEmpty()) missing.add("ACTIVITY_CODE");
                    if (agency.isEmpty()) missing.add("AGENCY_CODE");
                    if (centres_affaire.isEmpty()) missing.add("CENTRES_AFFARE");
                    if (marche.isEmpty()) missing.add("MARCHE");
                    if (segment.isEmpty()) missing.add("SEGMENT");
                    if (zone.isEmpty()) missing.add("ZONE");
                    if (region.isEmpty()) missing.add("REGION");



                    if (!missing.isEmpty()) {
                        saveError("Validation", "Champs manquants: " + String.join(", ", missing), rowNum, result);
                        continue;
                    }

                    // --- 3. الـ Mapping والـ Save ---
                    Client client = repository.findById(cli).orElse(new Client());
                    client.setCli(cli);
                    client.setCin(cin);
                    client.setFullName(fullName);
                    client.setTel(tel);
                    client.setMail(mail);
                    client.setAddress(address);
                    client.setStructure(structure);
                    client.setActivityCode(activity);
                    client.setRegionCode(region);
                    client.setMarchetCode(marche);
                    client.setSegmentCode(segment);
                    client.setZoneCode(zone);
                    client.setAgencyCode(agency);

                    // تعمير الأرقام (مع الحذر من الـ Null)
                    client.setTotalDaysImpaye(parseLongSafe(row.getCell(22)));
                    client.setTotalDaysSdb(parseLongSafe(row.getCell(23)));
                    client.setTotalImpayeAmount(parseBigDecimalSafe(row.getCell(24)));
                    client.setTotalDepassement(parseBigDecimalSafe(row.getCell(25)));
                    client.setTotalSdbAmount(parseBigDecimalSafe(row.getCell(26)));
                    client.setTotalCommitment(parseBigDecimalSafe(row.getCell(27)));
                    client.setOutstanding(parseBigDecimalSafe(row.getCell(28)));
                    client.setTotalAuthorization(parseBigDecimalSafe(row.getCell(29)));
                    client.setSectorCommitment(parseBigDecimalSafe(row.getCell(30)));

                    repository.save(client);
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

    // --- Helpers النظافة ---

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        else if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            }
            return String.format("%.0f", cell.getNumericCellValue()).trim();
        }
        else if (type == CellType.FORMULA) {
            try {
                return cell.getStringCellValue().trim();
            } catch (Exception e) {
                return String.format("%.0f", cell.getNumericCellValue()).trim();
            }
        }
        else if (type == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        else {
            return "";
        }
    }

    private Long parseLongSafe(Cell cell) {
        try {
            return (long) cell.getNumericCellValue();
        } catch (Exception e) {
            return 0L;
        }
    }

    private BigDecimal parseBigDecimalSafe(Cell cell) {
        try {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void saveError(String field, String message, int rowNum, ImportResult result) {
        result.getErrors().add("Ligne " + rowNum + " (" + field + "): " + message);
        ImportErrorLog log = new ImportErrorLog(field, message, rowNum);
        errorLogRepository.save(log);
    }
}