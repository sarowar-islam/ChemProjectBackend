package com.yunus.research.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerItem {
    private String title;
    private String organization;
    private String startDate;
    private String endDate;
    @Column(name = "is_current")
    private Boolean isCurrent;
    @Column(columnDefinition = "TEXT")
    private String description;
}
