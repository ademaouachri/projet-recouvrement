package com.example.backend.Service;

import com.example.backend.DTO.PalierDto;
import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Palier;
import com.example.backend.Model.Region;
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


    public PalierService(PalierRepository palierRepository, UtilisateurRepository utilisateurRepository) {
        this.palierRepository = palierRepository;
    }

    public Palier createPalier(Palier palier) {

        if (palierRepository.existsByCode(palier.getCode())) {
            throw new IllegalArgumentException("Un palier avec ce code existe déjà : " + palier.getCode());
        }


        if (palierRepository.existsByLabel(palier.getLabel())) {
            throw new IllegalArgumentException("Un Palier avec ce label existe déjà : " + palier.getLabel());
        }

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
            palier.setLabel(palierDetails.getLabel());
            return palierRepository.save(palier);
        }).orElseThrow(() -> new ResourceNotFoundException("Palier non trouvé avec l'id : " + id));
    }
    public void deletePalier(UUID id) {

        if (!palierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Palier introuvable avec l'ID : " + id);
        }
        palierRepository.deleteById(id);
    }
}
