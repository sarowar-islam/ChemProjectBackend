package com.yunus.research.controller;

import com.yunus.research.repository.MemberRepository;
import com.yunus.research.repository.NewsRepository;
import com.yunus.research.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SitemapController {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final NewsRepository newsRepository;

    @GetMapping(value = {"/sitemap.xml", "/api/sitemap.xml"}, produces = MediaType.APPLICATION_XML_VALUE)
    public String getSitemap() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Static pages
        String baseUrl = "https://bcerlab.com";
        addUrl(sb, baseUrl + "/", "daily", "1.0");
        addUrl(sb, baseUrl + "/projects", "weekly", "0.8");
        addUrl(sb, baseUrl + "/publications", "weekly", "0.9");
        addUrl(sb, baseUrl + "/team", "weekly", "0.9");
        addUrl(sb, baseUrl + "/alumni", "weekly", "0.9");
        addUrl(sb, baseUrl + "/news", "weekly", "0.7");
        addUrl(sb, baseUrl + "/notices", "weekly", "0.7");

        // Team members & Alumni
        memberRepository.findAll().forEach(member -> {
            String username = member.getUsername();
            if (username != null && !username.isBlank()) {
                boolean isAlumni = "alumni".equalsIgnoreCase(member.getPosition());
                String path = isAlumni ? "/alumni/" + username : "/team/" + username;
                addUrl(sb, baseUrl + path, "weekly", isAlumni ? "0.8" : "0.9");
            }
        });

        // Projects
        projectRepository.findAll().forEach(project -> {
            addUrl(sb, baseUrl + "/projects/" + project.getId(), "monthly", "0.7");
        });

        // News
        newsRepository.findAll().forEach(news -> {
            addUrl(sb, baseUrl + "/news/" + news.getId(), "monthly", "0.6");
        });

        sb.append("</urlset>");
        return sb.toString();
    }

    private void addUrl(StringBuilder sb, String loc, String changefreq, String priority) {
        sb.append("  <url>\n");
        sb.append("    <loc>").append(loc).append("</loc>\n");
        sb.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        sb.append("    <priority>").append(priority).append("</priority>\n");
        sb.append("  </url>\n");
    }
}
