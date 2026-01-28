package com.yunus.research.service;

import com.yunus.research.dto.NoticeDto;
import com.yunus.research.entity.Notice;
import com.yunus.research.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<NoticeDto> getRecentNotices(int limit) {
        return noticeRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit)).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<NoticeDto> getNoticeById(String id) {
        return noticeRepository.findById(id).map(this::toDto);
    }

    public NoticeDto createNotice(NoticeDto dto) {
        Notice notice = new Notice();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setPriority(parsePriority(dto.getPriority()));

        Notice saved = noticeRepository.save(notice);
        return toDto(saved);
    }

    public Optional<NoticeDto> updateNotice(String id, NoticeDto dto) {
        return noticeRepository.findById(id).map(notice -> {
            if (dto.getTitle() != null)
                notice.setTitle(dto.getTitle());
            if (dto.getContent() != null)
                notice.setContent(dto.getContent());
            if (dto.getPriority() != null)
                notice.setPriority(parsePriority(dto.getPriority()));

            return toDto(noticeRepository.save(notice));
        });
    }

    public boolean deleteNotice(String id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Notice.NoticePriority parsePriority(String priority) {
        if (priority == null)
            return Notice.NoticePriority.NORMAL;
        return switch (priority.toLowerCase()) {
            case "important" -> Notice.NoticePriority.IMPORTANT;
            default -> Notice.NoticePriority.NORMAL;
        };
    }

    private NoticeDto toDto(Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt() != null ? notice.getCreatedAt().toString() : null,
                notice.getPriority().name().toLowerCase());
    }
}
