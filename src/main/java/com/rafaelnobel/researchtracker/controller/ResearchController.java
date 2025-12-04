package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.dto.request.ResearchRequest;
import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.service.ResearchService;
import com.rafaelnobel.researchtracker.service.StorageService; // Import Service Baru
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/research")
public class ResearchController {

    @Autowired
    private ResearchService researchService;

    @Autowired
    private StorageService storageService; // Inject StorageService

    // 1. Tampilkan Daftar Penelitian
    @GetMapping
    public String listResearch(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Research> researchList = researchService.getAllResearch(user.getId());
        model.addAttribute("researches", researchList);
        return "research/list";
    }

    // 2. Tampilkan Form Tambah
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("researchRequest", new ResearchRequest());
        return "research/form";
    }

    // 3. Proses Tambah Data (UPDATED: Pakai storageService)
    @PostMapping("/add")
    public String addResearch(@ModelAttribute ResearchRequest request, 
                              @RequestParam("coverImage") MultipartFile file,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Research research = new Research();
        research.setTitle(request.getTitle());
        research.setResearchTheme(request.getResearchTheme());
        research.setFundAmount(request.getFundAmount());
        research.setReleaseYear(request.getReleaseYear());
        
        // --- PERBAIKAN DI SINI ---
        // Kita tidak lagi pakai Files.write manual, tapi panggil service
        if (!file.isEmpty()) {
            String filename = storageService.store(file); // Simpan file & dapatkan nama unik
            research.setCoverFilename(filename);
        }
        // -------------------------

        researchService.createResearch(research, user.getId());
        return "redirect:/research";
    }

    // 4. Hapus Data
    @GetMapping("/delete/{id}")
    public String deleteResearch(@PathVariable UUID id) {
        // (Opsional) Ambil data dulu untuk hapus gambarnya
        Research r = researchService.getResearchById(id);
        if (r.getCoverFilename() != null) {
            storageService.delete(r.getCoverFilename()); // Hapus file fisik biar hemat storage
        }
        
        researchService.deleteResearch(id);
        return "redirect:/research";
    }
    
    // 5. Tampilkan Detail
    @GetMapping("/{id}")
    public String detailResearch(@PathVariable UUID id, Model model) {
        Research research = researchService.getResearchById(id);
        model.addAttribute("research", research);
        return "research/detail";
    }
}