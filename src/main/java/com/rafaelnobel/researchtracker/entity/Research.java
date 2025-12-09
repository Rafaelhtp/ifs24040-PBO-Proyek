package com.rafaelnobel.researchtracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "researches")
public class Research {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String researchTheme;

    private String researcherName;
    private String researcherEmail;
    private String researcherInstitution;

    private String topic;
    @Column(length = 2000)
    private String description;
    @Column(length = 2000)
    private String impact;

    private String fundCurrency; // e.g., IDR, USD
    private String fundSource;   // e.g., Dikti, Internal Kampus

    @Column(nullable = false)
    private Double fundAmount;

    @Column(nullable = false)
    private Integer releaseYear;

  

    private String coverFilename;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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

    // --- MANUAL GETTER & SETTER ---
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getResearchTheme() { return researchTheme; }
    public void setResearchTheme(String researchTheme) { this.researchTheme = researchTheme; }

    public String getResearcherName() { return researcherName; }
    public void setResearcherName(String researcherName) { this.researcherName = researcherName; }

    public String getResearcherEmail() { return researcherEmail; }
    public void setResearcherEmail(String researcherEmail) { this.researcherEmail = researcherEmail; }

    public String getResearcherInstitution() { return researcherInstitution; }
    public void setResearcherInstitution(String researcherInstitution) { this.researcherInstitution = researcherInstitution; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }

    public String getFundCurrency() { return fundCurrency; }
    public void setFundCurrency(String fundCurrency) { this.fundCurrency = fundCurrency; }

    public String getFundSource() { return fundSource; }
    public void setFundSource(String fundSource) { this.fundSource = fundSource; }

    public Double getFundAmount() { return fundAmount; }
    public void setFundAmount(Double fundAmount) { this.fundAmount = fundAmount; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

 

    public String getCoverFilename() { return coverFilename; }
    public void setCoverFilename(String coverFilename) { this.coverFilename = coverFilename; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
