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

    public Optional<AuthResponse> loginAdmin(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            // Use BCrypt for password comparison
            if (passwordService.verifyPassword(password, admin.getPassword())) {
                UserDto userDto = new UserDto(
                        admin.getId(),
                        admin.getUsername(),
                        admin.getEmail(),
                        "admin");
                // TODO: Generate JWT token
                String token = "jwt-token-" + System.currentTimeMillis();
                return Optional.of(new AuthResponse(userDto, token));
            }
        }

        return Optional.empty();
    }

    public Optional<AuthResponse> loginMember(String username, String password) {
        Optional<Member> memberOpt = memberRepository.findByUsername(username);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            // Use BCrypt for password comparison
            if (passwordService.verifyPassword(password, member.getPassword())) {
                UserDto userDto = new UserDto(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail(),
                        "member");
                // TODO: Generate JWT token
                String token = "jwt-token-" + System.currentTimeMillis();
                return Optional.of(new AuthResponse(userDto, token));
            }
        }

        return Optional.empty();
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
