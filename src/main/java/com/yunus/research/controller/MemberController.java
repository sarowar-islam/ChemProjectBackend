package com.yunus.research.controller;

import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.MemberDto;
import com.yunus.research.dto.UpdateMemberSettingsRequest;
import com.yunus.research.service.MemberService;
import lombok.RequiredArgsConstructor;
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
}
