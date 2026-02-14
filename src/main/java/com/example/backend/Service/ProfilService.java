package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Profil;
import com.example.backend.Repository.ProfilRepository;
import com.example.backend.Repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfilService {

    private final ProfilRepository profilRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ProfilService(ProfilRepository profilRepository , UtilisateurRepository utilisateurRepository) {
        this.profilRepository = profilRepository;
        this.utilisateurRepository = utilisateurRepository;
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
        }).orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'id : " + id));
    }

    public void deleteProfil(UUID id) {

        Profil profil = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'id : " + id));



        if(utilisateurRepository.existsByProfilId(id)){
            throw new ResourceNotFoundException("Profil utilisé par des utilisateurs");
        }
        profilRepository.deleteById(id);
    }
}
