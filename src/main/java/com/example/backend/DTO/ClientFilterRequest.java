package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ClientFilterRequest {
    private List<String> classe;
    private List<String> dossierType;
    private List<String> traite;
    private List<String> contactFlag;
    private List<String> clientGroup;
    private List<String> activityCode;
    private List<String> regionCode;
    private List<String> marcheCode;
    private List<String> segmentCode;
    private List<String> zoneCode;
    private List<String> agencyCode;
    private List<String> postalCode;
}