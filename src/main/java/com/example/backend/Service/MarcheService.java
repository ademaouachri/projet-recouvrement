package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Marche;
import com.example.backend.Model.Palier;
import com.example.backend.Model.Segment;
import com.example.backend.Repository.MarcheRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MarcheService {

    private final MarcheRepository marcheRepository;

    public MarcheService(MarcheRepository marcheRepository) {
        this.marcheRepository = marcheRepository;
    }

    public Marche createMarche(Marche marche) {
        if (marcheRepository.existsByCode(marche.getCode())) {
            throw new IllegalArgumentException("Un marché avec ce code existe déjà : " + marche.getCode());
        }


        if (marcheRepository.existsByLabel(marche.getLabel())) {
            throw new IllegalArgumentException("Un marché avec ce libellé existe déjà : " + marche.getLabel());
        }
        return marcheRepository.save(marche);
    }

    public List<Marche> getAllMarches() {
        return marcheRepository.findAll();
    }

    public Optional<Marche> getMarcheById(UUID id) {
        return marcheRepository.findById(id);
    }

    public Marche updateMarche(UUID id, Marche marcheDetails) {
        return marcheRepository.findById(id).map(marche -> {

            marche.setLabel(marcheDetails.getLabel());
            return marcheRepository.save(marche);
        }).orElseThrow(() -> new ResourceNotFoundException("Marche non trouvé avec l'id : " + id));
    }

    public void deleteMarche(UUID id) {

        if (!marcheRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Marché introuvable avec l'ID : " + id);
        }
        marcheRepository.deleteById(id);
    }
}
