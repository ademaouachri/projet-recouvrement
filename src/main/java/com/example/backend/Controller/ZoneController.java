package com.example.backend.Controller;

import com.example.backend.Model.Zone;
import com.example.backend.Service.ZoneService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public Zone createZone(@RequestBody @Valid Zone zone) {
        return zoneService.createZone(zone);
    }

    @GetMapping
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable UUID id) {
        return zoneService.getZoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Zone updateZone(@PathVariable UUID id, @Valid @RequestBody Zone zone) {
        return zoneService.updateZone(id, zone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable UUID id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }
}
