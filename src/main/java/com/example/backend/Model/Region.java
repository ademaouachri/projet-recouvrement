package com.example.backend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "REGION")
@Getter
@Setter
@Builder
public class Region extends BaseEntity {

}
