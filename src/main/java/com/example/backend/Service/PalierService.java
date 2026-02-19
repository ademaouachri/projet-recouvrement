package com.example.backend.Service;

import com.example.backend.DTO.PalierDto;
import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Palier;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.PalierRepository;

import com.example.backend.Repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PalierService {

    private final PalierRepository palierRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PalierService(PalierRepository palierRepository, UtilisateurRepository utilisateurRepository) {
        this.palierRepository = palierRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Palier createPalier(Palier palier) {
        return palierRepository.save(palier);

    }

    public List<Palier> getAllPaliers() {
        return palierRepository.findAll();
    }

    public Optional<Palier> getPalierById(UUID id) {
        return palierRepository.findById(id);
    }

    public Palier updatePalier(UUID id, Palier palierDetails) {
        return palierRepository.findById(id).map(palier -> {
            palier.setCode(palierDetails.getCode());
            palier.setLabel(palierDetails.getLabel());
            return palierRepository.save(palier);
        }).orElseThrow(() -> new ResourceNotFoundException("Palier non trouvé avec l'id : " + id));
    }

    public void deletePalier(UUID id) {
        palierRepository.deleteById(id);
    }
}
