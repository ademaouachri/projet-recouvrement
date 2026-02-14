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
}
