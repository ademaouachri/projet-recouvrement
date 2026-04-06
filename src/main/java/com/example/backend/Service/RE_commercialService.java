package com.example.backend.Service;

import com.example.backend.Model.*;
import com.example.backend.Repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RE_commercialService {

    private final ClientRepository clientRepository;

    public RE_commercialService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public List<Client> getClientsForUser(Utilisateur utilisateur) {
        Profil profil = utilisateur.getProfil();
        if (profil == null) return List.of();

        // ← نبنيوا الـ listes من الـ collections متاع الـ user
        List<String> agencyCodes = profil.getAgence() == 1
                ? utilisateur.getAgences().stream()
                .map(Agence::getCode).toList()
                : null;

        List<String> zoneCodes = profil.getZone() == 1
                ? utilisateur.getZones().stream()
                .map(Zone::getCode).toList()
                : null;

        List<String> regionCodes = profil.getRegion() == 1
                ? utilisateur.getRegions().stream()
                .map(Region::getCode).toList()
                : null;

        List<String> activityCodes = profil.getActivite() == 1
                ? utilisateur.getActivites().stream()
                .map(Activite::getCode).toList()
                : null;

        List<String> marcheCodes = profil.getMarche() == 1
                ? utilisateur.getMarches().stream()
                .map(Marche::getCode).toList()
                : null;

        List<String> segmentCodes = profil.getSegment() == 1
                ? utilisateur.getSegments().stream()
                .map(Segment::getCode).toList()
                : null;

        List<String> businessCenterCodes = profil.getCentreAffaire() == 1
                ? utilisateur.getCentreAffaires().stream()
                .map(CentreAffaire::getCode).toList()
                : null;

        return clientRepository.findByPerimetre(
                agencyCodes,
                zoneCodes,
                regionCodes,
                activityCodes,
                marcheCodes,
                segmentCodes,
                businessCenterCodes
        );
    }
}