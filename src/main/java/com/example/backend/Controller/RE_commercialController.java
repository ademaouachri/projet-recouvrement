package com.example.backend.Controller;

import com.example.backend.DTO.DashboardStats;
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
@CrossOrigin(origins = "*") // مهم جداً للتعامل مع Angular
public class RE_commercialController {

    private final RE_commercialService reCommercialService;

    public RE_commercialController(RE_commercialService reCommercialService) {
        this.reCommercialService = reCommercialService;
    }

    /**
     * ميثود الـ Dashboard
     * URL: GET http://localhost:8080/re-commercial/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(required = false) String structure,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
    ) {
        DashboardStats stats = reCommercialService.getDashboardData(utilisateur, structure, startDate);
        return ResponseEntity.ok(stats);
    }

    /**
     * ميثود قائمة العملاء
     * URL: GET http://localhost:8080/re-commercial/clients
     */
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getClients(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String dossierType,
            @RequestParam(required = false) String structure,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String clientGroup,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "totalImpayeAmount") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        List<Client> clients = reCommercialService.getClientsForUser(
                utilisateur,
                postalCode,
                dossierType,
                structure,
                fullName,
                clientGroup,
                createdBy,
                startDate,
                sortBy,
                sortDir
        );

        return ResponseEntity.ok(clients);
    }
}