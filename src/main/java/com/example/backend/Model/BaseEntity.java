package com.example.backend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true, updatable = false, nullable = false)  // ⬅️ updatable = false مهم
    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @Column(unique = true)
    @NotBlank(message = "Le label est obligatoire")
    private String label;
}
