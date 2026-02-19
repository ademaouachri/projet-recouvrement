package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SEGMENTS")
@Getter
@Setter

public class Segment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "march_id")
    @JsonIgnoreProperties("segments")
    private  Marche marche;
}
