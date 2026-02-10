package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "REGION")
@Getter
@Setter
public class Region extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "zone_id")
    @JsonIgnoreProperties("regions")
    private Zone zone;
}
