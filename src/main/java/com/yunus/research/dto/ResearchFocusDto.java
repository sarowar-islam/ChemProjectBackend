package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResearchFocusDto {
    private String id;
    private String title;
    private String shortDescription;
    private String details;
    private Integer displayOrder;
    private String createdAt;
    private String updatedAt;
}
