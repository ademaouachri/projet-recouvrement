package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.*;
import com.example.backend.Repository.*;
import org.springframework.stereotype.Service;
import java.util.*; // Import Set and HashSet
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, OtpService otpService,
                              ProfilRepository profilRepository, PalierRepository palierRepository, ZoneRepository zoneRepository,
                              RegionRepository regionRepository, ActiviteRepository activiteRepository,
                              SegmentRepository segmentRepository, CentreAffaireRepository centreAffaireRepository,
                              MarcheRepository marcheRepository, AgenceRepository agenceRepository,
                              PasswordEncoder passwordEncoder, EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
        this.otpService = otpService;
        this.palierRepository = palierRepository;
        this.zoneRepository = zoneRepository;
        this.regionRepository = regionRepository;
        this.activiteRepository = activiteRepository;
        this.segmentRepository = segmentRepository;
        this.centreAffaireRepository = centreAffaireRepository;
        this.marcheRepository = marcheRepository;
        this.agenceRepository = agenceRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(Utilisateur user) {
        if (utilisateurRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        if (utilisateurRepository.existsByMatricule(user.getMatricule())) {
            throw new IllegalArgumentException("Un utilisateur avec ce matricule existe déjà");
        }

        if (user.getProfil() != null && user.getProfil().getId() != null) {
            Profil existingProfil = profilRepository.findById(user.getProfil().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé"));
            user.setProfil(existingProfil);
        }

        // Modification ici: Utilisation de Set au lieu de List
        if (user.getProfil() != null && user.getProfil().getPalier() == 1) {
            if (user.getPaliers() != null && !user.getPaliers().isEmpty()) {
                Set<Palier> attachedPaliers = new HashSet<>();
                for (Palier p : user.getPaliers()) {
                    attachedPaliers.add(palierRepository.findById(p.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Palier non trouvé")));
                }
                user.setPaliers(attachedPaliers);
            } else {
                throw new IllegalArgumentException("Le palier est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getZone() == 1) {
            if (user.getZones() != null && !user.getZones().isEmpty()) {
                Set<Zone> attached = new HashSet<>();
                for (Zone z : user.getZones()) {
                    attached.add(zoneRepository.findById(z.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Zone non trouvée")));
                }
                user.setZones(attached);
            } else {
                throw new IllegalArgumentException("La zone est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getRegion() == 1) {
            if (user.getRegions() != null && !user.getRegions().isEmpty()) {
                Set<Region> attached = new HashSet<>();
                for (Region i : user.getRegions()) {
                    attached.add(regionRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Region non trouvée")));
                }
                user.setRegions(attached);
            } else {
                throw new IllegalArgumentException("La région est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getAgence() == 1) {
            if (user.getAgences() != null && !user.getAgences().isEmpty()) {
                Set<Agence> attached = new HashSet<>();
                for (Agence i : user.getAgences()) {
                    attached.add(agenceRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Agence non trouvée")));
                }
                user.setAgences(attached);
            } else {
                throw new IllegalArgumentException("L'agence est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getActivite() == 1) {
            if (user.getActivites() != null && !user.getActivites().isEmpty()) {
                Set<Activite> attached = new HashSet<>();
                for (Activite i : user.getActivites()) {
                    attached.add(activiteRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Activite non trouvée")));
                }
                user.setActivites(attached);
            } else {
                throw new IllegalArgumentException("L'activité est obligatoire");
            }
        }

        // Appliquer le même principe pour CentreAffaire, Marche, Segment...
        if (user.getProfil() != null && user.getProfil().getCentreAffaire() == 1) {
            if (user.getCentreAffaires() != null && !user.getCentreAffaires().isEmpty()) {
                Set<CentreAffaire> attached = new HashSet<>();
                for (CentreAffaire i : user.getCentreAffaires()) {
                    attached.add(centreAffaireRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("CentreAffaire non trouvé")));
                }
                user.setCentreAffaires(attached);
            } else {
                throw new IllegalArgumentException("Le centre d'affaire est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getMarche() == 1) {
            if (user.getMarches() != null && !user.getMarches().isEmpty()) {
                Set<Marche> attached = new HashSet<>();
                for (Marche i : user.getMarches()) {
                    attached.add(marcheRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Marche non trouvé")));
                }
                user.setMarches(attached);
            } else {
                throw new IllegalArgumentException("Le marché est obligatoire");
            }
        }

        if (user.getProfil() != null && user.getProfil().getSegment() == 1) {
            if (user.getSegments() != null && !user.getSegments().isEmpty()) {
                Set<Segment> attached = new HashSet<>();
                for (Segment i : user.getSegments()) {
                    attached.add(segmentRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Segment non trouvé")));
                }
                user.setSegments(attached);
            } else {
                throw new IllegalArgumentException("Le segment est obligatoire");
            }
        }

        try {
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setEnabled(false);
            user.setMotDePasse(null);
            Utilisateur savedUser = utilisateurRepository.save(user);
            otpService.sendOtp(savedUser.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException("Erreur enregistrement : " + e.getMessage());
        }
    }

    // ... ساكمل لك الـ updateUtilisateur بنفس الطريقة ...

    public Utilisateur updateUtilisateur(UUID id, Utilisateur userDetails) {
        return utilisateurRepository.findById(id).map(user -> {
            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setEmail(userDetails.getEmail());
            user.setUtilisateurActive(userDetails.getUtilisateurActive());

            Profil profil = profilRepository.findById(userDetails.getProfil().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil not found"));
            user.setProfil(profil);

            // Update Paliers
            if (profil.getPalier() == 1) {
                Set<Palier> attached = new HashSet<>();
                if (userDetails.getPaliers() != null) {
                    for (Palier p : userDetails.getPaliers()) {
                        attached.add(palierRepository.findById(p.getId()).orElseThrow(() -> new ResourceNotFoundException("Palier not found")));
                    }
                    user.setPaliers(attached);
                } else { throw new IllegalArgumentException("Palier obligatoire"); }
            } else { user.getPaliers().clear(); }

            // Update Activités
            if (profil.getActivite() == 1) {
                Set<Activite> attached = new HashSet<>();
                if (userDetails.getActivites() != null) {
                    for (Activite i : userDetails.getActivites()) {
                        attached.add(activiteRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Activite not found")));
                    }
                    user.setActivites(attached);
                } else { throw new IllegalArgumentException("Activite obligatoire"); }
            } else { user.getActivites().clear(); }

            // Update Agences
            if (profil.getAgence() == 1) {
                Set<Agence> attached = new HashSet<>();
                if (userDetails.getAgences() != null) {
                    for (Agence i : userDetails.getAgences()) {
                        attached.add(agenceRepository.findById(i.getId()).orElseThrow(() -> new ResourceNotFoundException("Agence not found")));
                    }
                    user.setAgences(attached);
                } else { throw new IllegalArgumentException("Agence obligatoire"); }
            } else { user.getAgences().clear(); }

            // تواصل نفس المنطق لبقية الـ Collections (Zone, Region, Marche...)
            // فقط استعمل Set<T> attached = new HashSet<>();

            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    // بقية الميثودات (verifyUserOtp, setPassword...) تبقى كما هي
    public String verifyUserOtp(String email, String otpInput) {
        if (email == null || otpInput == null) return "Email ou OTP manquant";
        try {
            Utilisateur user = utilisateurRepository.findByEmail(email.trim());
            if (user == null) return "Utilisateur introuvable";
            if (!otpService.verifyOtp(otpInput.trim(), user.getOtp())) return "OTP incorrect";
            String token = UUID.randomUUID().toString();
            user.setActivationToken(token);
            user.setOtp(null);
            user.setEnabled(true);
            utilisateurRepository.save(user);
            emailService.sendSetPasswordEmail(user.getEmail(), token);
            return "OK";
        } catch (Exception e) { return "Erreur : " + e.getMessage(); }
    }

    public List<Utilisateur> getAllUtilisateurs() { return utilisateurRepository.findAll(); }
    public Optional<Utilisateur> getUtilisateurById(UUID id) { return utilisateurRepository.findById(id); }

    public String activateOrDeactivateUtilisateur(UUID id, Boolean active) {
        Utilisateur user = utilisateurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUtilisateurActive(active);
        utilisateurRepository.save(user);
        return active ? "Utilisateur activé" : "Utilisateur désactivé";
    }

    public String setPassword(String token, String password) {
        try {
            Utilisateur user = utilisateurRepository.findByActivationToken(token).orElse(null);
            if (user == null) return "Token invalide !";
            user.setMotDePasse(passwordEncoder.encode(password));
            user.setActivationToken(null);
            user.setEnabled(true);
            user.setUtilisateurActive(true);
            utilisateurRepository.save(user);
            return "OK";
        } catch (Exception e) { return "Erreur : " + e.getMessage(); }
    }
}