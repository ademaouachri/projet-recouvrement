package com.example.backend.Controller;

import com.example.backend.Model.Report;
import com.example.backend.Service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200") // للسماح لـ Angular بالاتصال بالـ Backend
public class ReportController {

    private final ReportService reportService;

    // Injection par constructeur manuel (بدون @Autowired)
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * إضافة تقرير جديد
     * تخدم الـ 3 حالات: Visite, Promesse de règlement, Facilité de paiement
     */
    @PostMapping("/add")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        // الـ Service سيتكفل بربط الأقساط تلقائياً بفضل التعديل الأخير
        Report savedReport = reportService.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }

    /**
     * جلب قائمة جميع التقارير (مع الأقساط التابعة لها)
     */
    @GetMapping("/all")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * جلب تقرير واحد بالتفصيل عبر الـ UUID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable UUID id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * حذف تقرير (سيحذف الأقساط التابعة له تلقائياً بسبب CascadeType.ALL)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}