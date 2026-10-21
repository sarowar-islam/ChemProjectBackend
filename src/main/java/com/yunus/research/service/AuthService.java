package com.yunus.research.service;

import com.yunus.research.dto.AuthResponse;
import com.yunus.research.dto.UserDto;
import com.yunus.research.entity.Admin;
import com.yunus.research.entity.Member;
import com.yunus.research.repository.AdminRepository;
import com.yunus.research.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final PasswordService passwordService;

    public AuthResponse loginAdmin(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String trimmedEmail = email.trim();
        Optional<Admin> adminOpt = adminRepository.findByEmail(trimmedEmail);
        if (adminOpt.isEmpty()) {
            adminOpt = adminRepository.findByUsername(trimmedEmail);
        }

        if (adminOpt.isEmpty()) {
            throw new IllegalArgumentException("Email not registered");
        }

        Admin admin = adminOpt.get();
        if (!passwordService.verifyPassword(password, admin.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        UserDto userDto = new UserDto(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail(),
                "admin");
        String token = "jwt-token-" + System.currentTimeMillis();
        return new AuthResponse(userDto, token);
    }

    public AuthResponse loginMember(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String trimmed = username.trim();
        Optional<Member> memberOpt = memberRepository.findByUsernameIgnoreCase(trimmed);
        if (memberOpt.isEmpty()) {
            memberOpt = memberRepository.findByEmailIgnoreCase(trimmed);
        }

        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Username not registered");
        }

        Member member = memberOpt.get();
        if (member.getPassword() == null || member.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("No password has been set for this account. Please use 'Forgot Password' to set one.");
        }

        if (!passwordService.verifyPassword(password, member.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        UserDto userDto = new UserDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                "member");
        String token = "jwt-token-" + System.currentTimeMillis();
        return new AuthResponse(userDto, token);
    }

    public Optional<UserDto> validateToken(String token) {
        // TODO: Implement JWT validation
        // For now, just return null (invalid)
        return Optional.empty();
    }

    public Optional<UserDto> getCurrentUser(String token) {
        // TODO: Extract user from JWT token
        return Optional.empty();
    }
}
