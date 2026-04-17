package com.example.backend.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DashboardStats {
    private Long totalDossiers;
    private BigDecimal totalImpayes;
    private BigDecimal totalSdb;
    private BigDecimal totalEngagement;
    private Long facilitePaiementCount;
    private Long promessePaiementCount;
    private Long sommation1Count;
    private Long sommation2Count;
    private List<MonthlyEvolutionDTO> monthlyEvolution;

    // زيد الـ Constructor هذا بالظبط (8 برامترات)
    public DashboardStats(Long totalDossiers, BigDecimal totalImpayes, BigDecimal totalSdb,
                          BigDecimal totalEngagement, Long facilitePaiementCount,
                          Long promessePaiementCount, Long sommation1Count, Long sommation2Count) {
        this.totalDossiers = totalDossiers;
        this.totalImpayes = totalImpayes != null ? totalImpayes : BigDecimal.ZERO;
        this.totalSdb = totalSdb != null ? totalSdb : BigDecimal.ZERO;
        this.totalEngagement = totalEngagement != null ? totalEngagement : BigDecimal.ZERO;
        this.facilitePaiementCount = facilitePaiementCount;
        this.promessePaiementCount = promessePaiementCount;
        this.sommation1Count = sommation1Count;
        this.sommation2Count = sommation2Count;
    }
}