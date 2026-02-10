package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Agence;
import com.example.backend.Repository.AgenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgenceService {

    private final AgenceRepository agenceRepository;

    public AgenceService(AgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;
    }

    public Agence createAgence(Agence agence) {
        return agenceRepository.save(agence);
    }

    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    public Optional<Agence> getAgenceById(UUID id) {
        return agenceRepository.findById(id);
    }

    public Agence updateAgence(UUID id, Agence agenceDetails) {
        return agenceRepository.findById(id).map(agence -> {
            agence.setCode(agenceDetails.getCode());
            agence.setLabel(agenceDetails.getLabel());
            return agenceRepository.save(agence);
        }).orElseThrow(() -> new ResourceNotFoundException("Agence non trouvée avec l'id : " + id));
    }

    public void deleteAgence(UUID id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence non trouvée avec l'id : " + id);
        }
        agenceRepository.deleteById(id);
    }
}
