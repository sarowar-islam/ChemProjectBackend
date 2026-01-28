package com.yunus.research.repository;

import com.yunus.research.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByStatus(Project.ProjectStatus status);

    List<Project> findAllByOrderByCreatedAtDesc();
}
