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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {

    private final ClientRepository repository;
    private final ImportErrorLogRepository errorLogRepository;
    private final DataFormatter dataFormatter = new DataFormatter();

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
                if (rowNum == 1) continue; // تخطي الـ Header

                if (isRowEmpty(row)) continue;

                try {
                    // --- 1. استخراج البيانات من الخلايا ---
                    String cli = getCellValue(row, 0);
                    String agency = getCellValue(row, 1);
                    String businessCenter = getCellValue(row, 2);
                    String activity = getCellValue(row, 3);
                    String region = getCellValue(row, 4);
                    String marche = getCellValue(row, 5);
                    String segment = getCellValue(row, 6);
                    String zone = getCellValue(row, 7);
                    String fullName = getCellValue(row, 9);
                    String cin = getCellValue(row, 10);
                    String tel = getCellValue(row, 12);
                    String mail = getCellValue(row, 16);
                    String address = getCellValue(row, 17);

                    // حقول غير إجبارية لكن تقرأ للمتابعة
                    String classe = getCellValue(row, 8);
                    String structure = getCellValue(row, 40);

                    // --- 2. التحقق من الحقول الإجبارية (Validation) ---
                    List<String> missingFields = new ArrayList<>();
                    if (cli.isEmpty()) missingFields.add("CLI");
                    if (agency.isEmpty()) missingFields.add("AGENCY_CODE");
                    if (businessCenter.isEmpty()) missingFields.add("BUSINESS_CENTER");
                    if (activity.isEmpty()) missingFields.add("ACTIVITY");
                    if (region.isEmpty()) missingFields.add("REGION");
                    if (marche.isEmpty()) missingFields.add("MARCHE");
                    if (segment.isEmpty()) missingFields.add("SEGMENT");
                    if (zone.isEmpty()) missingFields.add("ZONE");
                    if (fullName.isEmpty()) missingFields.add("FULL_NAME");
                    if (cin.isEmpty()) missingFields.add("CIN");
                    if (tel.isEmpty()) missingFields.add("TEL");
                    if (mail.isEmpty()) missingFields.add("MAIL");
                    if (address.isEmpty()) missingFields.add("ADDRESS");

                    if (!missingFields.isEmpty()) {
                        String errorMsg = "Champs obligatoires manquants: " + String.join(", ", missingFields);
                        saveError("Validation", errorMsg, rowNum, result);
                        continue; // رفض السطر تماماً
                    }

                    // --- 3. الربط مع الـ Entity ---
                    Client client = repository.findById(cli).orElse(new Client());

                    client.setCli(cli);
                    client.setAgencyCode(agency);
                    client.setBusinessCenterCode(businessCenter);
                    client.setActivityCode(activity);
                    client.setRegionCode(region);
                    client.setMarchetCode(marche);
                    client.setSegmentCode(segment);
                    client.setZoneCode(zone);
                    client.setFullName(fullName);
                    client.setCin(cin);
                    client.setTel(tel);
                    client.setMail(mail);
                    client.setAddress(address);

                    // بقية الحقول (تكملة البيانات)
                    client.setBirthDate(parseDateSafe(row.getCell(11)));
                    client.setTel1(getCellValue(row, 13));
                    client.setTel2(getCellValue(row, 14));
                    client.setTel3(getCellValue(row, 15));
                    client.setAddress1(getCellValue(row, 18));
                    client.setAddress2(getCellValue(row, 19));
                    client.setPostalCode(getCellValue(row, 20));
                    client.setCity(getCellValue(row, 21));
                    client.setClasse(classe);
                    client.setStructure(structure);

                    // البيانات المالية
                    client.setTotalDaysImpaye(parseLongSafe(row.getCell(22)));
                    client.setTotalDaysSdb(parseLongSafe(row.getCell(23)));
                    client.setTotalImpayeAmount(parseBigDecimalSafe(row.getCell(24)));
                    client.setTotalDepassement(parseBigDecimalSafe(row.getCell(25)));
                    client.setTotalSdbAmount(parseBigDecimalSafe(row.getCell(26)));
                    client.setTotalCommitment(parseBigDecimalSafe(row.getCell(27)));
                    client.setOutstanding(parseBigDecimalSafe(row.getCell(28)));
                    client.setTotalAuthorization(parseBigDecimalSafe(row.getCell(29)));
                    client.setSectorCommitment(parseBigDecimalSafe(row.getCell(30)));

                    // حقول إضافية
                    client.setFollowUpType(getCellValue(row, 31));
                    client.setContactFlag(getCellValue(row, 32));
                    client.setTraite(getCellValue(row, 33));
                    client.setChequeRestriction(getCellValue(row, 34));
                    client.setSectorClass(getCellValue(row, 35));
                    client.setIsParticular(getCellValue(row, 36));
                    client.setEcheanceAutorisation(parseDateSafe(row.getCell(37)));
                    client.setDossierType(getCellValue(row, 38));
                    client.setClientGroup(getCellValue(row, 39));
                    client.setMotifParticular(getCellValue(row, 41));

                    repository.save(client);
                    result.setSuccessCount(result.getSuccessCount() + 1);

                } catch (Exception e) {
                    saveError("Mapping", "Erreur ligne " + rowNum + ": " + e.getMessage(), rowNum, result);
                }
            }
            result.setTotalRows(rowNum - 1);
            result.setSuccess(true);
        } catch (Exception e) {
            result.getErrors().add("Erreur Critique: " + e.getMessage());
        }
        return result;
    }

    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return (cell == null) ? "" : dataFormatter.formatCellValue(cell).trim();
    }

    private LocalDate parseDateSafe(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            if (cell.getCellType() == CellType.STRING) {
                String dateStr = dataFormatter.formatCellValue(cell).trim();
                if (!dateStr.isEmpty()) {
                    try { return LocalDate.parse(dateStr); }
                    catch (Exception ex) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        return LocalDate.parse(dateStr, formatter);
                    }
                }
            }
        } catch (Exception e) { return null; }
        return null;
    }

    private Long parseLongSafe(Cell cell) {
        try {
            if (cell == null || cell.getCellType() == CellType.BLANK) return 0L;
            if (cell.getCellType() == CellType.NUMERIC) return (long) cell.getNumericCellValue();
            String val = dataFormatter.formatCellValue(cell).replaceAll("[^\\d]", "");
            return val.isEmpty() ? 0L : Long.parseLong(val);
        } catch (Exception e) { return 0L; }
    }

    private BigDecimal parseBigDecimalSafe(Cell cell) {
        try {
            if (cell == null || cell.getCellType() == CellType.BLANK) return BigDecimal.ZERO;
            if (cell.getCellType() == CellType.NUMERIC) return BigDecimal.valueOf(cell.getNumericCellValue());
            String val = dataFormatter.formatCellValue(cell).replaceAll("[^\\d.]", "");
            return (val.isEmpty() || val.equals(".")) ? BigDecimal.ZERO : new BigDecimal(val);
        } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        Cell cell = row.getCell(0);
        return cell == null || cell.getCellType() == CellType.BLANK;
    }

    private void saveError(String field, String message, int rowNum, ImportResult result) {
        result.getErrors().add("Ligne " + rowNum + ": " + message);
        errorLogRepository.save(new ImportErrorLog(field, message, rowNum));
    }
}