package com.example.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "AGENCES")
@Getter
@Setter
public class Agence extends BaseEntity {
}
