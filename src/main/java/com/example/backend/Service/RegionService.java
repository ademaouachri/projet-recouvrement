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
    private final ZoneRepository zoneRepository;

    public RegionService(RegionRepository regionRepository, ZoneRepository zoneRepository) {

        this.zoneRepository = zoneRepository;
        this.regionRepository = regionRepository;
    }

    public Region createRegion(Region region) {
        // Validation: Ensure Zone and Zone ID are provided
        if (region.getZone() == null || region.getZone().getId() == null) {
            throw new ResourceNotFoundException("Zone must be provided with a valid ID");
        }

        UUID zoneId = region.getZone().getId();

        // fetch Zone from database
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + zoneId));

        region.setZone(zone);

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
        }).orElseThrow(() -> new ResourceNotFoundException("Region non trouvée avec l'id : " + id));
    }

    public void deleteRegion(UUID id) {
        regionRepository.deleteById(id);
    }
}
