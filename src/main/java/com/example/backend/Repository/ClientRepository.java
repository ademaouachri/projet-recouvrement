package com.example.backend.Repository;

import com.example.backend.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByCin(String cin);
    @Query("""
        SELECT c FROM Client c
        WHERE (:agencyCodes IS NULL OR c.agencyCode IN :agencyCodes)
        AND (:zoneCodes IS NULL OR c.zoneCode IN :zoneCodes)
        AND (:regionCodes IS NULL OR c.regionCode IN :regionCodes)
        AND (:activityCodes IS NULL OR c.activityCode IN :activityCodes)
        AND (:marcheCodes IS NULL OR c.marchetCode IN :marcheCodes)
        AND (:segmentCodes IS NULL OR c.segmentCode IN :segmentCodes)
        AND (:businessCenterCodes IS NULL OR c.businessCenterCode IN :businessCenterCodes)
    """)
    List<Client> findByPerimetre(
            @Param("agencyCodes") List<String> agencyCodes,
            @Param("zoneCodes") List<String> zoneCodes,
            @Param("regionCodes") List<String> regionCodes,
            @Param("activityCodes") List<String> activityCodes,
            @Param("marcheCodes") List<String> marcheCodes,
            @Param("segmentCodes") List<String> segmentCodes,
            @Param("businessCenterCodes") List<String> businessCenterCodes
    );
}
