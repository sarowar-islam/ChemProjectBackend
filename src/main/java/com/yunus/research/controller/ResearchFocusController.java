package com.yunus.research.controller;

import com.yunus.research.dto.ResearchFocusDto;
import com.yunus.research.service.ResearchFocusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/research-focus")
@RequiredArgsConstructor
public class ResearchFocusController {

    private final ResearchFocusService researchFocusService;

    @GetMapping
    public ResponseEntity<List<ResearchFocusDto>> getAllFocus() {
        return ResponseEntity.ok(researchFocusService.getAllFocus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchFocusDto> getFocusById(@PathVariable String id) {
        return researchFocusService.getFocusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResearchFocusDto> createFocus(@RequestBody ResearchFocusDto dto) {
        ResearchFocusDto created = researchFocusService.createFocus(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResearchFocusDto> updateFocus(@PathVariable String id, @RequestBody ResearchFocusDto dto) {
        return researchFocusService.updateFocus(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFocus(@PathVariable String id) {
        if (researchFocusService.deleteFocus(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
