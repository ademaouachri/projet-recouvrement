package com.example.backend.Service;

import com.example.backend.Model.PaymentStatus;
import com.example.backend.Model.Report;
import com.example.backend.Repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * جلب كل التقارير مع تحديث الحالة أوتوماتيكياً بناءً على بيانات البنك والتواريخ
     */
    public List<Report> getAllReportsWithAutoCheck() {
        List<Report> reports = reportRepository.findAll();

        for (Report report : reports) {
            try {
                // الاتصال بـ API البنك (تأكد أن البورت هو 8082 لمشروع البنك)
                String bankUrl = "http://localhost:8083/api/bank/total-paid?cli=" + report.getCli();
                Double totalPaidInBank = restTemplate.getForObject(bankUrl, Double.class);

                if (totalPaidInBank != null) {
                    report.setPaidAmount(totalPaidInBank);
                }

                // تطبيق المنطق الذكي لتحديث الحالة
                updatePaymentStatusBasedOnDate(report);

            } catch (Exception e) {
                System.err.println("Erreur API Banque pour CLI " + report.getCli() + ": " + e.getMessage());
                updatePaymentStatusBasedOnDate(report);
            }
        }
        return reports;
    }

    /**
     * المنطق الذكي لتحديد حالة الخلاص:
     * - PAYE: إذا تم دفع كامل المبلغ.
     * - EN_RETARD: إذا فات التاريخ في (Promesse) أو لم يتم دفع القسط الأول في (Facilité).
     * - PARTIEL: إذا تم دفع جزء يغطي القسط المطلوب في حال كانت التسهيلات مفعلة.
     */
    private void updatePaymentStatusBasedOnDate(Report report) {
        double paid = (report.getPaidAmount() != null) ? report.getPaidAmount() : 0.0;
        double total = (report.getAmount() != null) ? report.getAmount() : 0.0;
        boolean isPastDate = report.getEngagementDate() != null && isPastDate(report.getEngagementDate());

        // 1. الخلاص الكامل ديما PAYE
        if (paid >= total && total > 0) {
            report.setStatus(PaymentStatus.PAYE);
            return;
        }

        // 2. إذا فات التاريخ (isPastDate == true)
        if (isPastDate) {
            if ("Promesse de règlement".equals(report.getPoint())) {
                // في الوعد: أي نقص عن المبلغ الكامل في التاريخ المحدد يعتبر تأخير
                report.setStatus(PaymentStatus.EN_RETARD);
            }
            else if ("Facilité de paiement".equals(report.getPoint())) {
                // في التقسيط: نحسب قيمة القسط الواحد (المبلغ الجملي / عدد الأقساط)
                int nbEcheances = (report.getScheduleNumber() != null && report.getScheduleNumber() > 0)
                        ? report.getScheduleNumber() : 1;
                double amountPerEcheance = total / nbEcheances;

                // إذا صب على الأقل قيمة قسط واحد، نعتبروه PARTIEL موش متأخر
                if (paid >= amountPerEcheance) {
                    report.setStatus(PaymentStatus.PARTIEL);
                } else {
                    report.setStatus(PaymentStatus.EN_RETARD);
                }
            } else {
                // حالات أخرى
                report.setStatus(PaymentStatus.EN_RETARD);
            }
        }
        // 3. إذا مازال ما فاتش التاريخ
        else {
            if (paid > 0) {
                report.setStatus(PaymentStatus.PARTIEL);
            } else {
                report.setStatus(PaymentStatus.NON_PAYE);
            }
        }
    }

    /**
     * التحقق من التاريخ (مقارنة تاريخ التقرير مع تاريخ اليوم)
     */
    public boolean isPastDate(String dateStr) {
        try {
            // التنسيق المعتمد yyyy-MM-dd (تأكد أنه نفس التنسيق القادم من Angular)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate estimationDate = LocalDate.parse(dateStr, formatter);
            return estimationDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    // --- ميثودات CRUD الأساسية ---

    public Report saveReport(Report report) {
        if (report.getCreationDate() == null) {
            report.setCreationDate(LocalDateTime.now());
        }
        report.setUpdatedDate(LocalDateTime.now());
        updatePaymentStatusBasedOnDate(report);
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report introuvable"));
    }

    public void deleteReport(UUID id) {
        reportRepository.deleteById(id);
    }
}