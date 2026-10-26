package com.yunus.research.controller;

import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.MemberDto;
import com.yunus.research.dto.UpdateMemberSettingsRequest;
import com.yunus.research.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable String id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<MemberDto> getMemberByUsername(@PathVariable String username) {
        return memberService.getMemberByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lookup")
    public ResponseEntity<MemberDto> lookupMember(@RequestParam String identifier) {
        return memberService.findByUsernameOrEmail(identifier)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable String id, @RequestBody MemberDto dto) {
        return memberService.updateMember(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/username/{username}/profile")
    public ResponseEntity<MemberDto> updateMemberProfile(@PathVariable String username, @RequestBody MemberDto dto) {
        return memberService.updateMemberProfile(username, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/username/{username}/settings")
    public ResponseEntity<?> updateMemberSettings(@PathVariable String username,
            @RequestBody UpdateMemberSettingsRequest request) {
        try {
            return memberService.updateMemberSettings(username, request)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable String id) {
        if (memberService.deleteMember(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/og/{username}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getMemberOpenGraphHtml(@PathVariable String username) {
        var memberOpt = memberService.getMemberByUsername(username);
        if (memberOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var member = memberOpt.get();
        String name = member.getName() != null ? member.getName() : "Team Member";
        String title = member.getTitle() != null ? member.getTitle() : "Researcher";
        String bio = member.getBio() != null ? member.getBio().replaceAll("<[^>]*>", "").trim() : "Research profile at BCER Lab.";
        if (bio.length() > 160) {
            bio = bio.substring(0, 157) + "...";
        }
        String photoUrl = member.getPhotoUrl() != null && !member.getPhotoUrl().isBlank()
                ? member.getPhotoUrl()
                : "https://bcerlab.com/photos/blank_profile.png";
        boolean isAlumni = "alumni".equalsIgnoreCase(member.getPosition());
        String profilePath = isAlumni ? "/alumni/" + username : "/team/" + username;
        String profileUrl = "https://bcerlab.com" + profilePath;

        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\" />\n" +
                "  <title>" + name + " - " + title + " | BCER Lab</title>\n" +
                "  <meta property=\"og:title\" content=\"" + name + " - " + title + " | BCER Lab\" />\n" +
                "  <meta property=\"og:description\" content=\"" + escapeHtml(bio) + "\" />\n" +
                "  <meta property=\"og:image\" content=\"" + photoUrl + "\" />\n" +
                "  <meta property=\"og:url\" content=\"" + profileUrl + "\" />\n" +
                "  <meta property=\"og:type\" content=\"profile\" />\n" +
                "  <meta name=\"twitter:card\" content=\"summary_large_image\" />\n" +
                "  <meta name=\"twitter:title\" content=\"" + name + " - " + title + " | BCER Lab\" />\n" +
                "  <meta name=\"twitter:description\" content=\"" + escapeHtml(bio) + "\" />\n" +
                "  <meta name=\"twitter:image\" content=\"" + photoUrl + "\" />\n" +
                "  <meta http-equiv=\"refresh\" content=\"0;url=" + profileUrl + "\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "  <script>window.location.href = \"" + profileUrl + "\";</script>\n" +
                "</body>\n" +
                "</html>";

        return ResponseEntity.ok(html);
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
