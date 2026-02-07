package com.example.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "CENTRES_AFFAIRE")
@Getter
@Setter
public class CentreAffaire extends BaseEntity {
}
