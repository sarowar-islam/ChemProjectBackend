package com.yunus.research.repository;

import com.yunus.research.entity.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findAllByOrderByCreatedAtDesc();

    List<News> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
