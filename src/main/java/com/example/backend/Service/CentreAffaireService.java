package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.CentreAffaire;
import com.example.backend.Repository.CentreAffaireRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CentreAffaireService {

    private final CentreAffaireRepository centreAffaireRepository;

    public CentreAffaireService(CentreAffaireRepository centreAffaireRepository) {
        this.centreAffaireRepository = centreAffaireRepository;
    }

    public CentreAffaire createCentreAffaire(CentreAffaire centreAffaire) {

        if (centreAffaireRepository.existsByCode(centreAffaire.getCode())) {
            throw new IllegalArgumentException("Un centre d'affaire avec ce code existe déjà: " + centreAffaire.getCode());
        }


        if (centreAffaireRepository.existsByLabel(centreAffaire.getLabel())) {
            throw new IllegalArgumentException("Un centre d'affaire avec ce libellé existe déjà : " + centreAffaire.getLabel());
        }

        return centreAffaireRepository.save(centreAffaire);
    }

    public List<CentreAffaire> getAllCentreAffaires() {
        return centreAffaireRepository.findAll();
    }

    public Optional<CentreAffaire> getCentreAffaireById(UUID id) {
        return centreAffaireRepository.findById(id);
    }

    public CentreAffaire updateCentreAffaire(UUID id, CentreAffaire centreAffaireDetails) {
        return centreAffaireRepository.findById(id).map(centreAffaire -> {

            centreAffaire.setLabel(centreAffaireDetails.getLabel());
            return centreAffaireRepository.save(centreAffaire);
        }).orElseThrow(() -> new ResourceNotFoundException("Centre d'affaire introuvable avec l'ID : " + id));
    }

    public void deleteCentreAffaire(UUID id) {

        if (!centreAffaireRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Centre d'affaire introuvable avec l'ID " + id);
        }
        centreAffaireRepository.deleteById(id);
    }
}
