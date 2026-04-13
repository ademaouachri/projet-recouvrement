package com.example.backend.Service;

import com.example.backend.Model.*;
import com.example.backend.Repository.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RE_commercialService {

    // حماية من injection — كان حد يبعث حقل غريب نرجعو للـ default
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "totalImpayeAmount", "totalCommitment", "totalAuthorization",
            "outstanding", "totalDaysImpaye", "totalDaysSdb",
            "createdAt", "updatedAt", "birthDate"
    );

    private final ClientRepository clientRepository;

    public RE_commercialService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public List<Client> getClientsForUser(Utilisateur utilisateur,
                                          String sortBy, String sortDir) {
        Profil profil = utilisateur.getProfil();
        if (profil == null) return List.of();

        // ── Perimetre (نفس الكود القديم بالحرف) ──────────────────────
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

        // ── Sort ──────────────────────────────────────────────────────
        Sort sort;

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "createdAt";
        }

        if ("asc".equalsIgnoreCase(sortDir)) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        } else {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        }
        // ── نفس الـ call القديم + sort ────────────────────────────────
        return clientRepository.findByPerimetre(
                agencyCodes, zoneCodes, regionCodes,
                activityCodes, marcheCodes, segmentCodes,
                businessCenterCodes, sort
        );
    }
}