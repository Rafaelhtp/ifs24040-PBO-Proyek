package com.rafaelnobel.researchtracker.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResearchResponse {
    private UUID id;
    private String title;
    private String researchTheme;
    private Double fundAmount;
    private Integer releaseYear;
    private String coverFilename;
    
    // Kita kirim ID pemilik, tapi bisa juga dikembangkan mengirim nama pemilik (userName)
    private UUID userId; 
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}