package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_import_errors")
public class ImportErrorLog {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "FIELD_NAME")
    private String fieldName;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "LINE_NUMBER")
    private int rowNumber;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();


    public ImportErrorLog(String fieldName, String description, int rowNumber) {
        this.fieldName = fieldName;
        this.description = description;
        this.rowNumber = rowNumber;
    }
}
