package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet; // تبديل ArrayList بـ HashSet
import java.util.Set;       // تبديل List بـ Set
import java.util.UUID;

@Entity
@Table(name = "UTILISATEURS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(nullable = true)
    private String motDePasse;

    private String otp;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "ENABLED")
    private Boolean enabled = false;

    @Column(name = "utilisateur_active")
    private Boolean utilisateurActive = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "profil_id")
    @JsonIgnoreProperties("utilisateurs")
    private Profil profil;

    // --- العلاقات المتعددة (استعمال Set لمنع الـ MultipleBagFetchException) ---

    @ManyToMany
    @JoinTable(name = "utilisateur_palier",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "palier_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Palier> paliers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_zone",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Zone> zones = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_region",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "region_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Region> regions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_segment",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Segment> segments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_marche",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "marche_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Marche> marches = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_centreAffaire",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "centreAffaire_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<CentreAffaire> centreAffaires = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_activite",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "activite_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Activite> activites = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_agence",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "agence_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Agence> agences = new HashSet<>();
}