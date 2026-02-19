package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.*;

import com.example.backend.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ProfilRepository profilRepository;
    private final OtpService otpService;
    private final PalierRepository palierRepository;
    private final ZoneRepository zoneRepository;
    private final RegionRepository regionRepository;
    private final AgenceRepository agenceRepository;
    private final ActiviteRepository activiteRepository;
    private final CentreAffaireRepository centreAffaireRepository;
    private final MarcheRepository marcheRepository;
    private final SegmentRepository segmentRepository;
    private final MdpService mdpService;
    private final GenerateurMotDePasseService generateurMotDePasseService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, OtpService otpService,
            ProfilRepository profilRepository, PalierRepository palierRepository, ZoneRepository zoneRepository,
            RegionRepository regionRepository, ActiviteRepository activiteRepository,
            SegmentRepository segmentRepository, CentreAffaireRepository centreAffaireRepository,
            MarcheRepository marcheRepository, AgenceRepository agenceRepository, MdpService mdpService,
            GenerateurMotDePasseService generateurMotDePasseService) {

        this.utilisateurRepository = utilisateurRepository;
        this.otpService = otpService;
        this.palierRepository = palierRepository;
        this.profilRepository = profilRepository;
        this.zoneRepository = zoneRepository;
        this.regionRepository = regionRepository;
        this.activiteRepository = activiteRepository;
        this.centreAffaireRepository = centreAffaireRepository;
        this.marcheRepository = marcheRepository;
        this.agenceRepository = agenceRepository;
        this.segmentRepository = segmentRepository;
        this.mdpService = mdpService;
        this.generateurMotDePasseService = generateurMotDePasseService;
    }

    public void registerUser(Utilisateur user) {
        if (user.getMatricule() == null || user.getMatricule().trim().isEmpty()) {
            throw new IllegalArgumentException("Le matricule est obligatoire !");
        }
        if (utilisateurRepository.existsByMatricule(user.getMatricule().trim())) {
            throw new IllegalArgumentException("Le matricule est déjà utilisé !");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire !");
        }
        if (utilisateurRepository.existsByEmail(user.getEmail().trim())) {
            throw new IllegalArgumentException("L'email est déjà utilisé !");
        }

        if (user.getProfil() != null) {
            if (user.getProfil().getId() != null) {
                Profil existingProfil = profilRepository.findById(user.getProfil().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Profil non trouvé avec l'id : " + user.getProfil().getId()));
                user.setProfil(existingProfil);
            }

        }

        if (user.getProfil() != null && user.getProfil().getPalier() == 1) {
            if (user.getPaliers() != null && !user.getPaliers().isEmpty()) {
                List<Palier> attachedPaliers = new ArrayList<>();
                for (Palier p : user.getPaliers()) {
                    Palier existingPalier = palierRepository.findById(p.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Palier non trouvé avec l'id : " + p.getId()));
                    attachedPaliers.add(existingPalier);
                }
                user.setPaliers(attachedPaliers);
            } else {
                throw new IllegalArgumentException("Le palier est obligatoire");

            }
        }

        if (user.getProfil() != null && user.getProfil().getZone() == 1) {
            if (user.getZones() != null && !user.getZones().isEmpty()) {
                List<Zone> attachedZones = new ArrayList<>();
                for (Zone z : user.getZones()) {
                    Zone existingzone = zoneRepository.findById(z.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Zone non trouvé avec l'id:" + z.getId()));
                    attachedZones.add(existingzone);

                }
                user.setZones(attachedZones);
            } else {
                throw new IllegalArgumentException("La zone est obligatoire");

            }

        }

        if (user.getProfil() != null && user.getProfil().getRegion() == 1) {
            if (user.getRegions() != null && !user.getRegions().isEmpty()) {
                List<Region> attachedRegions = new ArrayList<>();
                for (Region i : user.getRegions()) {
                    Region existingRegion = regionRepository.findById(i.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Region non trouvé avec l'id:" + i.getId()));
                    attachedRegions.add(existingRegion);

                }
                user.setRegions(attachedRegions);
            } else {
                throw new IllegalArgumentException("La région est obligatoire");
            }
        }
        if (user.getProfil() != null && user.getProfil().getAgence() == 1) {
            if (user.getAgences() != null && !user.getAgences().isEmpty()) {
                List<Agence> attachedAgence = new ArrayList<>();
                for (Agence i : user.getAgences()) {
                    Agence existingAgence = agenceRepository.findById(i.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Agence non trouvé avec l'id:" + i.getId()));
                    attachedAgence.add(existingAgence);

                }
                user.setAgences(attachedAgence);
            } else {
                throw new IllegalArgumentException("L'agence est obligatoire");

            }

        }
        if (user.getProfil() != null && user.getProfil().getActivite() == 1) {
            if (user.getActivites() != null && !user.getActivites().isEmpty()) {
                List<Activite> attachedActivite = new ArrayList<>();
                for (Activite i : user.getActivites()) {
                    Activite existingActivite = activiteRepository.findById(i.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Activite non trouvé avec l'id:" + i.getId()));
                    attachedActivite.add(existingActivite);

                }
                user.setActivites(attachedActivite);
            } else {
                throw new IllegalArgumentException("L'activité est obligatoire");

            }
        }

        if (user.getProfil() != null && user.getProfil().getCentreAffaire() == 1) {
            if (user.getCentreAffaires() != null && !user.getCentreAffaires().isEmpty()) {
                List<CentreAffaire> attachedCentreAffaires = new ArrayList<>();
                for (CentreAffaire i : user.getCentreAffaires()) {
                    CentreAffaire existingCentreAffaires = centreAffaireRepository.findById(i.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "CentreAffaires non trouvé avec l'id:" + i.getId()));
                    attachedCentreAffaires.add(existingCentreAffaires);

                }
                user.setCentreAffaires(attachedCentreAffaires);
            } else {

                throw new IllegalArgumentException("Le centre d'affaire est obligatoire");

            }

        }
        if (user.getProfil() != null && user.getProfil().getMarche() == 1) {
            if (user.getMarches() != null && !user.getMarches().isEmpty()) {
                List<Marche> attachedMarches = new ArrayList<>();
                for (Marche i : user.getMarches()) {
                    Marche existingMarches = marcheRepository.findById(i.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Marche non trouvé avec l'id:" + i.getId()));
                    attachedMarches.add(existingMarches);

                }
                user.setMarches(attachedMarches);
            } else {
                throw new IllegalArgumentException("Le marché est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getSegment() == 1) {
            if (user.getSegments() != null && !user.getSegments().isEmpty()) {
                List<Segment> attachedSegments = new ArrayList<>();
                for (Segment i : user.getSegments()) {
                    Segment existingSegments = segmentRepository.findById(i.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Segment non trouvé avec l'id:" + i.getId()));
                    attachedSegments.add(existingSegments);

                }
                user.setSegments(attachedSegments);
            } else {
                throw new IllegalArgumentException("Le segment est obligatoire");
            }
        }

        try {
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setEnabled(false);
            user.setMotDePasse(null); // Explicitly null for registration until verified
            Utilisateur savedUser = utilisateurRepository.save(user);
            otpService.sendOtp(savedUser.getEmail(), otp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
        }
    }

    public String verifyUserOtp(String email, String otpInput) {
        if (email == null || otpInput == null)
            return "Email ou OTP manquant";

        try {
            Utilisateur user = utilisateurRepository.findByEmail(email.trim());
            if (user == null)
                return "Utilisateur introuvable avec l'email : " + email;

            if (!otpService.verifyOtp(otpInput.trim(), user.getOtp()))
                return "Code OTP incorrect pour " + email;

            String mdp = GenerateurMotDePasseService.generer();
            user.setEnabled(true);
            user.setOtp(null);
            user.setMotDePasse(mdp);
            utilisateurRepository.save(user);

            mdpService.sendMdp(user.getEmail(), mdp);
            return "OK";
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
            return "Plusieurs utilisateurs trouvés avec cet email. Veuillez contacter l'administrateur.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la vérification : " + e.getMessage();
        }
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public java.util.Optional<Utilisateur> getUtilisateurById(UUID id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur updateUtilisateur(UUID id, Utilisateur userDetails) {
        return utilisateurRepository.findById(id).map(user -> {
            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setEmail(userDetails.getEmail());
            user.setProfil(userDetails.getProfil());
            user.setUtilisateurActive(userDetails.getUtilisateurActive());
            Profil profil = profilRepository.findById(userDetails.getProfil().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil not found"));

            user.setProfil(profil);

            if (profil.getPalier() == 1) {
                List<Palier> attachedPaliers = new ArrayList<>();
                if (userDetails.getPaliers() != null && !userDetails.getPaliers().isEmpty()) {
                    for (Palier p : userDetails.getPaliers()) {
                        Palier existingPalier = palierRepository.findById(p.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Palier non trouvé avec l'id : " + p.getId()));
                        attachedPaliers.add(existingPalier);
                    }
                    user.setPaliers(attachedPaliers);
                } else {
                    throw new IllegalArgumentException("Le palier est obligatoire");
                }

            } else {
                user.getPaliers().clear();
            }

            if (profil.getActivite() == 1) {
                List<Activite> attached = new ArrayList<>();
                if (userDetails.getActivites() != null) {
                    for (Activite i : userDetails.getActivites()) {
                        attached.add(activiteRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Activite not found")));
                    }
                    user.setActivites(attached);
                } else {
                    throw new IllegalArgumentException("l'activité est obligatoire");
                }

            } else {
                user.getActivites().clear();
            }

            if (profil.getAgence() == 1) {
                List<Agence> attached = new ArrayList<>();
                if (userDetails.getAgences() != null) {
                    for (Agence i : userDetails.getAgences()) {
                        attached.add(agenceRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Agence not found")));
                    }
                    user.setAgences(attached);
                } else {
                    throw new IllegalArgumentException("L'agence est obligatoire");
                }

            } else {
                user.getAgences().clear();
            }

            if (profil.getCentreAffaire() == 1) {
                List<CentreAffaire> attached = new ArrayList<>();
                if (userDetails.getCentreAffaires() != null) {
                    for (CentreAffaire i : userDetails.getCentreAffaires()) {
                        attached.add(centreAffaireRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("CentreAffaire not found")));
                    }
                    user.setCentreAffaires(attached);
                } else {

                    throw new IllegalArgumentException("Le centre d'affaire est obligatoire");

                }

            } else {
                user.getCentreAffaires().clear();
            }

            if (profil.getMarche() == 1) {
                List<Marche> attached = new ArrayList<>();
                if (userDetails.getMarches() != null) {
                    for (Marche i : userDetails.getMarches()) {
                        attached.add(marcheRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Marche not found")));
                    }
                    user.setMarches(attached);
                } else {
                    throw new IllegalArgumentException("Le marché est obligatoire");
                }

            } else {
                user.getMarches().clear();
            }

            if (profil.getSegment() == 1) {
                List<Segment> attached = new ArrayList<>();
                if (userDetails.getSegments() != null) {
                    for (Segment i : userDetails.getSegments()) {
                        attached.add(segmentRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Segment not found")));
                    }
                    user.setSegments(attached);
                } else {
                    throw new IllegalArgumentException("Le segment est obligatoire");
                }

            } else {
                user.getSegments().clear();
            }

            if (profil.getZone() == 1) {
                List<Zone> attached = new ArrayList<>();
                if (userDetails.getZones() != null) {
                    for (Zone i : userDetails.getZones()) {
                        attached.add(zoneRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Zone not found")));
                    }
                    user.setZones(attached);
                } else {
                    throw new IllegalArgumentException("La zone est obligatoire");

                }

            } else {
                user.getZones().clear();
            }

            if (profil.getRegion() == 1) {
                List<Region> attached = new ArrayList<>();
                if (userDetails.getRegions() != null) {
                    for (Region i : userDetails.getRegions()) {
                        attached.add(regionRepository.findById(i.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Region not found")));
                    }
                    user.setRegions(attached);
                } else {
                    throw new IllegalArgumentException("La région est obligatoire");
                }

            } else {
                user.getRegions().clear();
            }

            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
    }

    public String activateOrDeactivateUtilisateur(UUID id, Boolean active) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur non trouvé avec l'id: " + id));

        user.setUtilisateurActive(active);
        utilisateurRepository.save(user);

        if (active) {
            return "Utilisateur activé";
        } else {
            return "Utilisateur désactivé";
        }
    }

}
