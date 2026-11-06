package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentUnavailablePeriodDto {
    private String id;
    private String instrumentId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String reason;
    private String type;
}
