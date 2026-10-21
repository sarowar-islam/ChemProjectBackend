package com.yunus.research.controller;

import com.yunus.research.dto.ScholarProfileDto;
import com.yunus.research.dto.ScholarPublicationDto;
import com.yunus.research.entity.Member;
import com.yunus.research.entity.Publication;
import com.yunus.research.entity.SiteSettings;
import com.yunus.research.repository.MemberRepository;
import com.yunus.research.repository.PublicationRepository;
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
    private final PublicationRepository publicationRepository;

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
            List<Publication> dbPubs = publicationRepository.findAll();
            if (!dbPubs.isEmpty()) {
                log.info("Google Scholar scrape returned null, falling back to {} cached publications from database", dbPubs.size());
                List<ScholarPublicationDto> fallbackPubs = new ArrayList<>();
                int totalCites = 0;
                for (Publication p : dbPubs) {
                    ScholarPublicationDto dto = new ScholarPublicationDto();
                    dto.setTitle(p.getTitle());
                    dto.setAuthors(p.getAuthors());
                    dto.setYear(p.getYear() != null ? String.valueOf(p.getYear()) : "");
                    dto.setJournal(p.getJournal());
                    dto.setArticleUrl(p.getPdfLink());
                    dto.setCitedBy(p.getCitedBy() != null ? String.valueOf(p.getCitedBy()) : "0");
                    totalCites += (p.getCitedBy() != null ? p.getCitedBy() : 0);
                    fallbackPubs.add(dto);
                }
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("profile", Map.of(
                        "name", "Professor Dr. Yunus Ahmed",
                        "affiliation", "Professor & Head, Dept. of Chemistry, CUET",
                        "imageUrl", "",
                        "interests", "ARB and its ARG, AOPs, Environmental Remediation, Machine Learning, Electrochemical Sensor",
                        "totalCitations", totalCites > 0 ? totalCites : 2359,
                        "hIndex", 21,
                        "i10Index", 29));
                response.put("publications", fallbackPubs);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "error", "Failed to fetch publications from Google Scholar"));
        }

        List<ScholarPublicationDto> pubs = profile.getPublications() != null ? profile.getPublications() : new ArrayList<>();
        if (!pubs.isEmpty()) {
            syncPublications(pubs);
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
        response.put("publications", pubs);

        return ResponseEntity.ok(response);
    }

    private void syncPublications(List<ScholarPublicationDto> scholarPubs) {
        try {
            long currentCount = publicationRepository.count();
            if (currentCount < scholarPubs.size()) {
                publicationRepository.deleteAll();

                List<Publication> entities = new ArrayList<>();
                for (ScholarPublicationDto dto : scholarPubs) {
                    if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) continue;
                    Publication pub = new Publication();
                    pub.setTitle(dto.getTitle().trim());
                    pub.setAuthors(dto.getAuthors() != null ? dto.getAuthors() : new ArrayList<>());

                    int year = 2024;
                    try {
                        if (dto.getYear() != null && !dto.getYear().trim().isEmpty()) {
                            year = Integer.parseInt(dto.getYear().trim());
                        }
                    } catch (Exception ignored) {}
                    pub.setYear(year);

                    pub.setJournal(dto.getJournal() != null && !dto.getJournal().trim().isEmpty() ? dto.getJournal().trim() : "Journal Publication");
                    pub.setPdfLink(dto.getArticleUrl());

                    int citedBy = 0;
                    try {
                        if (dto.getCitedBy() != null && !dto.getCitedBy().trim().isEmpty()) {
                            citedBy = Integer.parseInt(dto.getCitedBy().trim());
                        }
                    } catch (Exception ignored) {}
                    pub.setCitedBy(citedBy);

                    entities.add(pub);
                }
                publicationRepository.saveAll(entities);
                log.info("Successfully synced {} publications into the database.", entities.size());
            }
        } catch (Exception e) {
            log.warn("Could not sync publications to database: {}", e.getMessage());
        }
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
