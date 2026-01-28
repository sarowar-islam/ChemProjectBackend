package com.yunus.research.controller;

import com.yunus.research.dto.PublicationDto;
import com.yunus.research.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping
    public ResponseEntity<List<PublicationDto>> getAllPublications() {
        return ResponseEntity.ok(publicationService.getAllPublications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDto> getPublicationById(@PathVariable String id) {
        return publicationService.getPublicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PublicationDto>> getPublicationsByMember(@PathVariable String memberId) {
        return ResponseEntity.ok(publicationService.getPublicationsByMember(memberId));
    }

    @PostMapping
    public ResponseEntity<PublicationDto> createPublication(@RequestBody PublicationDto dto) {
        return ResponseEntity.ok(publicationService.createPublication(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationDto> updatePublication(@PathVariable String id, @RequestBody PublicationDto dto) {
        return publicationService.updatePublication(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable String id) {
        if (publicationService.deletePublication(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
