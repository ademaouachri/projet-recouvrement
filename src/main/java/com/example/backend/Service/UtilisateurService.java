package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.*;
import com.example.backend.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
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
                              ProfilRepository profilRepository, PalierRepository palierRepository,
                              ZoneRepository zoneRepository, RegionRepository regionRepository,
                              ActiviteRepository activiteRepository, SegmentRepository segmentRepository,
                              CentreAffaireRepository centreAffaireRepository, MarcheRepository marcheRepository,
                              AgenceRepository agenceRepository, PasswordEncoder passwordEncoder,
                              EmailService emailService) {
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

    @Transactional
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
            attachRelatedEntities(user, user, existingProfil);
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

    @Transactional
    public Utilisateur updateUtilisateur(UUID id, Utilisateur userDetails) {
        return utilisateurRepository.findById(id).map(user -> {
            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setEmail(userDetails.getEmail());
            user.setMatricule(userDetails.getMatricule());
            user.setUtilisateurActive(userDetails.getUtilisateurActive());

            Profil profil = profilRepository.findById(userDetails.getProfil().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil not found"));
            user.setProfil(profil);

            // استدعاء ميثود الربط لتحديث القوائم
            attachRelatedEntities(user, userDetails, profil);

            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    /**
     * ميثود مساعدة لربط الكيانات (Regions, Segments, etc.) بناءً على صلاحيات البروفايل
     */
    private void attachRelatedEntities(Utilisateur targetUser, Utilisateur sourceData, Profil profil) {

        // Paliers
        if (profil.getPalier() == 1) {
            if (sourceData.getPaliers() != null && !sourceData.getPaliers().isEmpty()) {
                Set<Palier> attached = new HashSet<>();
                for (Palier p : sourceData.getPaliers()) {
                    attached.add(palierRepository.findById(p.getId()).orElseThrow(() -> new ResourceNotFoundException("Palier not found")));
                }
                targetUser.setPaliers(attached);
            } else { throw new IllegalArgumentException("Le palier est obligatoire"); }
        } else { targetUser.getPaliers().clear(); }

        // Zones
        if (profil.getZone() == 1) {
            if (sourceData.getZones() != null && !sourceData.getZones().isEmpty()) {
                Set<Zone> attached = new HashSet<>();
                for (Zone z : sourceData.getZones()) {
                    attached.add(zoneRepository.findById(z.getId()).orElseThrow(() -> new ResourceNotFoundException("Zone not found")));
                }
                targetUser.setZones(attached);
            } else { throw new IllegalArgumentException("La zone est obligatoire"); }
        } else { targetUser.getZones().clear(); }

        // Regions
        if (profil.getRegion() == 1) {
            if (sourceData.getRegions() != null && !sourceData.getRegions().isEmpty()) {
                Set<Region> attached = new HashSet<>();
                for (Region r : sourceData.getRegions()) {
                    attached.add(regionRepository.findById(r.getId()).orElseThrow(() -> new ResourceNotFoundException("Region not found")));
                }
                targetUser.setRegions(attached);
            } else { throw new IllegalArgumentException("La région est obligatoire"); }
        } else { targetUser.getRegions().clear(); }

        // Agences
        if (profil.getAgence() == 1) {
            if (sourceData.getAgences() != null && !sourceData.getAgences().isEmpty()) {
                Set<Agence> attached = new HashSet<>();
                for (Agence a : sourceData.getAgences()) {
                    attached.add(agenceRepository.findById(a.getId()).orElseThrow(() -> new ResourceNotFoundException("Agence not found")));
                }
                targetUser.setAgences(attached);
            } else { throw new IllegalArgumentException("L'agence est obligatoire"); }
        } else { targetUser.getAgences().clear(); }

        // Activités
        if (profil.getActivite() == 1) {
            if (sourceData.getActivites() != null && !sourceData.getActivites().isEmpty()) {
                Set<Activite> attached = new HashSet<>();
                for (Activite act : sourceData.getActivites()) {
                    attached.add(activiteRepository.findById(act.getId()).orElseThrow(() -> new ResourceNotFoundException("Activite not found")));
                }
                targetUser.setActivites(attached);
            } else { throw new IllegalArgumentException("L'activité est obligatoire"); }
        } else { targetUser.getActivites().clear(); }

        // Centre Affaires
        if (profil.getCentreAffaire() == 1) {
            if (sourceData.getCentreAffaires() != null && !sourceData.getCentreAffaires().isEmpty()) {
                Set<CentreAffaire> attached = new HashSet<>();
                for (CentreAffaire c : sourceData.getCentreAffaires()) {
                    attached.add(centreAffaireRepository.findById(c.getId()).orElseThrow(() -> new ResourceNotFoundException("Centre d'affaire not found")));
                }
                targetUser.setCentreAffaires(attached);
            } else { throw new IllegalArgumentException("Le centre d'affaire est obligatoire"); }
        } else { targetUser.getCentreAffaires().clear(); }

        // Marches
        if (profil.getMarche() == 1) {
            if (sourceData.getMarches() != null && !sourceData.getMarches().isEmpty()) {
                Set<Marche> attached = new HashSet<>();
                for (Marche m : sourceData.getMarches()) {
                    attached.add(marcheRepository.findById(m.getId()).orElseThrow(() -> new ResourceNotFoundException("Marche not found")));
                }
                targetUser.setMarches(attached);
            } else { throw new IllegalArgumentException("Le marché est obligatoire"); }
        } else { targetUser.getMarches().clear(); }

        // Segments
        if (profil.getSegment() == 1) {
            if (sourceData.getSegments() != null && !sourceData.getSegments().isEmpty()) {
                Set<Segment> attached = new HashSet<>();
                for (Segment s : sourceData.getSegments()) {
                    attached.add(segmentRepository.findById(s.getId()).orElseThrow(() -> new ResourceNotFoundException("Segment not found")));
                }
                targetUser.setSegments(attached);
            } else { throw new IllegalArgumentException("Le segment est obligatoire"); }
        } else { targetUser.getSegments().clear(); }
    }

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