package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList; // زيادة الـ Import هذا أحسن
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PROFILS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profil {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Le libellé ne peut pas être nul")
    private String libelle;

    @Column(name = "CODE_PROFIL")
    @NotBlank(message = "Le code profil est obligatoire")
    private String codeProfil;

    @NotNull(message = "L'activité est obligatoire")
    private Integer activite;

    @NotNull(message = "L'agence est obligatoire")
    private Integer agence;

    @NotNull(message = "La zone est obligatoire")
    private Integer zone;

    @NotNull(message = "La région est obligatoire")
    private Integer region;

    @NotBlank(message = "La structure est obligatoire")
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