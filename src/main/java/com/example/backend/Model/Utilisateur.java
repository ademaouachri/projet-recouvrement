package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
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

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
    private String otp;

    @Column(name = "ENABLED")
    private Boolean enabled = false;


    @Column(name = "utilisateur_active")
    private Boolean utilisateurActive = false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profil_id")
    @JsonIgnoreProperties("utilisateurs")
    private Profil profil;

    @ManyToMany
    @JoinTable(
            name = "utilisateur_palier",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "palier_id")
    )
    @JsonIgnoreProperties("utilisateurs")
    private List<Palier> paliers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "utilisateur_zone",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    @JsonIgnoreProperties("utilisateurs")
    private  List<Zone>zones = new ArrayList<>();



    @ManyToMany
    @JoinTable(name = "utilisateur_region",
    joinColumns = @JoinColumn(name = "utilisateur_id")
    ,inverseJoinColumns = @JoinColumn(name = "region_id"))
    private List<Region> regions = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "utilisateur_segment",
            joinColumns = @JoinColumn(name = "utilisateur_id")
            ,inverseJoinColumns = @JoinColumn(name = "segment_id"))
    private List<Segment> segments = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "utilisateur_marche",
            joinColumns = @JoinColumn(name = "utilisateur_id")
            ,inverseJoinColumns = @JoinColumn(name = "marche_id"))
    private List<Marche> marches = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "utilisateur_centreAffaire",
            joinColumns = @JoinColumn(name = "utilisateur_id")
            ,inverseJoinColumns = @JoinColumn(name = "centreAffaire_id"))
    private List<CentreAffaire> centreAffaires = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "utilisateur_activite",
            joinColumns = @JoinColumn(name = "utilisateur_id")
            ,inverseJoinColumns = @JoinColumn(name = "activite_id"))
    private List<Activite> activites = new ArrayList<>();



    @ManyToMany
    @JoinTable(name = "utilisateur_agence",
            joinColumns = @JoinColumn(name = "utilisateur_id")
            ,inverseJoinColumns = @JoinColumn(name = "agence_id"))
    private List<Agence> agences = new ArrayList<>();





}
