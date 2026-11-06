package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentSpecificationDto {
    private String id;
    private String instrumentId;
    private String label;
    private String value;
    private Integer sortOrder;
}
