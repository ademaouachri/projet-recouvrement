package com.example.backend.Controller;

import com.example.backend.Model.Client;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.RE_commercialService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/re-commercial")
public class RE_commercialController {

    private final RE_commercialService reCommercialService;

    public RE_commercialController(RE_commercialService reCommercialService) {
        this.reCommercialService = reCommercialService;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getClients(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String dossierType,
            @RequestParam(required = false) String structure,   // 1. استلام الـ structure
            @RequestParam(required = false) String fullName,    // 2. استلام الـ fullName
            @RequestParam(required = false) String clientGroup,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "totalImpayeAmount") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        // 3. نبعثوهم للـ Service بنفس الترتيب الجديد
        List<Client> clients = reCommercialService.getClientsForUser(
                utilisateur,
                postalCode,
                dossierType,
                structure,   // الترتيب مهم!
                fullName,    // الترتيب مهم!
                clientGroup,
                createdBy,
                startDate,
                sortBy,
                sortDir
        );

        return ResponseEntity.ok(clients);
    }
}