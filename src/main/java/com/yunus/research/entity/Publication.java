package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    // Store authors as a list
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "publication_authors", joinColumns = @JoinColumn(name = "publication_id"))
    @Column(name = "author")
    private List<String> authors = new ArrayList<>();

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String journal;

    @Column(name = "pdf_link")
    private String pdfLink;

    // Reference to member who added this publication
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "cited_by")
    private Integer citedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
