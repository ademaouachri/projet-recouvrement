package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Activite;
import com.example.backend.Repository.ActiviteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActiviteService {

    private final ActiviteRepository activiteRepository;

    public ActiviteService(ActiviteRepository activiteRepository) {

        this.activiteRepository = activiteRepository;
    }

    public Activite createActivite(Activite activite) {

        return activiteRepository.save(activite);
    }

    public List<Activite> getAllActivites() {
        return activiteRepository.findAll();
    }

    public Optional<Activite> getActiviteById(UUID id) {
        return activiteRepository.findById(id);
    }

    public Activite updateActivite(UUID id, Activite activiteDetails) {
        return activiteRepository.findById(id).map(activite -> {
            activite.setCode(activiteDetails.getCode());
            activite.setLabel(activiteDetails.getLabel());
            return activiteRepository.save(activite);
        }).orElseThrow(() -> new ResourceNotFoundException("Activite non trouvée avec l'id : " + id));
    }

    public void deleteActivite(UUID id) {

        activiteRepository.deleteById(id);
    }
}
