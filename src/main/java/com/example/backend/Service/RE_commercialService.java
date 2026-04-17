package com.example.backend.Service;

import com.example.backend.Model.*;
import com.example.backend.Repository.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RE_commercialService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "totalImpayeAmount", "totalCommitment", "totalAuthorization",
            "outstanding", "totalDaysImpaye", "totalDaysSdb",
            "createdAt", "updatedAt", "totalDepassement", "totalSdbAmount"
    );

    private final ClientRepository clientRepository;

    public RE_commercialService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<Client> getClientsForUser(Utilisateur utilisateur,
                                          String postalCode,
                                          String dossierType,
                                          String structure,   // بارامتر جديد
                                          String fullName,    // بارامتر جديد
                                          String clientGroup,
                                          String createdBy,
                                          LocalDateTime startDate,
                                          String sortBy,
                                          String sortDir) {

        Profil profil = utilisateur.getProfil();
        if (profil == null) return List.of();

        // 1. تحضير الـ Perimetre
        List<String> agencyCodes = null;
        List<String> zoneCodes = null;
        List<String> regionCodes = null;
        List<String> activityCodes = null;
        List<String> marcheCodes = null;
        List<String> segmentCodes = null;
        List<String> businessCenterCodes = null;

        if (profil.getAgence() == 1 && utilisateur.getAgences() != null) {
            agencyCodes = new ArrayList<>();
            for (Agence a : utilisateur.getAgences()) agencyCodes.add(a.getCode());
        }
        if (profil.getZone() == 1 && utilisateur.getZones() != null) {
            zoneCodes = new ArrayList<>();
            for (Zone z : utilisateur.getZones()) zoneCodes.add(z.getCode());
        }
        if (profil.getRegion() == 1 && utilisateur.getRegions() != null) {
            regionCodes = new ArrayList<>();
            for (Region r : utilisateur.getRegions()) regionCodes.add(r.getCode());
        }
        if (profil.getActivite() == 1 && utilisateur.getActivites() != null) {
            activityCodes = new ArrayList<>();
            for (Activite a : utilisateur.getActivites()) activityCodes.add(a.getCode());
        }
        if (profil.getMarche() == 1 && utilisateur.getMarches() != null) {
            marcheCodes = new ArrayList<>();
            for (Marche m : utilisateur.getMarches()) marcheCodes.add(m.getCode());
        }
        if (profil.getSegment() == 1 && utilisateur.getSegments() != null) {
            segmentCodes = new ArrayList<>();
            for (Segment s : utilisateur.getSegments()) segmentCodes.add(s.getCode());
        }
        if (profil.getCentreAffaire() == 1 && utilisateur.getCentreAffaires() != null) {
            businessCenterCodes = new ArrayList<>();
            for (CentreAffaire ca : utilisateur.getCentreAffaires()) businessCenterCodes.add(ca.getCode());
        }

        // 2. تحضير الـ Sort
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "totalImpayeAmount";
        }

        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // 3. نبعثوا الـ 15 Parameter (زدنا structure و fullName)
        return clientRepository.findByPerimetre(
                agencyCodes,
                zoneCodes,
                regionCodes,
                activityCodes,
                marcheCodes,
                segmentCodes,
                businessCenterCodes,
                postalCode,
                dossierType,
                structure,  // بعثنا الـ structure للـ Repository
                fullName,   // بعثنا الـ fullName للـ Repository
                clientGroup,
                createdBy,
                startDate,
                sort
        );
    }
}