package com.rafaelnobel.researchtracker.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class ResearchRequest {
    private String title;
    private String researchTheme;
    private Double fundAmount;
    private Integer releaseYear;
    private MultipartFile coverImage;

    // --- MANUAL GETTER & SETTER ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getResearchTheme() { return researchTheme; }
    public void setResearchTheme(String researchTheme) { this.researchTheme = researchTheme; }

    public Double getFundAmount() { return fundAmount; }
    public void setFundAmount(Double fundAmount) { this.fundAmount = fundAmount; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public MultipartFile getCoverImage() { return coverImage; }
    public void setCoverImage(MultipartFile coverImage) { this.coverImage = coverImage; }
}