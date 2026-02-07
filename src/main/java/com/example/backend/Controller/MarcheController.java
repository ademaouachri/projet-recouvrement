package com.example.backend.Controller;

import com.example.backend.Model.Marche;
import com.example.backend.Service.MarcheService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/marches")
public class MarcheController {

    private final MarcheService marcheService;

    public MarcheController(MarcheService marcheService) {
        this.marcheService = marcheService;
    }

    @PostMapping
    public Marche createMarche(@Valid @RequestBody Marche marche) {
        return marcheService.createMarche(marche);
    }

    @GetMapping
    public List<Marche> getAllMarches() {
        return marcheService.getAllMarches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marche> getMarcheById(@PathVariable UUID id) {
        return marcheService.getMarcheById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Marche updateMarche(@PathVariable UUID id, @Valid @RequestBody Marche marche) {
        return marcheService.updateMarche(id, marche);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarche(@PathVariable UUID id) {
        marcheService.deleteMarche(id);
        return ResponseEntity.noContent().build();
    }
}
