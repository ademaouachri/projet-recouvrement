package com.example.backend.Service;

import com.example.backend.DTO.ImportResult;
import com.example.backend.Model.Client;
import com.example.backend.Model.ImportErrorLog;
import com.example.backend.Model.Parameter;
import com.example.backend.Repository.ClientRepository;
import com.example.backend.Repository.ImportErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final ClientRepository repository;
    private final ImportErrorLogRepository errorLogRepository;
    private final ParameterService parameterService; // ✅ تم إضافة السيرفيس
    private final DataFormatter dataFormatter = new DataFormatter();

    @Transactional
    public ImportResult processExcelFile(MultipartFile file) {
        ImportResult result = new ImportResult();
        List<Client> clientsToSave = new ArrayList<>();
        int rowNum = 0;
        String fileName = file.getOriginalFilename();

        // حذف الأخطاء القديمة
        errorLogRepository.deleteByFileName(fileName);

        // 1. جلب البارامترات مرة واحدة للسرعة
        Parameter amiableIMP = parameterService.getByCodeAndType("phase amiable", "IMP");
        Parameter amiableSDB = parameterService.getByCodeAndType("phase amiable", "SDB");
        Parameter commIMP = parameterService.getByCodeAndType("phase commerciale", "IMP");
        Parameter commSDB = parameterService.getByCodeAndType("phase commerciale", "SDB");

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                rowNum++;
                if (rowNum == 1 || isRowEmpty(row)) continue;

                try {
                    // 2. قراءة البيانات للـ Validation الصارم (نفس منطقك)
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

                    List<String> missing = new ArrayList<>();
                    if (cli.isEmpty()) missing.add("CLI");
                    if (agency.isEmpty()) missing.add("AGENCY_CODE");
                    if (businessCenter.isEmpty()) missing.add("BUSINESS_CENTER_CODE");
                    if (activity.isEmpty()) missing.add("ACTIVITY_CODE");
                    if (region.isEmpty()) missing.add("REGION_CODE");
                    if (marche.isEmpty()) missing.add("MARCHE_CODE");
                    if (segment.isEmpty()) missing.add("SEGMENT_CODE");
                    if (zone.isEmpty()) missing.add("ZONE_CODE");
                    if (fullName.isEmpty()) missing.add("FULL_NAME");
                    if (cin.isEmpty()) missing.add("CIN");
                    if (tel.isEmpty()) missing.add("TEL");
                    if (mail.isEmpty()) missing.add("MAIL");
                    if (address.isEmpty()) missing.add("ADDRESS");

                    if (!missing.isEmpty()) {
                        saveError(fileName, "Validation", "Champs manquants: " + String.join(", ", missing), rowNum, result);
                        continue;
                    }

                    // 3. إدارة الـ Entity
                    Optional<Client> existingClient = repository.findById(cli);
                    Client client = existingClient.orElse(new Client());

                    if (existingClient.isPresent()) {
                        result.setUpdateCount(result.getUpdateCount() + 1);
                    } else {
                        client.setCli(cli);
                        result.setSuccessCount(result.getSuccessCount() + 1);
                    }

                    // 4. المابينغ مع تعمير الـ Structure آلياً (المنطق الجديد)
                    mapRowToClient(row, client, amiableIMP, amiableSDB, commIMP, commSDB);

                    clientsToSave.add(client);

                    if (clientsToSave.size() >= 50) {
                        repository.saveAll(clientsToSave);
                        clientsToSave.clear();
                    }

                } catch (Exception e) {
                    saveError(fileName, "Technical", "Erreur ligne " + rowNum + ": " + e.getMessage(), rowNum, result);
                }
            }

            if (!clientsToSave.isEmpty()) {
                repository.saveAll(clientsToSave);
            }

            result.setTotalRows(rowNum - 1);
            result.setSuccess(true);

        } catch (Exception e) {
            result.setSuccess(false);
            result.getErrors().add("Erreur Critique: " + e.getMessage());
        }
        return result;
    }

    private void mapRowToClient(Row row, Client client, Parameter aIMP, Parameter aSDB, Parameter cIMP, Parameter cSDB) {
        // مابينغ المعلومات الأساسية (كما هي في كودك)
        client.setAgencyCode(getCellValue(row, 1));
        client.setBusinessCenterCode(getCellValue(row, 2));
        client.setActivityCode(getCellValue(row, 3));
        client.setRegionCode(getCellValue(row, 4));
        client.setMarchetCode(getCellValue(row, 5));
        client.setSegmentCode(getCellValue(row, 6));
        client.setZoneCode(getCellValue(row, 7));
        client.setClasse(getCellValue(row, 8));
        client.setFullName(getCellValue(row, 9));
        client.setCin(getCellValue(row, 10));
        client.setBirthDate(parseDateSafe(row.getCell(11)));
        client.setTel(getCellValue(row, 12));
        client.setTel1(getCellValue(row, 13));
        client.setTel2(getCellValue(row, 14));
        client.setTel3(getCellValue(row, 15));
        client.setMail(getCellValue(row, 16));
        client.setAddress(getCellValue(row, 17));
        client.setAddress1(getCellValue(row, 18));
        client.setAddress2(getCellValue(row, 19));
        client.setPostalCode(getCellValue(row, 20));
        client.setCity(getCellValue(row, 21));

        // البيانات المالية
        long daysImpaye = parseLongSafe(row.getCell(22));
        long daysSdb = parseLongSafe(row.getCell(23));
        client.setTotalDaysImpaye(daysImpaye);
        client.setTotalDaysSdb(daysSdb);
        client.setTotalImpayeAmount(parseBigDecimalSafe(row.getCell(24)));
        client.setTotalDepassement(parseBigDecimalSafe(row.getCell(25)));
        client.setTotalSdbAmount(parseBigDecimalSafe(row.getCell(26)));
        client.setTotalCommitment(parseBigDecimalSafe(row.getCell(27)));
        client.setOutstanding(parseBigDecimalSafe(row.getCell(28)));
        client.setTotalAuthorization(parseBigDecimalSafe(row.getCell(29)));
        client.setSectorCommitment(parseBigDecimalSafe(row.getCell(30)));

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

        // --- ✅ تعمير الـ Structure بناءً على الأيام والبارامترات ---
        if (daysSdb > 0) {
            if (checkRange(daysSdb, aSDB)) client.setStructure("S003");
            else if (checkRange(daysSdb, cSDB)) client.setStructure("S002");
            else client.setStructure(getCellValue(row, 40));
        } else {
            if (checkRange(daysImpaye, aIMP)) client.setStructure("S003");
            else if (checkRange(daysImpaye, cIMP)) client.setStructure("S002");
            else client.setStructure(getCellValue(row, 40));
        }

        // تاريخ الإضافة (كما هو في كودك)
        LocalDateTime excelDate = parseLocalDateTimeSafe(row.getCell(42));
        if (excelDate != null) {
            client.setCreatedAt(excelDate);
        } else if (client.getCreatedAt() == null) {
            client.setCreatedAt(LocalDateTime.now());
        }
    }

    private boolean checkRange(long days, Parameter param) {
        return param != null && days >= param.getJourDebut() && days <= param.getJourFin();
    }

    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return (cell == null) ? "" : dataFormatter.formatCellValue(cell).trim();
    }

    private LocalDateTime parseLocalDateTimeSafe(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            String val = dataFormatter.formatCellValue(cell).trim();
            if (val.isEmpty()) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]");
            return LocalDateTime.parse(val, formatter);
        } catch (Exception e) { return null; }
    }

    private LocalDate parseDateSafe(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            return null;
        } catch (Exception e) { return null; }
    }

    private Long parseLongSafe(Cell cell) {
        try {
            if (cell == null) return 0L;
            if (cell.getCellType() == CellType.NUMERIC) return (long) cell.getNumericCellValue();
            String val = dataFormatter.formatCellValue(cell).replaceAll("[^\\d]", "");
            return val.isEmpty() ? 0L : Long.parseLong(val);
        } catch (Exception e) { return 0L; }
    }

    private BigDecimal parseBigDecimalSafe(Cell cell) {
        try {
            if (cell == null) return BigDecimal.ZERO;
            if (cell.getCellType() == CellType.NUMERIC) return BigDecimal.valueOf(cell.getNumericCellValue());
            String val = dataFormatter.formatCellValue(cell).replaceAll("[^\\d.]", "");
            return val.isEmpty() ? BigDecimal.ZERO : new BigDecimal(val);
        } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        Cell cell = row.getCell(0);
        return cell == null || cell.getCellType() == CellType.BLANK;
    }

    private void saveError(String fileName, String field, String message, int rowNum, ImportResult result) {
        result.getErrors().add("Ligne " + rowNum + ": " + message);
        errorLogRepository.save(new ImportErrorLog(fileName, field, message, rowNum));
    }
}