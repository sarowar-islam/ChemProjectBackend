package com.yunus.research.repository;

import com.yunus.research.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String> {
    List<Notice> findAllByOrderByCreatedAtDesc();

    List<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
