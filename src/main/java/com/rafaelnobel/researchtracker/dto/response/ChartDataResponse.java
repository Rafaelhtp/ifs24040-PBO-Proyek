package com.rafaelnobel.researchtracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartDataResponse {
    private String label; // Contoh: "INFOKOM"
    private Long value;   // Contoh: 5 (Jumlah penelitian)
}