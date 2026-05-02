package com.example.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    @Id
    private String keyParam;
    private String valueParam;
    private String codeParametre; // مثلاً "phase commerciale"
    private String typeParametre; // "SDB" أو "IMP"
    private Integer jourDebut;
    private Integer jourFin;

}