package com.yunus.research.controller;

import com.yunus.research.dto.SiteSettingsDto;
import com.yunus.research.service.SiteSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SiteSettingsService siteSettingsService;

    @GetMapping
    public ResponseEntity<SiteSettingsDto> getSettings() {
        return ResponseEntity.ok(siteSettingsService.getSettings());
    }

    @PutMapping
    public ResponseEntity<SiteSettingsDto> updateSettings(@RequestBody SiteSettingsDto dto) {
        return ResponseEntity.ok(siteSettingsService.updateSettings(dto));
    }
}
