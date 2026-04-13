package com.example.backend.Controller;

import com.example.backend.Model.Client;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.RE_commercialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/re-commercial")
public class RE_commercialController {

    private final RE_commercialService reCommercialService;

    public RE_commercialController(RE_commercialService reCommercialService) {
        this.reCommercialService = reCommercialService;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getMyClients(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(defaultValue = "createdAt") String sortBy,   // ← جديد
            @RequestParam(defaultValue = "desc") String sortDir) {     // ← جديد

        if (utilisateur == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(
                reCommercialService.getClientsForUser(utilisateur, sortBy, sortDir)
        );
    }
}