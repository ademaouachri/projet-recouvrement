package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Zone;
import com.example.backend.Repository.ZoneRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone) {
       return  zoneRepository.save(zone);
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
            return zoneRepository.save(zone);
        }).orElseThrow(() -> new ResourceNotFoundException("Zone non trouvée avec l'id : " + id));
    }

    public void deleteZone(UUID id) {
        zoneRepository.deleteById(id);
    }
}
