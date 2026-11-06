package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "instrument_unavailable_periods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentUnavailablePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String instrumentId;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String type; // maintenance, calibration, closed
}
