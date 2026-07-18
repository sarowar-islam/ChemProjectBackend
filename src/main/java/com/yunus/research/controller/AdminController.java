package com.yunus.research.controller;

import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.MemberDto;
import com.yunus.research.service.MemberPasswordResetService;
import com.yunus.research.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final MemberPasswordResetService memberPasswordResetService;

    @PostMapping("/members")
    public ResponseEntity<MemberDto> createMemberWithPassword(@RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @PostMapping("/members/{id}/generate-security-code")
    public ResponseEntity<Map<String, String>> generateSecurityCodeForMember(@PathVariable String id) {
        try {
            String securityCode = memberPasswordResetService.generateSecurityCodeForMember(id);
            return ResponseEntity.ok(Map.of(
                    "securityCode", securityCode,
                    "message", "Security code generated successfully. It is valid for 30 minutes."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
