package com.example.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ZONE")
@Getter
@Setter
public class Zone extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties("zone")
    private Region region;



}
