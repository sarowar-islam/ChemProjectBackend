package com.yunus.research.service;

import com.yunus.research.dto.MemberDto;
import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.UpdateMemberSettingsRequest;
import com.yunus.research.entity.Member;
import com.yunus.research.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordService passwordService;

    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<MemberDto> getMemberById(String id) {
        return memberRepository.findById(id).map(this::toDto);
    }

    public Optional<MemberDto> getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).map(this::toDto);
    }

    public MemberDto createMember(CreateMemberRequest request) {
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPassword(passwordService.hashPassword(request.getPassword()));
        member.setPhone(request.getPhone());
        member.setPosition(request.getPosition());
        member.setTitle(request.getTitle());
        member.setResearchArea(request.getResearchArea());
        member.setBio(request.getBio());
        member.setPhotoUrl(request.getPhotoUrl());
        member.setGoogleScholarLink(request.getGoogleScholarLink());
        member.setExpertise(request.getExpertise());

        if (request.getJoinedDate() != null && !request.getJoinedDate().isEmpty()) {
            member.setJoinedDate(LocalDate.parse(request.getJoinedDate().substring(0, 10)));
        }

        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

    @Transactional
    public Optional<MemberDto> updateMember(String id, MemberDto dto) {
        return memberRepository.findById(id).map(member -> {
            if (dto.getName() != null)
                member.setName(dto.getName());
            if (dto.getEmail() != null)
                member.setEmail(dto.getEmail());
            if (dto.getPhone() != null)
                member.setPhone(dto.getPhone());
            if (dto.getPosition() != null)
                member.setPosition(dto.getPosition());
            if (dto.getTitle() != null)
                member.setTitle(dto.getTitle());
            if (dto.getResearchArea() != null)
                member.setResearchArea(dto.getResearchArea());
            if (dto.getBio() != null)
                member.setBio(dto.getBio());
            if (dto.getPhotoUrl() != null)
                member.setPhotoUrl(dto.getPhotoUrl());
            if (dto.getGoogleScholarLink() != null)
                member.setGoogleScholarLink(dto.getGoogleScholarLink());
            if (dto.getExpertise() != null) {
                // Clear and add new items to handle ElementCollection properly
                member.getExpertise().clear();
                member.getExpertise().addAll(dto.getExpertise());
            }

            return toDto(memberRepository.save(member));
        });
    }

    @Transactional
    public Optional<MemberDto> updateMemberProfile(String username, MemberDto dto) {
        return memberRepository.findByUsername(username).map(member -> {
            if (dto.getEmail() != null)
                member.setEmail(dto.getEmail());
            if (dto.getPhone() != null)
                member.setPhone(dto.getPhone());
            if (dto.getBio() != null)
                member.setBio(dto.getBio());
            if (dto.getPhotoUrl() != null)
                member.setPhotoUrl(dto.getPhotoUrl());
            if (dto.getGoogleScholarLink() != null)
                member.setGoogleScholarLink(dto.getGoogleScholarLink());
            if (dto.getExpertise() != null) {
                // Clear and add new items to handle ElementCollection properly
                member.getExpertise().clear();
                member.getExpertise().addAll(dto.getExpertise());
            }

            return toDto(memberRepository.save(member));
        });
    }

    @Transactional
    public Optional<MemberDto> updateMemberSettings(String username, UpdateMemberSettingsRequest request) {
        return memberRepository.findByUsername(username).map(member -> {
            // Update name if provided
            if (request.getName() != null && !request.getName().isEmpty()) {
                member.setName(request.getName());
            }

            // Update username if provided and different
            if (request.getUsername() != null && !request.getUsername().isEmpty()
                    && !request.getUsername().equals(username)) {
                // Check if new username already exists
                if (memberRepository.existsByUsername(request.getUsername())) {
                    throw new IllegalArgumentException("Username already taken");
                }
                member.setUsername(request.getUsername());
            }

            // Update password if provided
            if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                // Verify current password
                if (request.getCurrentPassword() == null || request.getCurrentPassword().isEmpty()) {
                    throw new IllegalArgumentException("Current password is required");
                }
                if (!passwordService.verifyPassword(request.getCurrentPassword(), member.getPassword())) {
                    throw new IllegalArgumentException("Current password is incorrect");
                }
                member.setPassword(passwordService.hashPassword(request.getNewPassword()));
            }

            return toDto(memberRepository.save(member));
        });
    }

    public boolean deleteMember(String id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MemberDto toDto(Member member) {
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getPosition(),
                member.getTitle(),
                member.getResearchArea(),
                member.getBio(),
                member.getPhotoUrl(),
                member.getGoogleScholarLink(),
                member.getExpertise(),
                member.getJoinedDate() != null ? member.getJoinedDate().toString() : null);
    }
}
