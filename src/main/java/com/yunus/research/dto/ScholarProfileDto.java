package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarProfileDto {
    private String name;
    private String affiliation;
    private String imageUrl;
    private String interests;
    private int totalCitations;
    private int hIndex;
    private int i10Index;
    private List<ScholarPublicationDto> publications;
}
