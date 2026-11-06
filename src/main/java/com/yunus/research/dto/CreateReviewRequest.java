package com.yunus.research.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    @NotBlank(message = "Instrument ID is required")
    private String instrumentId;

    private String bookingId;

    @NotBlank(message = "Reviewer name is required")
    private String reviewerName;

    private String reviewerAffiliation;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;
}
