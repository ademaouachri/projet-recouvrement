package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "AGENCES")
@Getter
@Setter
public class Agence extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "zone_id")
    @JsonIgnoreProperties("regions")
    private Zone zone;
}
