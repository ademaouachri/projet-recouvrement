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
        if (zoneRepository.existsByCode(zone.getCode())) {
            throw new IllegalArgumentException("Une zone avec ce code existe déjà : " + zone.getCode());
        }


        if (zoneRepository.existsByLabel(zone.getLabel())) {
            throw new IllegalArgumentException("Une zone avec ce libellé existe déjà : " + zone.getLabel());
        }

        if(zone.getRegion() == null || zone.getRegion().getId()==null) {
            throw new ResourceNotFoundException("La région doit être fournie avec un ID valide");
        }
        else {
            UUID regionId = zone.getRegion().getId();
            Region region= regionRepository.findById(regionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Région introuvable avec l'ID: " + regionId));

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
                    .orElseThrow(() -> new ResourceNotFoundException("Région introuvable avec l'ID " + regionId));

            zoneDetails.setRegion(region);
            zone.setRegion(zoneDetails.getRegion());
            return zoneRepository.save(zone);
        }).orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID : " + id));
    }

    public void deleteZone(UUID id) {
        if (!zoneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Zone introuvable avec l'ID : " + id);
        }
        zoneRepository.deleteById(id);
    }
}
