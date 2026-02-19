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
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final RegionRepository regionRepository;

    public ZoneService(ZoneRepository zoneRepository, RegionRepository regionRepository) {
        this.zoneRepository = zoneRepository;
        this.regionRepository = regionRepository;
    }

    public Zone createZone(Zone zone) {
        if(zone.getRegion() == null || zone.getRegion().getId()==null) {
            throw new ResourceNotFoundException("Region not found");
        }
        else {
            UUID regionId = zone.getRegion().getId();
            Region region= regionRepository.findById(regionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + regionId));

            zone.setRegion(region);
            return  zoneRepository.save(zone);
        }



    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Optional<Zone> getZoneById(UUID id) {
        return zoneRepository.findById(id);
    }

    public Zone updateZone(UUID id, Zone zoneDetails) {
        return zoneRepository.findById(id).map(zone -> {
            zone.setCode(zoneDetails.getCode());
            zone.setLabel(zoneDetails.getLabel());

            UUID regionId = zoneDetails.getRegion().getId();
            Region region= regionRepository.findById(regionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + regionId));

            zoneDetails.setRegion(region);
            zone.setRegion(zoneDetails.getRegion());
            return zoneRepository.save(zone);
        }).orElseThrow(() -> new ResourceNotFoundException("Zone non trouvée avec l'id : " + id));
    }

    public void deleteZone(UUID id) {
        zoneRepository.deleteById(id);
    }
}
