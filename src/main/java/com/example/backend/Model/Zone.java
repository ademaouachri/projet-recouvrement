package com.example.backend.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ZONE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Le code est obligatoire")
    private String code;

    @NotNull(message = "Le label est obligatoire")
    private String label;
}
