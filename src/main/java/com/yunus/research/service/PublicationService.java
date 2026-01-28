package com.yunus.research.service;

import com.yunus.research.dto.PublicationDto;
import com.yunus.research.entity.Publication;
import com.yunus.research.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public List<PublicationDto> getAllPublications() {
        return publicationRepository.findAllByOrderByYearDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PublicationDto> getPublicationById(String id) {
        return publicationRepository.findById(id).map(this::toDto);
    }

    public List<PublicationDto> getPublicationsByMember(String memberId) {
        return publicationRepository.findByMemberId(memberId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PublicationDto createPublication(PublicationDto dto) {
        Publication publication = new Publication();
        publication.setTitle(dto.getTitle());
        publication.setAuthors(dto.getAuthors());
        publication.setYear(dto.getYear());
        publication.setJournal(dto.getJournal());
        publication.setPdfLink(dto.getPdfLink());
        publication.setMemberId(dto.getMemberId());
        publication.setCitedBy(dto.getCitedBy());

        Publication saved = publicationRepository.save(publication);
        return toDto(saved);
    }

    public Optional<PublicationDto> updatePublication(String id, PublicationDto dto) {
        return publicationRepository.findById(id).map(publication -> {
            if (dto.getTitle() != null)
                publication.setTitle(dto.getTitle());
            if (dto.getAuthors() != null)
                publication.setAuthors(dto.getAuthors());
            if (dto.getYear() != null)
                publication.setYear(dto.getYear());
            if (dto.getJournal() != null)
                publication.setJournal(dto.getJournal());
            if (dto.getPdfLink() != null)
                publication.setPdfLink(dto.getPdfLink());
            if (dto.getMemberId() != null)
                publication.setMemberId(dto.getMemberId());
            if (dto.getCitedBy() != null)
                publication.setCitedBy(dto.getCitedBy());

            return toDto(publicationRepository.save(publication));
        });
    }

    public boolean deletePublication(String id) {
        if (publicationRepository.existsById(id)) {
            publicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PublicationDto toDto(Publication publication) {
        return new PublicationDto(
                publication.getId(),
                publication.getTitle(),
                publication.getAuthors(),
                publication.getYear(),
                publication.getJournal(),
                publication.getPdfLink(),
                publication.getMemberId(),
                publication.getCitedBy());
    }
}
