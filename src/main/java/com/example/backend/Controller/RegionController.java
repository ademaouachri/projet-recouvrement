package com.example.backend.Controller;

import com.example.backend.Model.Region;
import com.example.backend.Service.RegionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    public Region createRegion(@Valid @RequestBody Region region) {
        return regionService.createRegion(region);
    }

    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable UUID id) {
        return regionService.getRegionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Region updateRegion(@PathVariable UUID id, @Valid @RequestBody Region region) {
        return regionService.updateRegion(id, region);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable UUID id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
