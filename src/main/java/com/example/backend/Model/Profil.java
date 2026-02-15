package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "La structure est obligatoire")
    private String structure;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer activite = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer agence = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer zone = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer region = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer palier = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer segment = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    private Integer marche = 0;


    @Min(value = 0, message = "La valeur minimale est 0")
    @Max(value = 1, message = "La valeur maximale est 1")
    @Column(name = "CENTRE_AFFAIRE")
    private Integer centreAffaire = 0;





}