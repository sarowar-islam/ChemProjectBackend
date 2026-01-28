package com.yunus.research.controller;

import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.MemberDto;
import com.yunus.research.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberDto> createMemberWithPassword(@RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }
}
