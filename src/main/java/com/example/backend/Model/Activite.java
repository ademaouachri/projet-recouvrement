package com.example.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "ACTIVITES")
@Getter
@Setter

public class Activite extends BaseEntity {
}
