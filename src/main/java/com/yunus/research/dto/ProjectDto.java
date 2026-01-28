package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String id;
    private String title;
    private String description;
    private String researchLink;
    private String status; // "ongoing" or "completed"
    private String startDate;
    private String endDate;
}
