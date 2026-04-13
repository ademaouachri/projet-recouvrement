package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientFilterRequest {

    // === Filters ===
    private String isParticular;
    private String contactFlag;
    private String traite;
    private String chequeRestriction;
    private String sectorClass;
    private String dossierType;
    private String followUpType;
    private String classe;
    private String city;
    private String clientGroup;

    private Boolean hasDepassement;   // true = totalDepassement > 0
    private Boolean hasSdbAmount;     // true = totalSdbAmount > 0
    private Boolean echeanceExpired;  // true = echeanceAutorisation < today

    // === Sort ===
    private String sortBy = "createdAt";       // default
    private String sortDir = "desc";            // asc | desc
}