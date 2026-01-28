package com.yunus.research.service;

import com.yunus.research.dto.MemberDto;
import com.yunus.research.dto.CreateMemberRequest;
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
        member.setDesignation(request.getDesignation());
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
            if (dto.getDesignation() != null)
                member.setDesignation(dto.getDesignation());
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
                member.getDesignation(),
                member.getResearchArea(),
                member.getBio(),
                member.getPhotoUrl(),
                member.getGoogleScholarLink(),
                member.getExpertise(),
                member.getJoinedDate() != null ? member.getJoinedDate().toString() : null);
    }
}
