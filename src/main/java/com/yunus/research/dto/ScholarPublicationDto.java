package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarPublicationDto {
    private String title;
    private List<String> authors;
    private String year;
    private String journal;
    private String citedBy;
    private String articleUrl;
    private String scholarId; // Unique ID from Google Scholar
}
