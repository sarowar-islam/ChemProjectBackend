package com.yunus.research.service;

import com.yunus.research.dto.SiteSettingsDto;
import com.yunus.research.entity.SiteSettings;
import com.yunus.research.repository.SiteSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteSettingsService {

    private final SiteSettingsRepository siteSettingsRepository;

    public SiteSettingsDto getSettings() {
        Optional<SiteSettings> settings = siteSettingsRepository.findById("default");

        if (settings.isPresent()) {
            return toDto(settings.get());
        }

        // Return default settings if not found
        return new SiteSettingsDto(
                "default",
                "https://scholar.google.com/citations?user=yMZlErUAAAAJ",
                "Welcome to Prof. Dr. Yunus Ahmed Research Group at Chittagong University of Engineering & Technology (CUET).",
                null);
    }

    public SiteSettingsDto updateSettings(SiteSettingsDto dto) {
        SiteSettings settings = siteSettingsRepository.findById("default")
                .orElse(new SiteSettings());

        settings.setId("default");
        if (dto.getGoogleScholarUrl() != null) {
            settings.setGoogleScholarUrl(dto.getGoogleScholarUrl());
        }
        if (dto.getAboutUs() != null) {
            settings.setAboutUs(dto.getAboutUs());
        }

        SiteSettings saved = siteSettingsRepository.save(settings);
        return toDto(saved);
    }

    private SiteSettingsDto toDto(SiteSettings settings) {
        return new SiteSettingsDto(
                settings.getId(),
                settings.getGoogleScholarUrl(),
                settings.getAboutUs(),
                settings.getUpdatedAt() != null ? settings.getUpdatedAt().toString() : null);
    }
}
