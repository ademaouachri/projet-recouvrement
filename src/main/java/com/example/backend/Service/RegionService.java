package com.example.backend.Service;

import com.example.backend.Model.Region;
import com.example.backend.Repository.RegionRepository;

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
            region.setCode(regionDetails.getCode());
            region.setLabel(regionDetails.getLabel());
            return regionRepository.save(region);
        }).orElseThrow(() -> new RuntimeException("Region non trouvée avec l'id : " + id));
    }

    public void deleteRegion(UUID id) {
        regionRepository.deleteById(id);
    }
}
