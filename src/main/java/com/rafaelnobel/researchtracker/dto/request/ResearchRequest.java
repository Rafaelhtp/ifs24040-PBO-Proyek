package com.rafaelnobel.researchtracker.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class ResearchRequest {
    private String title;
    private String researchTheme;
    private String researcherName;
    private String researcherEmail;
    private String researcherInstitution;
    private String topic;
    private String description;
    private String impact;
    private Double fundAmount;
    private Integer releaseYear;
    private String fundCurrency;
    private String fundSource;
    private MultipartFile coverImage;

    // --- MANUAL GETTER & SETTER ---
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

    public Double getFundAmount() { return fundAmount; }
    public void setFundAmount(Double fundAmount) { this.fundAmount = fundAmount; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getFundCurrency() { return fundCurrency; }
    public void setFundCurrency(String fundCurrency) { this.fundCurrency = fundCurrency; }

    public String getFundSource() { return fundSource; }
    public void setFundSource(String fundSource) { this.fundSource = fundSource; }

    public MultipartFile getCoverImage() { return coverImage; }
    public void setCoverImage(MultipartFile coverImage) { this.coverImage = coverImage; }
}
