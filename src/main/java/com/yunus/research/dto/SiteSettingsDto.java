package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingsDto {
    private String id;
    private String googleScholarUrl;
    private String aboutUs;
    private String updatedAt;
}
