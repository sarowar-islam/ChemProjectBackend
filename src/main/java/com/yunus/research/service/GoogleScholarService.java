package com.yunus.research.service;

import com.yunus.research.dto.ScholarProfileDto;
import com.yunus.research.dto.ScholarPublicationDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class GoogleScholarService {

    private static final String SCHOLAR_BASE_URL = "https://scholar.google.com";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    /**
     * Extract user ID from Google Scholar URL
     */
    public String extractUserId(String scholarUrl) {
        if (scholarUrl == null || scholarUrl.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile("[?&]user=([^&]+)");
        Matcher matcher = pattern.matcher(scholarUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Scrape Google Scholar profile and publications
     */
    public ScholarProfileDto scrapeProfile(String scholarUrl) {
        String userId = extractUserId(scholarUrl);
        if (userId == null) {
            log.error("Could not extract user ID from URL: {}", scholarUrl);
            return null;
        }

        try {
            // Fetch the profile page
            String profileUrl = SCHOLAR_BASE_URL + "/citations?user=" + userId + "&hl=en&cstart=0&pagesize=100";
            log.info("Scraping Google Scholar profile: {}", profileUrl);

            Document doc = Jsoup.connect(profileUrl)
                    .userAgent(USER_AGENT)
                    .timeout(30000)
                    .get();

            ScholarProfileDto profile = new ScholarProfileDto();

            // Extract profile info
            Element nameElement = doc.selectFirst("#gsc_prf_in");
            if (nameElement != null) {
                profile.setName(nameElement.text());
            }

            Element affiliationElement = doc.selectFirst(".gsc_prf_il");
            if (affiliationElement != null) {
                profile.setAffiliation(affiliationElement.text());
            }

            Element imageElement = doc.selectFirst("#gsc_prf_pup-img");
            if (imageElement != null) {
                String imgSrc = imageElement.attr("src");
                if (imgSrc.startsWith("/")) {
                    imgSrc = SCHOLAR_BASE_URL + imgSrc;
                }
                profile.setImageUrl(imgSrc);
            }

            // Extract interests/research areas
            Elements interestElements = doc.select("#gsc_prf_int a");
            if (!interestElements.isEmpty()) {
                List<String> interests = new ArrayList<>();
                for (Element interest : interestElements) {
                    interests.add(interest.text());
                }
                profile.setInterests(String.join(", ", interests));
            }

            // Extract citation metrics
            Elements statsTable = doc.select("#gsc_rsb_st td.gsc_rsb_std");
            if (statsTable.size() >= 3) {
                try {
                    profile.setTotalCitations(parseIntSafe(statsTable.get(0).text()));
                    profile.setHIndex(parseIntSafe(statsTable.get(2).text()));
                    profile.setI10Index(parseIntSafe(statsTable.get(4).text()));
                } catch (Exception e) {
                    log.warn("Could not parse citation metrics: {}", e.getMessage());
                }
            }

            // Extract publications
            List<ScholarPublicationDto> publications = new ArrayList<>();
            Elements pubRows = doc.select("#gsc_a_b .gsc_a_tr");

            for (Element row : pubRows) {
                try {
                    ScholarPublicationDto pub = new ScholarPublicationDto();

                    // Title and link
                    Element titleElement = row.selectFirst(".gsc_a_at");
                    if (titleElement != null) {
                        pub.setTitle(titleElement.text());
                        String href = titleElement.attr("href");
                        if (href != null && !href.isEmpty()) {
                            pub.setArticleUrl(SCHOLAR_BASE_URL + href);
                            // Extract scholar ID from URL
                            Pattern cidPattern = Pattern.compile("citation_for_view=([^:]+):([^&]+)");
                            Matcher cidMatcher = cidPattern.matcher(href);
                            if (cidMatcher.find()) {
                                pub.setScholarId(cidMatcher.group(2));
                            }
                        }
                    }

                    // Authors and journal info
                    Elements grayTexts = row.select(".gs_gray");
                    if (grayTexts.size() >= 1) {
                        String authorsText = grayTexts.get(0).text();
                        pub.setAuthors(parseAuthors(authorsText));
                    }
                    if (grayTexts.size() >= 2) {
                        pub.setJournal(grayTexts.get(1).text());
                    }

                    // Citations
                    Element citedByElement = row.selectFirst(".gsc_a_ac");
                    if (citedByElement != null) {
                        pub.setCitedBy(citedByElement.text());
                    }

                    // Year
                    Element yearElement = row.selectFirst(".gsc_a_y .gsc_a_h");
                    if (yearElement != null) {
                        pub.setYear(yearElement.text());
                    }

                    if (pub.getTitle() != null && !pub.getTitle().isEmpty()) {
                        publications.add(pub);
                    }
                } catch (Exception e) {
                    log.warn("Error parsing publication row: {}", e.getMessage());
                }
            }

            profile.setPublications(publications);
            log.info("Successfully scraped {} publications for {}", publications.size(), profile.getName());

            return profile;

        } catch (IOException e) {
            log.error("Error scraping Google Scholar profile: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Scrape only publications from a Google Scholar profile
     */
    public List<ScholarPublicationDto> scrapePublications(String scholarUrl) {
        ScholarProfileDto profile = scrapeProfile(scholarUrl);
        if (profile != null && profile.getPublications() != null) {
            return profile.getPublications();
        }
        return new ArrayList<>();
    }

    /**
     * Parse authors string into a list
     */
    private List<String> parseAuthors(String authorsText) {
        if (authorsText == null || authorsText.isEmpty()) {
            return new ArrayList<>();
        }

        // Split by comma and trim
        String[] authors = authorsText.split(",");
        List<String> authorList = new ArrayList<>();
        for (String author : authors) {
            String trimmed = author.trim();
            if (!trimmed.isEmpty() && !trimmed.equals("...")) {
                authorList.add(trimmed);
            }
        }
        return authorList;
    }

    /**
     * Safely parse integer from string
     */
    private int parseIntSafe(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        try {
            // Remove any non-digit characters
            String cleaned = text.replaceAll("[^0-9]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
