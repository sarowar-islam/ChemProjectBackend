package com.yunus.research.service;

import com.yunus.research.dto.ProjectDto;
import com.yunus.research.entity.Project;
import com.yunus.research.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDto> getProjectById(String id) {
        return projectRepository.findById(id).map(this::toDto);
    }

    public ProjectDto createProject(ProjectDto dto) {
        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setResearchLink(dto.getResearchLink());
        project.setStatus(parseStatus(dto.getStatus()));
        project.setStartDate(LocalDate.parse(dto.getStartDate()));
        if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
            project.setEndDate(LocalDate.parse(dto.getEndDate()));
        }

        Project saved = projectRepository.save(project);
        return toDto(saved);
    }

    public Optional<ProjectDto> updateProject(String id, ProjectDto dto) {
        return projectRepository.findById(id).map(project -> {
            if (dto.getTitle() != null)
                project.setTitle(dto.getTitle());
            if (dto.getDescription() != null)
                project.setDescription(dto.getDescription());
            if (dto.getResearchLink() != null)
                project.setResearchLink(dto.getResearchLink());
            if (dto.getStatus() != null)
                project.setStatus(parseStatus(dto.getStatus()));
            if (dto.getStartDate() != null)
                project.setStartDate(LocalDate.parse(dto.getStartDate()));
            if (dto.getEndDate() != null)
                project.setEndDate(LocalDate.parse(dto.getEndDate()));

            return toDto(projectRepository.save(project));
        });
    }

    public boolean deleteProject(String id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Project.ProjectStatus parseStatus(String status) {
        if (status == null)
            return Project.ProjectStatus.ONGOING;
        return switch (status.toLowerCase()) {
            case "completed" -> Project.ProjectStatus.COMPLETED;
            default -> Project.ProjectStatus.ONGOING;
        };
    }

    private ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getResearchLink(),
                project.getStatus().name().toLowerCase(),
                project.getStartDate().toString(),
                project.getEndDate() != null ? project.getEndDate().toString() : null);
    }
}
