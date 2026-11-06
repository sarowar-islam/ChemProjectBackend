package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentDto {
    private String id;
    private String slug;
    private String name;
    private String shortDescription;
    private String description;
    private String categoryId;
    private String category;
    private String status;
    private String imageUrl;
    private String sopUrl;
    private String operatingProcedureUrl;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String location;
    private String responsibleAdminId;
    private String responsibleAdminName;
    private String responsibleAdminEmail;
    private List<String> features;
    private List<String> usageRequirements;
    private Integer openingHour;
    private Integer closingHour;
    private Integer slotIntervalMinutes;
    private Integer minDurationMinutes;
    private Integer maxDurationMinutes;
    private Integer advanceBookingDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InstrumentSpecificationDto> specifications;
    private Double averageRating;
    private Long reviewCount;
}
