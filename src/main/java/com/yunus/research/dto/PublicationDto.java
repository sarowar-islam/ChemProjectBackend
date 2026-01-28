package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDto {
    private String id;
    private String title;
    private List<String> authors;
    private Integer year;
    private String journal;
    private String pdfLink;
    private String memberId;
    private Integer citedBy;
}
