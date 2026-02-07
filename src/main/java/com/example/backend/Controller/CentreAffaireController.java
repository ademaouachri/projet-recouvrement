package com.example.backend.Controller;

import com.example.backend.Model.CentreAffaire;
import com.example.backend.Service.CentreAffaireService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/centres-affaire")
public class CentreAffaireController {

    private final CentreAffaireService centreAffaireService;

    public CentreAffaireController(CentreAffaireService centreAffaireService) {
        this.centreAffaireService = centreAffaireService;
    }

    @PostMapping
    public CentreAffaire createCentreAffaire(@Valid @RequestBody CentreAffaire centreAffaire) {
        return centreAffaireService.createCentreAffaire(centreAffaire);
    }

    @GetMapping
    public List<CentreAffaire> getAllCentreAffaires() {
        return centreAffaireService.getAllCentreAffaires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentreAffaire> getCentreAffaireById(@PathVariable UUID id) {
        return centreAffaireService.getCentreAffaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public CentreAffaire updateCentreAffaire(@PathVariable UUID id, @Valid @RequestBody CentreAffaire centreAffaire) {
        return centreAffaireService.updateCentreAffaire(id, centreAffaire);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCentreAffaire(@PathVariable UUID id) {
        centreAffaireService.deleteCentreAffaire(id);
        return ResponseEntity.noContent().build();
    }
}
