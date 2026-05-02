package com.example.backend.Controller;

import com.example.backend.Model.Report;
import com.example.backend.Service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * جلب قائمة جميع التقارير مع التثبت الأوتوماتيكي من البنك
     */
    @GetMapping("/all")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReportsWithAutoCheck();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/add")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report savedReport = reportService.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable UUID id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ AJOUTER CETTE MÉTHODE
    @GetMapping("/is-past-date")
    public ResponseEntity<Boolean> isPastDate(@RequestParam String date) {
        boolean result = reportService.isPastDate(date);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pay")
    public ResponseEntity<Report> pay(@RequestParam String cli,
                                      @RequestParam double amount) {
        return ResponseEntity.ok(reportService.saveReport(null));
    }
}