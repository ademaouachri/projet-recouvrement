package com.example.backend.Controller;

import com.example.backend.Model.Activite;
import com.example.backend.Service.ActiviteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activites")
public class ActiviteController {

    private final ActiviteService activiteService;

    public ActiviteController(ActiviteService activiteService) {
        this.activiteService = activiteService;
    }

    @PostMapping
    public Activite createActivite(@Valid @RequestBody Activite activite) {
        return activiteService.createActivite(activite);
    }

    @GetMapping
    public List<Activite> getAllActivites() {
        return activiteService.getAllActivites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activite> getActiviteById(@PathVariable UUID id) {
        return activiteService.getActiviteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Activite updateActivite(@PathVariable UUID id, @Valid @RequestBody Activite activite) {
        return activiteService.updateActivite(id, activite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivite(@PathVariable UUID id) {
        activiteService.deleteActivite(id);
        return ResponseEntity.noContent().build();
    }
}
