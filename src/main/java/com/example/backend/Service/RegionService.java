package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Region;
import com.example.backend.Model.Zone;
import com.example.backend.Repository.RegionRepository;

import com.example.backend.Repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegionService {

    private final RegionRepository regionRepository;


    public RegionService(RegionRepository regionRepository) {


        this.regionRepository = regionRepository;
    }

    public Region createRegion(Region region) {
        if (regionRepository.existsByCode(region.getCode())) {
            throw new IllegalArgumentException("Une région avec ce code existe déjà : " + region.getCode());
        }


        if (regionRepository.existsByLabel(region.getLabel())) {
            throw new IllegalArgumentException("Une région avec ce libellé existe déjà : " + region.getLabel());
        }



        return regionRepository.save(region);
    }

    public List<Region> getAllRegions() {

        return regionRepository.findAll();
    }

    public Optional<Region> getRegionById(UUID id) {

        return regionRepository.findById(id);
    }

    public Region updateRegion(UUID id, Region regionDetails) {
        return regionRepository.findById(id).map(region -> {

            region.setLabel(regionDetails.getLabel());
            return regionRepository.save(region);
        }).orElseThrow(() -> new ResourceNotFoundException("Region non trouvée avec l'id : " + id));
    }

    public void deleteRegion(UUID id) {
        if (!regionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Région introuvable avec l'ID : " + id);
        }
        regionRepository.deleteById(id);
    }
}
