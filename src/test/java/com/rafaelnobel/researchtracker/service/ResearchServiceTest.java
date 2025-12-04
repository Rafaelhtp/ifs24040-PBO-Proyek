package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ResearchServiceTest {

    @Mock
    private ResearchRepository researchRepository;

    @InjectMocks
    private ResearchService researchService;

    @Test
    void testCreateResearch() {
        // 1. Siapkan Data Dummy
        UUID userId = UUID.randomUUID();
        Research research = new Research();
        research.setTitle("Penelitian AI");
        research.setFundAmount(5000000.0);

        // 2. Manipulasi Repository (Pura-pura sukses simpan)
        Mockito.when(researchRepository.save(Mockito.any(Research.class))).thenAnswer(invocation -> {
            Research r = invocation.getArgument(0);
            r.setId(UUID.randomUUID()); // Pura-pura dapat ID dari DB
            return r;
        });

        // 3. Jalankan Service
        Research saved = researchService.createResearch(research, userId);

        // 4. Cek Hasil (Assert)
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Penelitian AI", saved.getTitle());
        Assertions.assertEquals(userId, saved.getUserId());
        System.out.println("✅ Test Create Research: SUKSES");
    }

    @Test
    void testGetTotalFunds() {
        // 1. Siapkan Data Dummy
        UUID userId = UUID.randomUUID();
        Research r1 = new Research(); r1.setFundAmount(100.0);
        Research r2 = new Research(); r2.setFundAmount(200.0);
        
        Mockito.when(researchRepository.findByUserId(userId)).thenReturn(List.of(r1, r2));

        // 2. Jalankan Service (Harusnya 100 + 200 = 300)
        List<Research> list = researchService.getAllResearch(userId);
        double total = list.stream().mapToDouble(Research::getFundAmount).sum();

        // 3. Cek Hasil
        Assertions.assertEquals(300.0, total);
        System.out.println("✅ Test Hitung Total Dana: SUKSES");
    }
}