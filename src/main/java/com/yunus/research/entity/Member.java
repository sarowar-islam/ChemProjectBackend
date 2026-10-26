package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    private String position;

    private String title;

    @Column(name = "research_area")
    private String researchArea;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "security_code", length = 6)
    private String securityCode;

    @Column(name = "google_scholar_link")
    private String googleScholarLink;

    // Alumni / Professional fields
    @Column(name = "current_title")
    private String currentTitle;

    @Column(name = "current_organization")
    private String currentOrganization;

    @Column(name = "current_department")
    private String currentDepartment;

    private String location;

    @Column(name = "current_position_start_date")
    private String currentPositionStartDate;

    @Column(name = "current_position_description", columnDefinition = "TEXT")
    private String currentPositionDescription;

    @Column(name = "former_role")
    private String formerRole;

    @Column(name = "lab_affiliation")
    private String labAffiliation;

    @Column(name = "lab_contributions", columnDefinition = "TEXT")
    private String labContributions;

    private String orcid;

    @Column(name = "research_gate")
    private String researchGate;

    @Column(name = "personal_website")
    private String personalWebsite;

    @Column(name = "is_email_public")
    private Boolean isEmailPublic = true;

    @Column(name = "is_contact_public")
    private Boolean isContactPublic = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_social_links", joinColumns = @JoinColumn(name = "member_id"))
    private List<SocialLink> socialLinks = new ArrayList<>();

    // Store expertise as JSON/ElementCollection in PostgreSQL
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_expertise", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "expertise")
    private List<String> expertise = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_career_history", joinColumns = @JoinColumn(name = "member_id"))
    private List<CareerItem> careerHistory = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_education", joinColumns = @JoinColumn(name = "member_id"))
    private List<EducationItem> education = new ArrayList<>();

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (joinedDate == null) {
            joinedDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
