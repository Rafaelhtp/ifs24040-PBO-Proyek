package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResearchServiceTest {

    @Mock
    private ResearchRepository researchRepository;

    @InjectMocks
    private ResearchService researchService;

    // --- 1. Test Create ---
    @Test
    void testCreateResearch() {
        UUID userId = UUID.randomUUID();
        Research research = new Research();
        research.setTitle("Penelitian AI");
        research.setFundAmount(5000000.0);

        when(researchRepository.save(any(Research.class))).thenAnswer(invocation -> {
            Research r = invocation.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        Research saved = researchService.createResearch(research, userId);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(userId, saved.getUserId());
    }

    // --- 2. Test Get All (Fix Merah di Baris 20) ---
    @Test
    void testGetAllResearchByUserId() {
        UUID userId = UUID.randomUUID();
        Research r1 = new Research();
        when(researchRepository.findByUserId(userId)).thenReturn(Arrays.asList(r1));

        // Panggil method yang sebelumnya merah
        List<Research> result = researchService.getAllResearch(userId);

        Assertions.assertEquals(1, result.size());
        verify(researchRepository, times(1)).findByUserId(userId);
    }

    // --- 3. Test Get All Admin ---
    @Test
    void testGetAllResearchForAdmin() {
        Research r1 = new Research();
        Research r2 = new Research();
        when(researchRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Research> result = researchService.getAllResearchForAdmin();

        Assertions.assertEquals(2, result.size());
    }

    // --- 4. Test Get By ID ---
    @Test
    void testGetResearchById() {
        UUID id = UUID.randomUUID();
        Research research = new Research();
        research.setId(id);
        when(researchRepository.findById(id)).thenReturn(Optional.of(research));

        Research result = researchService.getResearchById(id);

        Assertions.assertNotNull(result);
    }

    // --- 5. Test Update Lengkap (Gambar Ada) ---
    @Test
    void testUpdateResearch_WithImage() {
        UUID id = UUID.randomUUID();
        Research existing = new Research();
        existing.setId(id);
        existing.setTitle("Lama");
        existing.setCoverFilename("lama.jpg");

        Research baru = new Research();
        baru.setTitle("Baru");
        baru.setCoverFilename("baru.jpg"); // Case: Gambar diupdate

        when(researchRepository.findById(id)).thenReturn(Optional.of(existing));
        when(researchRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Research updated = researchService.updateResearch(id, baru);

        Assertions.assertEquals("baru.jpg", updated.getCoverFilename());
    }

    // --- 6. Test Update Tanpa Gambar (Fix Kuning di Baris 59) ---
    @Test
    void testUpdateResearch_NoImage() {
        UUID id = UUID.randomUUID();
        Research existing = new Research();
        existing.setId(id);
        existing.setCoverFilename("tetap_lama.jpg");

        Research baru = new Research();
        baru.setTitle("Baru");
        baru.setCoverFilename(null); // Case: Gambar TIDAK diupdate (null)

        when(researchRepository.findById(id)).thenReturn(Optional.of(existing));
        when(researchRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Research updated = researchService.updateResearch(id, baru);

        // Pastikan gambar lama TIDAK berubah jadi null
        Assertions.assertEquals("tetap_lama.jpg", updated.getCoverFilename());
    }

    // --- 7. Test Delete ---
    @Test
    void testDeleteResearch() {
        UUID id = UUID.randomUUID();
        researchService.deleteResearch(id);
        verify(researchRepository, times(1)).deleteById(id);
    }

    // --- 8. Test Total Funds ---
    @Test
    void testGetTotalFunds() {
        UUID userId = UUID.randomUUID();
        Research r1 = new Research(); r1.setFundAmount(100.0);
        Research r2 = new Research(); r2.setFundAmount(200.0);

        when(researchRepository.findByUserId(userId)).thenReturn(Arrays.asList(r1, r2));

        Double total = researchService.getTotalFunds(userId);

        Assertions.assertEquals(300.0, total);
    }

    // --- 9. Test Distinct Themes ---
    @Test
    void testGetDistinctThemes() {
        UUID userId = UUID.randomUUID();
        when(researchRepository.findDistinctThemesByUserId(userId)).thenReturn(Arrays.asList("AI", "IoT"));

        List<String> result = researchService.getDistinctThemes(userId);

        Assertions.assertEquals(2, result.size());
    }
}