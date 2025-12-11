package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResearchService {

    @Autowired
    private ResearchRepository researchRepository;

    // 1. Ambil semua data milik user yang sedang login
    public List<Research> getAllResearch(UUID userId) {
        return researchRepository.findByUserId(userId);
    }

    public List<Research> getAllResearchForAdmin() {
        return researchRepository.findAll();
    }

    // 2. Ambil satu data detail berdasarkan ID
    public Research getResearchById(UUID id) {
        return researchRepository.findById(id).orElse(null);
    }

    // 3. Simpan data baru (Create)
    public Research createResearch(Research research, UUID userId) {
        research.setUserId(userId); // Set pemilik data
        research.setCreatedAt(LocalDateTime.now());
        research.setUpdatedAt(LocalDateTime.now());
        return researchRepository.save(research);
    }

    // 4. Update data (Edit)
    public Research updateResearch(UUID id, Research researchBaru) {
        Research researchLama = getResearchById(id);
        
        // Update field yang boleh diubah saja
        researchLama.setTitle(researchBaru.getTitle());
        researchLama.setResearchTheme(researchBaru.getResearchTheme());
        researchLama.setResearcherName(researchBaru.getResearcherName());
        researchLama.setResearcherEmail(researchBaru.getResearcherEmail());
        researchLama.setResearcherInstitution(researchBaru.getResearcherInstitution());
        researchLama.setTopic(researchBaru.getTopic());
        researchLama.setDescription(researchBaru.getDescription());
        researchLama.setImpact(researchBaru.getImpact());
        researchLama.setFundAmount(researchBaru.getFundAmount());
        researchLama.setReleaseYear(researchBaru.getReleaseYear());
        researchLama.setFundCurrency(researchBaru.getFundCurrency());
        researchLama.setFundSource(researchBaru.getFundSource());
        
        // Jangan lupa update gambarnya jika ada (logika gambar nanti di Controller)
        if (researchBaru.getCoverFilename() != null) {
            researchLama.setCoverFilename(researchBaru.getCoverFilename());
        }

        researchLama.setUpdatedAt(LocalDateTime.now());
        
        return researchRepository.save(researchLama);
    }

    // 5. Hapus data (Delete)
    public void deleteResearch(UUID id) {
        researchRepository.deleteById(id);
    }
    
    // 6. Hitung Total Dana (Untuk Keperluan Chart nanti)
    public Double getTotalFunds(UUID userId) {
        List<Research> list = researchRepository.findByUserId(userId);
        return list.stream()
                .mapToDouble(Research::getFundAmount)
                .sum();
    }

    // 7. Ambil daftar tema unik milik user
    public List<String> getDistinctThemes(UUID userId) {
        return researchRepository.findDistinctThemesByUserId(userId);
    }
}
