package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Marche;
import com.example.backend.Model.Segment;
import com.example.backend.Model.Zone;
import com.example.backend.Repository.MarcheRepository;
import com.example.backend.Repository.SegmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final MarcheRepository marcheRepository;

    public SegmentService(SegmentRepository segmentRepository, MarcheRepository marcheRepository) {
        this.marcheRepository = marcheRepository;
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(Segment segment) {

        if (segmentRepository.existsByCode(segment.getCode())) {
            throw new IllegalArgumentException("Un segment avec ce code existe déjà : " + segment.getCode());
        }
        if (segmentRepository.existsByLabel(segment.getLabel())) {
            throw new IllegalArgumentException("Un segment avec ce label existe déjà : " + segment.getLabel());
        }

        if (segment.getMarche() == null || segment.getMarche() .getId() == null) {
            throw new ResourceNotFoundException("Le marché doit être fourni avec un ID valide");
        }
        else {
            UUID marcheId =  segment.getMarche() .getId();
            Marche marche = marcheRepository.findById(marcheId)
                .orElseThrow(() -> new ResourceNotFoundException("Marché introuvable avec l'ID: " + marcheId));
             segment.setMarche(marche);
             return segmentRepository.save(segment);
    }}

    public List<Segment> getAllSegments() {
        return segmentRepository.findAll();
    }

    public Optional<Segment> getSegmentById(UUID id) {
        return segmentRepository.findById(id);
    }

    public Segment updateSegment(UUID id, Segment segmentDetails) {
        return segmentRepository.findById(id).map(segment -> {
            segment.setLabel(segmentDetails.getLabel());
            UUID marcheId =  segmentDetails.getMarche() .getId();
            Marche marche = marcheRepository.findById(marcheId)
                    .orElseThrow(() -> new ResourceNotFoundException("Marché introuvable avec l'ID " + marcheId));
            segmentDetails.setMarche(marche);
            segment.setMarche(segmentDetails.getMarche());
            return segmentRepository.save(segment);
        }).orElseThrow(() -> new ResourceNotFoundException("Segment non trouvé avec l'id : " + id));
    }

    public void deleteSegment(UUID id) {

        if (!segmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Segment introuvable avec l'ID : " + id);
        }
        segmentRepository.deleteById(id);
    }
}
