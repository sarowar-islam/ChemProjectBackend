package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentBookingDto {
    private String id;
    private String instrumentId;
    private String requesterId;
    private String requesterName;
    private String requesterEmail;
    private String requesterPhone;
    private String affiliation;
    private String supervisorName;
    private String purposeTitle;
    private String purposeDescription;
    private String sampleDetails;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private String adminNote;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
