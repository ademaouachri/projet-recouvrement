package com.example.backend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "PROFILS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Le libellé ne peut pas être nul")
    private String libelle;

    @Column(name = "CODE_PROFIL")
    @NotNull(message = "Le code profil est obligatoire")
    private String codeProfil;

    @NotNull(message = "L'activité est obligatoire")
    private Integer activite;

    @NotNull(message = "L'agence est obligatoire")
    private Integer agence;

    @NotNull(message = "La zone est obligatoire")
    private Integer zone;

    @NotNull(message = "La région est obligatoire")
    private Integer region;

    @NotNull(message = "La structure est obligatoire")
    private String structure;

    @NotNull(message = "Le palier est obligatoire")
    private Integer palier;

    @NotNull(message = "Le segment est obligatoire")
    private Integer segment;

    @NotNull(message = "Le marché est obligatoire")
    private Integer marche;

    @NotNull(message = "Le centre d'affaire est obligatoire")
    @Column(name = "CENTRE_AFFAIRE")
    private Integer centreAffaire;
}
