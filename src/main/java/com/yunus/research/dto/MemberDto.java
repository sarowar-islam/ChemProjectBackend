package com.yunus.research.dto;

import com.yunus.research.entity.CareerItem;
import com.yunus.research.entity.EducationItem;
import com.yunus.research.entity.SocialLink;
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
    private String securityCode;
    private String phone;
    private String position;
    private String title;
    private String researchArea;
    private String bio;
    private String photoUrl;
    private String googleScholarLink;
    private List<SocialLink> socialLinks;
    private List<String> expertise;
    private String joinedDate;

    // Alumni / Professional fields
    private String currentTitle;
    private String currentOrganization;
    private String currentDepartment;
    private String location;
    private String currentPositionStartDate;
    private String currentPositionDescription;
    private String formerRole;
    private String labAffiliation;
    private String labContributions;
    private String orcid;
    private String researchGate;
    private String personalWebsite;
    private Boolean isEmailPublic;
    private Boolean isContactPublic;
    private List<CareerItem> careerHistory;
    private List<EducationItem> education;
}
