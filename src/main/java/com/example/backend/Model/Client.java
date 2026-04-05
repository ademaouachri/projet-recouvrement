package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CLIENT")
public class Client {

    @Id
    @Column(name = "CLI", nullable = false)
    private String cli;

    @Column(name = "AGENCY_CODE")
    private String agencyCode;

    @Column(name = "BUSINESS_CENTER_CODE")
    private String businessCenterCode;

    @Column(name = "ACTIVITY_CODE")
    private String activityCode;

    @Column(name = "REGION_CODE")
    private String regionCode;

    @Column(name = "MARCHE_CODE")
    private String marchetCode;

    @Column(name = "SEGMENT_CODE")
    private String segmentCode;

    @Column(name = "ZONE_CODE")
    private String zoneCode;

    @Column(name = "CLASSE")
    private String classe;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "CIN")
    private String cin;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "TEL1")
    private String tel1;

    @Column(name = "TEL2")
    private String tel2;

    @Column(name = "TEL3")
    private String tel3;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ADDRESS1")
    private String address1;

    @Column(name = "ADDRESS2")
    private String address2;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "CITY")
    private String city;


    @Column(name = "TOTAL_DAYS_IMPAYE")
    private Long totalDaysImpaye;

    @Column(name = "TOTAL_DAYS_SDB")
    private Long totalDaysSdb;

    @Column(name = "TOTAL_IMPAYE_AMOUNT", precision = 20, scale = 3)
    private BigDecimal totalImpayeAmount;

    @Column(name = "TOTAL_DEPASSEMENT", precision = 20, scale = 3)
    private BigDecimal totalDepassement;

    @Column(name = "TOTAL_SDB_AMOUNT", precision = 20, scale = 3)
    private BigDecimal totalSdbAmount;

    @Column(name = "TOTAL_COMMITMENT", precision = 20, scale = 3)
    private BigDecimal totalCommitment;

    @Column(name = "OUTSTANDING", precision = 20, scale = 3)
    private BigDecimal outstanding;

    @Column(name = "TOTAL_AUTHORIZATION", precision = 20, scale = 3)
    private BigDecimal totalAuthorization;

    @Column(name = "SECTOR_COMMITMENT", precision = 20, scale = 3)
    private BigDecimal sectorCommitment;

    // ====== الأعمدة النصية ======
    @Column(name = "FOLLOW_UP_TYPE")
    private String followUpType;

    @Column(name = "CONTACT_FLAG")
    private String contactFlag;

    @Column(name = "TRAITE", length = 1)
    private String traite;

    @Column(name = "CHEQUE_RESTRICTION")
    private String chequeRestriction;

    @Column(name = "SECTOR_CLASS", length = 1)
    private String sectorClass;

    @Column(name = "IS_PARTICULAR", length = 1)
    private String isParticular;

    @Column(name = "ECHEANCE_AUTORISATION")
    private LocalDate echeanceAutorisation;

    @Column(name = "DOSSIER_TYPE")
    private String dossierType;

    @Column(name = "CLIENT_GROUP")
    private String clientGroup;

    @Column(name = "STRUCTURE")
    private String structure;

    @Column(name = "MOTIF_PARTICULAR")
    private String motifParticular;

    // ====== الأعمدة التلقائية ======
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}