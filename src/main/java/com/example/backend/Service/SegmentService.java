package com.example.backend.Service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.Model.Segment;
import com.example.backend.Repository.SegmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;

    public SegmentService(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public Segment createSegment(Segment segment) {
        return segmentRepository.save(segment);
    }

    public List<Segment> getAllSegments() {
        return segmentRepository.findAll();
    }

    public Optional<Segment> getSegmentById(UUID id) {
        return segmentRepository.findById(id);
    }

    public Segment updateSegment(UUID id, Segment segmentDetails) {
        return segmentRepository.findById(id).map(segment -> {
            segment.setCode(segmentDetails.getCode());
            segment.setLabel(segmentDetails.getLabel());
            return segmentRepository.save(segment);
        }).orElseThrow(() -> new ResourceNotFoundException("Segment non trouvé avec l'id : " + id));
    }

    public void deleteSegment(UUID id) {
        segmentRepository.deleteById(id);
    }
}
