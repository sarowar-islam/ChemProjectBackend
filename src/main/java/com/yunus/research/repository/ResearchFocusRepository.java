package com.yunus.research.repository;

import com.yunus.research.entity.ResearchFocus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchFocusRepository extends JpaRepository<ResearchFocus, String> {
    List<ResearchFocus> findAllByOrderByDisplayOrderAscCreatedAtAsc();
}
