package com.yunus.research.controller;

import com.yunus.research.entity.Admin;
import com.yunus.research.entity.Member;
import com.yunus.research.repository.AdminRepository;
import com.yunus.research.repository.MemberRepository;
import com.yunus.research.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * One-time migration controller to hash existing plain-text passwords.
 * This should be removed after migration is complete.
 */
@RestController
@RequestMapping("/api/migrate")
@RequiredArgsConstructor
@Slf4j
public class MigrationController {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final PasswordService passwordService;

    /**
     * Migrate all plain-text passwords to BCrypt hashes.
     * This endpoint should be called once and then disabled/removed.
     * 
     * WARNING: Only run this once! Running it on already-hashed passwords will
     * break login.
     */
    @PostMapping("/hash-passwords")
    public ResponseEntity<Map<String, Object>> hashAllPasswords(@RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Add ?confirm=true to actually run the migration. WARNING: Only run this once!"));
        }

        int adminCount = 0;
        int memberCount = 0;

        // Hash admin passwords
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            String currentPassword = admin.getPassword();
            // Check if password is already hashed (BCrypt hashes start with $2a$, $2b$, or
            // $2y$)
            if (currentPassword != null && !currentPassword.startsWith("$2")) {
                admin.setPassword(passwordService.hashPassword(currentPassword));
                adminRepository.save(admin);
                adminCount++;
                log.info("Hashed password for admin: {}", admin.getEmail());
            }
        }

        // Hash member passwords
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            String currentPassword = member.getPassword();
            // Check if password is already hashed
            if (currentPassword != null && !currentPassword.startsWith("$2")) {
                member.setPassword(passwordService.hashPassword(currentPassword));
                memberRepository.save(member);
                memberCount++;
                log.info("Hashed password for member: {}", member.getUsername());
            }
        }

        log.info("Password migration completed. Hashed {} admin(s) and {} member(s)", adminCount, memberCount);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password migration completed",
                "adminsUpdated", adminCount,
                "membersUpdated", memberCount));
    }
}
