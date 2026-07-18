package com.yunus.research.service;

import com.yunus.research.dto.PasswordResetConfirmRequest;
import com.yunus.research.entity.Member;
import com.yunus.research.entity.MemberPasswordResetCode;
import com.yunus.research.repository.MemberPasswordResetCodeRepository;
import com.yunus.research.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberPasswordResetService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int RESET_EXPIRY_MINUTES = 15;
    private static final int SECURITY_CODE_EXPIRY_MINUTES = 30;

    private final MemberRepository memberRepository;
    private final MemberPasswordResetCodeRepository resetCodeRepository;
    private final PasswordService passwordService;
    private final GmailEmailService gmailEmailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void requestReset(String email) {
        String normalizedEmail = normalizeEmail(email);
        Optional<Member> memberOpt = memberRepository.findByEmailIgnoreCase(normalizedEmail);
        if (memberOpt.isEmpty()) {
            gmailEmailService.sendNoAccountExistsNotice(normalizedEmail);
            return;
        }

        Member member = memberOpt.get();
        resetCodeRepository.deleteByEmailIgnoreCase(normalizedEmail);

        String code = generateCode();
        MemberPasswordResetCode resetCode = new MemberPasswordResetCode();
        resetCode.setMemberId(member.getId());
        resetCode.setEmail(member.getEmail());
        resetCode.setCodeHash(passwordService.hashPassword(code));
        resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(RESET_EXPIRY_MINUTES));
        resetCode.setAttempts(0);
        resetCodeRepository.save(resetCode);

        gmailEmailService.sendPasswordResetCode(member.getEmail(), member.getName(), code);
    }

    @Transactional
    public String generateSecurityCodeForMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return generateSecurityCodeForEmail(member.getEmail());
    }

    @Transactional
    public String generateSecurityCodeForEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        Member member = memberRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        resetCodeRepository.deleteByEmailIgnoreCase(normalizedEmail);

        String code = generateCode();
        MemberPasswordResetCode resetCode = new MemberPasswordResetCode();
        resetCode.setMemberId(member.getId());
        resetCode.setEmail(member.getEmail());
        resetCode.setCodeHash(passwordService.hashPassword(code));
        resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(SECURITY_CODE_EXPIRY_MINUTES));
        resetCode.setAttempts(0);
        resetCodeRepository.save(resetCode);

        return code;
    }

    @Transactional
    public void confirmReset(PasswordResetConfirmRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        String code = trimToEmpty(request.getCode());
        String newPassword = trimToEmpty(request.getNewPassword());

        if (code.isEmpty()) {
            throw new IllegalArgumentException("Reset code is required");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        Member member = memberRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset code"));

        MemberPasswordResetCode resetCode = resetCodeRepository
                .findTopByEmailIgnoreCaseAndUsedAtIsNullOrderByCreatedAtDesc(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset code"));

        if (resetCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetCodeRepository.delete(resetCode);
            throw new IllegalArgumentException("Invalid or expired reset code");
        }

        if (!passwordService.verifyPassword(code, resetCode.getCodeHash())) {
            int attempts = Optional.ofNullable(resetCode.getAttempts()).orElse(0) + 1;
            resetCode.setAttempts(attempts);
            if (attempts >= MAX_ATTEMPTS) {
                resetCodeRepository.delete(resetCode);
                throw new IllegalArgumentException("Too many invalid attempts. Please request a new reset code.");
            }
            resetCodeRepository.save(resetCode);
            throw new IllegalArgumentException("Invalid or expired reset code");
        }

        resetCode.setUsedAt(LocalDateTime.now());
        resetCodeRepository.save(resetCode);
        resetCodeRepository.deleteByEmailIgnoreCase(normalizedEmail);

        member.setPassword(passwordService.hashPassword(newPassword));
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void verifyResetCode(String email, String code) {
        String normalizedEmail = normalizeEmail(email);
        String trimmedCode = trimToEmpty(code);

        if (trimmedCode.isEmpty()) {
            throw new IllegalArgumentException("Reset code is required");
        }

        MemberPasswordResetCode resetCode = resetCodeRepository
                .findTopByEmailIgnoreCaseAndUsedAtIsNullOrderByCreatedAtDesc(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset code"));

        if (resetCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired reset code");
        }

        if (!passwordService.verifyPassword(trimmedCode, resetCode.getCodeHash())) {
            throw new IllegalArgumentException("Invalid or expired reset code");
        }
    }

    private String generateCode() {
        return String.format(Locale.ROOT, "%06d", secureRandom.nextInt(1_000_000));
    }

    private String normalizeEmail(String email) {
        String trimmed = trimToEmpty(email);
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}