package com.yunus.research.controller;

import com.yunus.research.dto.ScholarProfileDto;
import com.yunus.research.dto.ScholarPublicationDto;
import com.yunus.research.entity.Member;
import com.yunus.research.entity.SiteSettings;
import com.yunus.research.repository.MemberRepository;
import com.yunus.research.repository.SiteSettingsRepository;
import com.yunus.research.service.GoogleScholarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scholar")
@RequiredArgsConstructor
@Slf4j
public class ScholarController {

    private final GoogleScholarService googleScholarService;
    private final SiteSettingsRepository siteSettingsRepository;
    private final MemberRepository memberRepository;

    /**
     * Get publications from the main site's Google Scholar profile
     */
    @GetMapping("/publications")
    public ResponseEntity<Map<String, Object>> getMainPublications() {
        Optional<SiteSettings> settingsOpt = siteSettingsRepository.findById("default");

        if (settingsOpt.isEmpty() || settingsOpt.get().getGoogleScholarUrl() == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Google Scholar URL not configured"));
        }

        String scholarUrl = settingsOpt.get().getGoogleScholarUrl();
        ScholarProfileDto profile = googleScholarService.scrapeProfile(scholarUrl);

        if (profile == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Failed to fetch publications from Google Scholar"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("profile", Map.of(
                "name", profile.getName() != null ? profile.getName() : "",
                "affiliation", profile.getAffiliation() != null ? profile.getAffiliation() : "",
                "imageUrl", profile.getImageUrl() != null ? profile.getImageUrl() : "",
                "interests", profile.getInterests() != null ? profile.getInterests() : "",
                "totalCitations", profile.getTotalCitations(),
                "hIndex", profile.getHIndex(),
                "i10Index", profile.getI10Index()));
        response.put("publications", profile.getPublications() != null ? profile.getPublications() : new ArrayList<>());

        return ResponseEntity.ok(response);
    }

    /**
     * Get publications from a specific Google Scholar URL
     */
    @GetMapping("/publications/url")
    public ResponseEntity<Map<String, Object>> getPublicationsByUrl(@RequestParam String scholarUrl) {
        if (scholarUrl == null || scholarUrl.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Google Scholar URL is required"));
        }

        ScholarProfileDto profile = googleScholarService.scrapeProfile(scholarUrl);

        if (profile == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Failed to fetch publications from Google Scholar"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("profile", Map.of(
                "name", profile.getName() != null ? profile.getName() : "",
                "affiliation", profile.getAffiliation() != null ? profile.getAffiliation() : "",
                "imageUrl", profile.getImageUrl() != null ? profile.getImageUrl() : "",
                "interests", profile.getInterests() != null ? profile.getInterests() : "",
                "totalCitations", profile.getTotalCitations(),
                "hIndex", profile.getHIndex(),
                "i10Index", profile.getI10Index()));
        response.put("publications", profile.getPublications() != null ? profile.getPublications() : new ArrayList<>());

        return ResponseEntity.ok(response);
    }

    /**
     * Get publications for a specific team member by their ID
     */
    @GetMapping("/member/{memberId}/publications")
    public ResponseEntity<Map<String, Object>> getMemberPublications(@PathVariable String memberId) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (memberOpt.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Member not found"));
        }

        Member member = memberOpt.get();

        if (member.getGoogleScholarLink() == null || member.getGoogleScholarLink().isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "member", Map.of(
                            "id", member.getId(),
                            "name", member.getName()),
                    "publications", new ArrayList<>(),
                    "message", "No Google Scholar profile linked"));
        }

        ScholarProfileDto profile = googleScholarService.scrapeProfile(member.getGoogleScholarLink());

        if (profile == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Failed to fetch publications from Google Scholar"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("member", Map.of(
                "id", member.getId(),
                "name", member.getName(),
                "googleScholarLink", member.getGoogleScholarLink()));
        response.put("profile", Map.of(
                "name", profile.getName() != null ? profile.getName() : "",
                "affiliation", profile.getAffiliation() != null ? profile.getAffiliation() : "",
                "totalCitations", profile.getTotalCitations(),
                "hIndex", profile.getHIndex(),
                "i10Index", profile.getI10Index()));
        response.put("publications", profile.getPublications() != null ? profile.getPublications() : new ArrayList<>());

        return ResponseEntity.ok(response);
    }

    /**
     * Get profile info (stats) from Google Scholar
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfileStats() {
        Optional<SiteSettings> settingsOpt = siteSettingsRepository.findById("default");

        if (settingsOpt.isEmpty() || settingsOpt.get().getGoogleScholarUrl() == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Google Scholar URL not configured"));
        }

        String scholarUrl = settingsOpt.get().getGoogleScholarUrl();
        ScholarProfileDto profile = googleScholarService.scrapeProfile(scholarUrl);

        if (profile == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Failed to fetch profile from Google Scholar"));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "profile", Map.of(
                        "name", profile.getName() != null ? profile.getName() : "",
                        "affiliation", profile.getAffiliation() != null ? profile.getAffiliation() : "",
                        "imageUrl", profile.getImageUrl() != null ? profile.getImageUrl() : "",
                        "interests", profile.getInterests() != null ? profile.getInterests() : "",
                        "totalCitations", profile.getTotalCitations(),
                        "hIndex", profile.getHIndex(),
                        "i10Index", profile.getI10Index())));
    }
}
