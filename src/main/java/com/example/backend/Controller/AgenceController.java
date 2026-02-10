package com.example.backend.Controller;

import com.example.backend.Model.Agence;
import com.example.backend.Service.AgenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final AgenceService agenceService;

    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping
    public Agence createAgence(@Valid @RequestBody Agence agence) {
        return agenceService.createAgence(agence);
    }

    @GetMapping
    public List<Agence> getAllAgences() {
        return agenceService.getAllAgences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agence> getAgenceById(@PathVariable UUID id) {
        return agenceService.getAgenceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Agence updateAgence(@PathVariable UUID id, @Valid @RequestBody Agence agence) {
        return agenceService.updateAgence(id, agence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgence(@PathVariable UUID id) {
        agenceService.deleteAgence(id);
        return ResponseEntity.noContent().build();
    }
}
