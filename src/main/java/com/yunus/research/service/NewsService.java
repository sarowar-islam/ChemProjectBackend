package com.yunus.research.service;

import com.yunus.research.dto.NewsDto;
import com.yunus.research.entity.News;
import com.yunus.research.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public List<NewsDto> getAllNews() {
        return newsRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<NewsDto> getRecentNews(int limit) {
        return newsRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit)).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<NewsDto> getNewsById(String id) {
        return newsRepository.findById(id).map(this::toDto);
    }

    public NewsDto createNews(NewsDto dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setImageUrl(dto.getImageUrl());
        news.setAuthor(dto.getAuthor());

        News saved = newsRepository.save(news);
        return toDto(saved);
    }

    public Optional<NewsDto> updateNews(String id, NewsDto dto) {
        return newsRepository.findById(id).map(news -> {
            if (dto.getTitle() != null)
                news.setTitle(dto.getTitle());
            if (dto.getSummary() != null)
                news.setSummary(dto.getSummary());
            if (dto.getContent() != null)
                news.setContent(dto.getContent());
            if (dto.getImageUrl() != null)
                news.setImageUrl(dto.getImageUrl());
            if (dto.getAuthor() != null)
                news.setAuthor(dto.getAuthor());

            return toDto(newsRepository.save(news));
        });
    }

    public boolean deleteNews(String id) {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private NewsDto toDto(News news) {
        return new NewsDto(
                news.getId(),
                news.getTitle(),
                news.getSummary(),
                news.getContent(),
                news.getImageUrl(),
                news.getCreatedAt() != null ? news.getCreatedAt().toString() : null,
                news.getAuthor());
    }
}
