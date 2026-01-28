package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private String id;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private String date;
    private String author;
}
