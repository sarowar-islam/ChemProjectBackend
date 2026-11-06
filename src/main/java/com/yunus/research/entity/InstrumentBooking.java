package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "instrument_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String instrumentId;

    private String requesterId;

    @Column(nullable = false)
    private String requesterName;

    @Column(nullable = false)
    private String requesterEmail;

    private String requesterPhone;

    @Column(nullable = false)
    private String affiliation;

    private String supervisorName;

    @Column(nullable = false)
    private String purposeTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String purposeDescription;

    @Column(columnDefinition = "TEXT")
    private String sampleDetails;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String status; // pending, approved, rejected, cancelled

    @Column(columnDefinition = "TEXT")
    private String adminNote;

    private String reviewedBy;

    private LocalDateTime reviewedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }
}
