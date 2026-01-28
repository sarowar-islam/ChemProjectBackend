package com.yunus.research.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String designation;
    private String researchArea;
    private String bio;
    private String photoUrl;
    private String googleScholarLink;
    private List<String> expertise;
    private String joinedDate;
}
