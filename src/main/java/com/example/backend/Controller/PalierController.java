package com.example.backend.Controller;

import com.example.backend.Model.Palier;
import com.example.backend.Service.PalierService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/paliers")
public class PalierController {

    private final PalierService palierService;

    public PalierController(PalierService palierService) {
        this.palierService = palierService;
    }

    @PostMapping
    public Palier createPalier(@Valid @RequestBody Palier palier) {
        return palierService.createPalier(palier);
    }

    @GetMapping
    public List<Palier> getAllPaliers() {
        return palierService.getAllPaliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Palier> getPalierById(@PathVariable UUID id) {
        return palierService.getPalierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Palier updatePalier(@PathVariable UUID id, @Valid @RequestBody Palier palier) {
        return palierService.updatePalier(id, palier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePalier(@PathVariable UUID id) {
        palierService.deletePalier(id);
        return ResponseEntity.noContent().build();
    }
}
