package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instruments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortDescription;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String status; // available, booked, in_use, maintenance

    @Column(nullable = false)
    private String imageUrl;

    private String sopUrl;

    private String operatingProcedureUrl;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    private String serialNumber;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String responsibleAdminId;

    @Column(nullable = false)
    private String responsibleAdminName;

    @Column(nullable = false)
    private String responsibleAdminEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instrument_features", joinColumns = @JoinColumn(name = "instrument_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instrument_usage_requirements", joinColumns = @JoinColumn(name = "instrument_id"))
    @Column(name = "requirement")
    private List<String> usageRequirements = new ArrayList<>();

    @Column(nullable = false)
    private Integer openingHour;

    @Column(nullable = false)
    private Integer closingHour;

    @Column(nullable = false)
    private Integer slotIntervalMinutes;

    @Column(nullable = false)
    private Integer minDurationMinutes;

    @Column(nullable = false)
    private Integer maxDurationMinutes;

    @Column(nullable = false)
    private Integer advanceBookingDays;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
