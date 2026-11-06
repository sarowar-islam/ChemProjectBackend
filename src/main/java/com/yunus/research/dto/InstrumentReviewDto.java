package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentReviewDto {
    private String id;
    private String instrumentId;
    private String bookingId;
    private String reviewerName;
    private String reviewerAffiliation;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
