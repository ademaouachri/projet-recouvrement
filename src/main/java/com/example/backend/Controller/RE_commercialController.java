package com.example.backend.Controller;

import com.example.backend.Model.Client;
import com.example.backend.Model.Utilisateur;
import com.example.backend.Service.RE_commercialService;
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
            @AuthenticationPrincipal Utilisateur utilisateur) {

        List<Client> clients = reCommercialService.getClientsForUser(utilisateur);
        return ResponseEntity.ok(clients);
    }
}