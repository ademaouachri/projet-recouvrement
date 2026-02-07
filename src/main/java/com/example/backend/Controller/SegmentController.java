package com.example.backend.Controller;

import com.example.backend.Model.Segment;
import com.example.backend.Service.SegmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/segments")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @PostMapping
    public Segment createSegment(@Valid @RequestBody Segment segment) {
        return segmentService.createSegment(segment);
    }

    @GetMapping
    public List<Segment> getAllSegments() {
        return segmentService.getAllSegments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Segment> getSegmentById(@PathVariable UUID id) {
        return segmentService.getSegmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Segment updateSegment(@PathVariable UUID id, @Valid @RequestBody Segment segment) {
        return segmentService.updateSegment(id, segment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable UUID id) {
        segmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }
}
