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
public class SocialLink {
    private String platform;

    @Column(columnDefinition = "TEXT")
    private String url;
}
