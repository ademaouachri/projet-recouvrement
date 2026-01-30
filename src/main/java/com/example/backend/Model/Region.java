package com.example.backend.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "REGION", schema = "REF_RECTEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Le code est obligatoire")
    private String code;

    @NotNull(message = "Le label est obligatoire")
    private String label;


}
