package com.example.backend.DTO;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String token;
    private String password;
}
