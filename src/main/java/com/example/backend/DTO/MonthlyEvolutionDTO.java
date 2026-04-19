package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyEvolutionDTO {
    private Integer year;
    private Integer month;

    private BigDecimal impaye;
    private BigDecimal sdb;
    private BigDecimal engagement;
    private BigDecimal engagementCloture;
    }

