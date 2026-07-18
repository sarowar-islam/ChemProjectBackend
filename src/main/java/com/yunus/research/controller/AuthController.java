package com.yunus.research.controller;

import com.yunus.research.dto.AuthResponse;
import com.yunus.research.dto.PasswordResetConfirmRequest;
import com.yunus.research.dto.PasswordResetRequest;
import com.yunus.research.dto.LoginRequest;
import com.yunus.research.service.AuthService;
import com.yunus.research.service.MemberPasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberPasswordResetService memberPasswordResetService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        return authService.loginAdmin(request.getEmail(), request.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(new AuthResponse(null, null)));
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> loginMember(@RequestBody LoginRequest request) {
        return authService.loginMember(request.getUsername(), request.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(new AuthResponse(null, null)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT is stateless, just return success
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/member/forgot-password/request")
    public ResponseEntity<?> requestMemberPasswordReset(@RequestBody PasswordResetRequest request) {
        try {
            memberPasswordResetService.requestReset(request.getEmail());
            return ResponseEntity.ok(Map.of(
                    "message",
                    "An email has been sent to the given mail address. Please check the spam box if mail is not found in inbox."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/member/forgot-password/verify")
    public ResponseEntity<?> verifyMemberPasswordResetCode(@RequestBody PasswordResetConfirmRequest request) {
        try {
            memberPasswordResetService.verifyResetCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok(Map.of("message", "Code verified"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/member/forgot-password/confirm")
    public ResponseEntity<?> confirmMemberPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
        try {
            memberPasswordResetService.confirmReset(request);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
        String token = authHeader.substring(7);
        boolean valid = authService.validateToken(token).isPresent();
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String token = authHeader.substring(7);
        return authService.getCurrentUser(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).body(null));
    }
}
