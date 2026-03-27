package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Agence;
import com.example.backend.Model.Zone;
import com.example.backend.Repository.AgenceRepository;
import com.example.backend.Repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgenceService {

    private final AgenceRepository agenceRepository;
    private final ZoneRepository zoneRepository;

    public AgenceService(AgenceRepository agenceRepository, ZoneRepository zoneRepository) {
        this.agenceRepository = agenceRepository;
        this.zoneRepository = zoneRepository;
    }

    public Agence createAgence(Agence agence) {

        if (agenceRepository.existsByCode(agence.getCode())) {
            throw new IllegalArgumentException("Une agence avec ce code existe déjà : " + agence.getCode());
        }


        if (agenceRepository.existsByLabel(agence.getLabel())) {
            throw new IllegalArgumentException("Une agence avec ce libellé existe déjà : " + agence.getLabel());
        }

        if (agence.getZone() == null || agence.getZone().getId() == null) {
            throw new ResourceNotFoundException("La zone doit être fournie avec un ID valide");
        }
        else {
            UUID zoneId = agence.getZone().getId();
            Zone zone = zoneRepository.findById(zoneId)
                    .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID: " + zoneId));
            agence.setZone(zone);
            return agenceRepository.save(agence);
        }

    }

    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    public Optional<Agence> getAgenceById(UUID id) {
        return agenceRepository.findById(id);
    }

    public Agence updateAgence(UUID id, Agence agenceDetails) {
        return agenceRepository.findById(id).map(agence -> {

            agence.setLabel(agenceDetails.getLabel());

            UUID zoneId = agenceDetails.getZone().getId();
            Zone zone = zoneRepository.findById(zoneId)
                    .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID:: " + zoneId));
            agenceDetails.setZone(zone);
            agence.setZone(agenceDetails.getZone());

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
