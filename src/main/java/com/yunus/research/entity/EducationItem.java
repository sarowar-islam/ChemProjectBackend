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
public class EducationItem {
    private String degree;
    private String institution;
    private String department;
    private String graduationYear;
    @Column(columnDefinition = "TEXT")
    private String thesis;
}
