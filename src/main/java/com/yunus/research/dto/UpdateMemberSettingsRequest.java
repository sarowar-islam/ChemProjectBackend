package com.yunus.research.dto;

import lombok.Data;

@Data
public class UpdateMemberSettingsRequest {
    private String name;
    private String username;
    private String currentPassword;
    private String newPassword;
}
