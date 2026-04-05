package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImportResult {

    private boolean success;
    private int totalRows;
    private int successCount;
    private int updateCount;
    private List<String> errors;

    public ImportResult() {
        this.errors = new ArrayList<>();
        this.success = true;
    }
}
