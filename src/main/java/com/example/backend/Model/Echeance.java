package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(name = "ECHEANCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Echeance {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "MONTANT_ECHEANCE")
    private Double montantEcheance;

    @Column(name = "DATE_ECHEANCE")
    private String dateEcheance;

    @ManyToOne // علاقة "عديد الأقساط لتقرير واحد"
    @JoinColumn(name = "report_id")
    private Report report;
}