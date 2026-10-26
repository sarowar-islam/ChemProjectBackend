package com.yunus.research.service;

import com.yunus.research.dto.MemberDto;
import com.yunus.research.dto.CreateMemberRequest;
import com.yunus.research.dto.UpdateMemberSettingsRequest;
import com.yunus.research.entity.Member;
import com.yunus.research.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final int SECURITY_CODE_LENGTH = 6;
    private final SecureRandom secureRandom = new SecureRandom();
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

    public Optional<MemberDto> findByUsernameOrEmail(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return Optional.empty();
        }
        String trimmed = identifier.trim();
        if (trimmed.contains("@")) {
            return memberRepository.findByEmailIgnoreCase(trimmed).map(this::toDto);
        }
        return memberRepository.findByUsernameIgnoreCase(trimmed)
                .map(this::toDto)
                .or(() -> memberRepository.findByUsername(trimmed).map(this::toDto));
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
        member.setSecurityCode(generateSecurityCode(request.getSecurityCode()));
        member.setGoogleScholarLink(request.getGoogleScholarLink());
        member.setSocialLinks(request.getSocialLinks() != null ? request.getSocialLinks() : new ArrayList<>());
        member.setExpertise(request.getExpertise() != null ? request.getExpertise() : new ArrayList<>());
        member.setCareerHistory(new ArrayList<>());
        member.setEducation(new ArrayList<>());

        if (request.getJoinedDate() != null && !request.getJoinedDate().isEmpty()) {
            member.setJoinedDate(LocalDate.parse(request.getJoinedDate().substring(0, 10)));
        }

        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

    private String generateSecurityCode(String requestedCode) {
        if (requestedCode != null && !requestedCode.isBlank()) {
            return requestedCode.trim();
        }
        return String.format(Locale.ROOT, "%0" + SECURITY_CODE_LENGTH + "d", secureRandom.nextInt(1_000_000));
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
            if (dto.getCurrentTitle() != null)
                member.setCurrentTitle(dto.getCurrentTitle());
            if (dto.getCurrentOrganization() != null)
                member.setCurrentOrganization(dto.getCurrentOrganization());
            if (dto.getCurrentDepartment() != null)
                member.setCurrentDepartment(dto.getCurrentDepartment());
            if (dto.getLocation() != null)
                member.setLocation(dto.getLocation());
            if (dto.getCurrentPositionStartDate() != null)
                member.setCurrentPositionStartDate(dto.getCurrentPositionStartDate());
            if (dto.getCurrentPositionDescription() != null)
                member.setCurrentPositionDescription(dto.getCurrentPositionDescription());
            if (dto.getFormerRole() != null)
                member.setFormerRole(dto.getFormerRole());
            if (dto.getLabAffiliation() != null)
                member.setLabAffiliation(dto.getLabAffiliation());
            if (dto.getLabContributions() != null)
                member.setLabContributions(dto.getLabContributions());
            if (dto.getOrcid() != null)
                member.setOrcid(dto.getOrcid());
            if (dto.getResearchGate() != null)
                member.setResearchGate(dto.getResearchGate());
            if (dto.getPersonalWebsite() != null)
                member.setPersonalWebsite(dto.getPersonalWebsite());
            if (dto.getIsEmailPublic() != null)
                member.setIsEmailPublic(dto.getIsEmailPublic());
            if (dto.getIsContactPublic() != null)
                member.setIsContactPublic(dto.getIsContactPublic());

            if (dto.getSocialLinks() != null) {
                member.getSocialLinks().clear();
                member.getSocialLinks().addAll(dto.getSocialLinks());
            }
            if (dto.getExpertise() != null) {
                member.getExpertise().clear();
                member.getExpertise().addAll(dto.getExpertise());
            }
            if (dto.getCareerHistory() != null) {
                member.getCareerHistory().clear();
                member.getCareerHistory().addAll(dto.getCareerHistory());
            }
            if (dto.getEducation() != null) {
                member.getEducation().clear();
                member.getEducation().addAll(dto.getEducation());
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
            if (dto.getCurrentTitle() != null)
                member.setCurrentTitle(dto.getCurrentTitle());
            if (dto.getCurrentOrganization() != null)
                member.setCurrentOrganization(dto.getCurrentOrganization());
            if (dto.getCurrentDepartment() != null)
                member.setCurrentDepartment(dto.getCurrentDepartment());
            if (dto.getLocation() != null)
                member.setLocation(dto.getLocation());
            if (dto.getCurrentPositionStartDate() != null)
                member.setCurrentPositionStartDate(dto.getCurrentPositionStartDate());
            if (dto.getCurrentPositionDescription() != null)
                member.setCurrentPositionDescription(dto.getCurrentPositionDescription());
            if (dto.getFormerRole() != null)
                member.setFormerRole(dto.getFormerRole());
            if (dto.getLabAffiliation() != null)
                member.setLabAffiliation(dto.getLabAffiliation());
            if (dto.getLabContributions() != null)
                member.setLabContributions(dto.getLabContributions());
            if (dto.getOrcid() != null)
                member.setOrcid(dto.getOrcid());
            if (dto.getResearchGate() != null)
                member.setResearchGate(dto.getResearchGate());
            if (dto.getPersonalWebsite() != null)
                member.setPersonalWebsite(dto.getPersonalWebsite());
            if (dto.getIsEmailPublic() != null)
                member.setIsEmailPublic(dto.getIsEmailPublic());
            if (dto.getIsContactPublic() != null)
                member.setIsContactPublic(dto.getIsContactPublic());

            if (dto.getSocialLinks() != null) {
                member.getSocialLinks().clear();
                member.getSocialLinks().addAll(dto.getSocialLinks());
            }
            if (dto.getExpertise() != null) {
                member.getExpertise().clear();
                member.getExpertise().addAll(dto.getExpertise());
            }
            if (dto.getCareerHistory() != null) {
                member.getCareerHistory().clear();
                member.getCareerHistory().addAll(dto.getCareerHistory());
            }
            if (dto.getEducation() != null) {
                member.getEducation().clear();
                member.getEducation().addAll(dto.getEducation());
            }

            return toDto(memberRepository.save(member));
        });
    }

    @Transactional
    public Optional<MemberDto> updateMemberSettings(String username, UpdateMemberSettingsRequest request) {
        return memberRepository.findByUsername(username).map(member -> {
            if (request.getName() != null && !request.getName().isEmpty()) {
                member.setName(request.getName());
            }

            if (request.getUsername() != null && !request.getUsername().isEmpty()
                    && !request.getUsername().equals(username)) {
                if (memberRepository.existsByUsername(request.getUsername())) {
                    throw new IllegalArgumentException("Username already taken");
                }
                member.setUsername(request.getUsername());
            }

            if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
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
                member.getSecurityCode(),
                member.getPhone(),
                member.getPosition(),
                member.getTitle(),
                member.getResearchArea(),
                member.getBio(),
                member.getPhotoUrl(),
                member.getGoogleScholarLink(),
                member.getSocialLinks(),
                member.getExpertise(),
                member.getJoinedDate() != null ? member.getJoinedDate().toString() : null,
                member.getCurrentTitle(),
                member.getCurrentOrganization(),
                member.getCurrentDepartment(),
                member.getLocation(),
                member.getCurrentPositionStartDate(),
                member.getCurrentPositionDescription(),
                member.getFormerRole(),
                member.getLabAffiliation(),
                member.getLabContributions(),
                member.getOrcid(),
                member.getResearchGate(),
                member.getPersonalWebsite(),
                member.getIsEmailPublic(),
                member.getIsContactPublic(),
                member.getCareerHistory(),
                member.getEducation());
    }
}
