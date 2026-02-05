package com.example.backend.Service;

import com.example.backend.Model.Profil;
import com.example.backend.Repository.ProfilRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfilService {

    private final ProfilRepository profilRepository;

    public ProfilService(ProfilRepository profilRepository) {
        this.profilRepository = profilRepository;
    }

    public Profil createProfil(Profil profil) {
        return profilRepository.save(profil);
    }

    public java.util.List<Profil> getAllProfils() {
        return profilRepository.findAll();
    }

    public java.util.Optional<Profil> getProfilById(UUID id) {
        return profilRepository.findById(id);
    }

    public Profil updateProfil(UUID id, Profil profilDetails) {
        return profilRepository.findById(id).map(profil -> {
            profil.setLibelle(profilDetails.getLibelle());
            profil.setCodeProfil(profilDetails.getCodeProfil());
            profil.setActivite(profilDetails.getActivite());
            profil.setAgence(profilDetails.getAgence());
            profil.setZone(profilDetails.getZone());
            profil.setRegion(profilDetails.getRegion());
            profil.setStructure(profilDetails.getStructure());
            profil.setPalier(profilDetails.getPalier());
            profil.setSegment(profilDetails.getSegment());
            profil.setMarche(profilDetails.getMarche());
            profil.setCentreAffaire(profilDetails.getCentreAffaire());
            return profilRepository.save(profil);
        }).orElseThrow(() -> new RuntimeException("Profil non trouvé avec l'id : " + id));
    }

    public void deleteProfil(UUID id) {
        profilRepository.deleteById(id);
    }
}
