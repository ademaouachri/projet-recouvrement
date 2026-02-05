package com.example.backend.Controller;

import com.example.backend.Model.Profil;
import com.example.backend.Service.ProfilService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profils")
public class ProfilController {

    private final ProfilService profilService;

    public ProfilController(ProfilService profilService) {
        this.profilService = profilService;
    }

    @PostMapping
    public Profil createProfil(@Valid @RequestBody Profil profil) {
        return profilService.createProfil(profil);
    }

    @GetMapping
    public List<Profil> getAllProfils() {
        return profilService.getAllProfils();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profil> getProfilById(@PathVariable UUID id) {
        return profilService.getProfilById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Profil updateProfil(@PathVariable UUID id, @Valid @RequestBody Profil profil) {
        return profilService.updateProfil(id, profil);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable UUID id) {
        profilService.deleteProfil(id);
        return ResponseEntity.noContent().build();
    }
}
