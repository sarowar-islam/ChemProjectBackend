package com.yunus.research.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    @NotBlank(message = "Instrument ID is required")
    private String instrumentId;

    private String requesterId;

    @NotBlank(message = "Requester name is required")
    private String requesterName;

    @NotBlank(message = "Requester email is required")
    @Email(message = "Invalid email format")
    private String requesterEmail;

    private String requesterPhone;

    @NotBlank(message = "Affiliation is required")
    private String affiliation;

    private String supervisorName;

    @NotBlank(message = "Purpose title is required")
    private String purposeTitle;

    @NotBlank(message = "Purpose description is required")
    private String purposeDescription;

    private String sampleDetails;

    @NotNull(message = "Start time is required")
    private LocalDateTime startAt;

    @NotNull(message = "End time is required")
    private LocalDateTime endAt;
}
