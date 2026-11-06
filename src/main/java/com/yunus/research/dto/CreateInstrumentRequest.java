package com.yunus.research.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstrumentRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Short description is required")
    private String shortDescription;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Category name is required")
    private String category;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String sopUrl;

    private String operatingProcedureUrl;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    private String serialNumber;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Responsible admin ID is required")
    private String responsibleAdminId;

    @NotBlank(message = "Responsible admin name is required")
    private String responsibleAdminName;

    @NotBlank(message = "Responsible admin email is required")
    @Email(message = "Invalid email format")
    private String responsibleAdminEmail;

    private List<String> features;
    private List<String> usageRequirements;

    @NotNull(message = "Opening hour is required")
    @Min(value = 0, message = "Opening hour must be between 0 and 23")
    @Max(value = 23, message = "Opening hour must be between 0 and 23")
    private Integer openingHour;

    @NotNull(message = "Closing hour is required")
    @Min(value = 1, message = "Closing hour must be between 1 and 24")
    @Max(value = 24, message = "Closing hour must be between 1 and 24")
    private Integer closingHour;

    @NotNull(message = "Slot interval is required")
    private Integer slotIntervalMinutes;

    @NotNull(message = "Min duration is required")
    private Integer minDurationMinutes;

    @NotNull(message = "Max duration is required")
    private Integer maxDurationMinutes;

    @NotNull(message = "Advance booking days is required")
    private Integer advanceBookingDays;

    private List<SpecificationInput> specifications;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecificationInput {
        @NotBlank(message = "Specification label is required")
        private String label;

        @NotBlank(message = "Specification value is required")
        private String value;
    }
}
