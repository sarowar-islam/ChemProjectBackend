package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentCalendarEventDto {
    private String id;
    private String instrumentId;
    private String kind; // booking or unavailable
    private String title;
    private String subtitle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
