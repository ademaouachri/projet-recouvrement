package com.example.backend.Service;

import com.example.backend.Model.Echeance;
import com.example.backend.Model.Report;
import com.example.backend.Repository.ReportRepository;
import com.example.backend.Repository.EcheanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final EcheanceRepository echeanceRepository;

    // Injection par constructeur manuel
    public ReportService(ReportRepository reportRepository, EcheanceRepository echeanceRepository) {
        this.reportRepository = reportRepository;
        this.echeanceRepository = echeanceRepository;
    }

    /**
     * حفظ أو تحديث التقرير
     * يغطي الحالات الأربعة: Visite, Promesse, Facilité, Injoignable
     */
    public Report saveReport(Report report) {
        // 1. إدارة تواريخ الرقابة (Audit)
        if (report.getCreationDate() == null) {
            // حالة إنشاء تقرير جديد (Add)
            report.setCreationDate(LocalDateTime.now());
        }
        // تاريخ التحديث يتغير دائماً عند كل عملية حفظ (Add أو Update)
        report.setUpdatedDate(LocalDateTime.now());

        // 2. الربط المنطقي للأقساط (Bidirectional Link)
        // مهم جداً لنجاح عملية الـ Facilité de paiement
        if (report.getEcheances() != null && !report.getEcheances().isEmpty()) {
            for (Echeance echeance : report.getEcheances()) {
                echeance.setReport(report); // ربط القسط بالتقرير الأب
            }
        }

        // 3. الحفظ الفعلي
        // CascadeType.ALL في الـ Entity سيتكفل بحفظ الأقساط تلقائياً
        return reportRepository.save(report);
    }

    /**
     * جلب قائمة جميع التقارير من قاعدة البيانات
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * البحث عن تقرير معين بواسطة الـ UUID
     */
    public Report getReportById(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report introuvable avec l'ID : " + id));
    }

    /**
     * حذف تقرير
     * سيتم حذف الأقساط المرتبطة به آلياً من قاعدة البيانات
     */
    public void deleteReport(UUID id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : Report non trouvé !");
        }
        reportRepository.deleteById(id);
    }
}