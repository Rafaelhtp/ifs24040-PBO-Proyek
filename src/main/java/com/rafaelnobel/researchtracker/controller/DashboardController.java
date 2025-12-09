package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import com.rafaelnobel.researchtracker.service.ResearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ResearchService researchService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, @org.springframework.web.bind.annotation.RequestParam(required = false) String q) {
        User user = (User) session.getAttribute("user");

        // --- BYPASS LOGIN (Untuk Demo) ---
        if (user == null) {
            user = userRepository.findByEmail("admin@del.ac.id").orElse(null);
            if (user == null) {
                // Buat user dummy di memori jika db kosong
                user = new User();
                user.setId(UUID.randomUUID());
                user.setName("Pengguna Demo");
            }
            session.setAttribute("user", user);
        }

        // Ambil semua data penelitian
        List<Research> researches = new ArrayList<>();
        try {
            // Jika user admin, kita ambil SEMUA data (biar grafik ramai)
            if (user.getEmail() != null && user.getEmail().equals("admin@del.ac.id")) {
                researches = researchService.getAllResearch(user.getId()); 
            } else {
                researches = researchService.getAllResearch(user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (q != null && !q.isBlank()) {
            String s = q.toLowerCase();
            researches = researches.stream().filter(r ->
                    (r.getTitle() != null && r.getTitle().toLowerCase().contains(s)) ||
                    (r.getResearcherName() != null && r.getResearcherName().toLowerCase().contains(s)) ||
                    (r.getResearchTheme() != null && r.getResearchTheme().toLowerCase().contains(s))
            ).toList();
        }

        double totalDana = researches.stream().filter(r -> r.getFundAmount() != null).mapToDouble(Research::getFundAmount).sum();
        long totalJudul = researches.size();
        long totalTema = researches.stream().map(Research::getResearchTheme).distinct().count();

        // 2. Data untuk PIE CHART (Jumlah per Tema)
        Map<String, Long> themeStats = researches.stream()
            .collect(Collectors.groupingBy(Research::getResearchTheme, Collectors.counting()));

        // 3. Data untuk BAR CHART (Dana per Tahun) - INI YANG BARU
        Map<Integer, Double> fundingByYear = researches.stream()
            .filter(r -> r.getFundAmount() != null && r.getReleaseYear() != null)
            .collect(Collectors.groupingBy(
                Research::getReleaseYear,
                Collectors.summingDouble(Research::getFundAmount)
            ));
        
        // Sorting tahun biar urut di grafik
        Map<Integer, Double> sortedFunding = new TreeMap<>(fundingByYear);

        // --- Kirim ke HTML ---
        model.addAttribute("user", user);
        model.addAttribute("researches", researches);
        model.addAttribute("q", q);
        model.addAttribute("totalResearch", totalJudul);
        model.addAttribute("totalFund", totalDana);
        model.addAttribute("totalThemes", totalTema); // Kartu ke-3
        
        // Data Chart 1 (Pie: Tema)
        model.addAttribute("themeLabels", themeStats.keySet());
        model.addAttribute("themeData", themeStats.values());
        
        // Data Chart 2 (Bar: Dana per Tahun)
        model.addAttribute("yearLabels", sortedFunding.keySet());
        model.addAttribute("yearData", sortedFunding.values());

        return "dashboard";
    }

    // Aliases: akses langsung tanpa prefix /research
    @GetMapping("/researchers")
    public String aliasResearchers() {
        return "redirect:/research/researchers";
    }

    @GetMapping("/researchers/add")
    public String aliasResearchersAdd() {
        return "redirect:/research/researchers/add";
    }

    // Detail dari Dashboard
    @GetMapping("/dashboard/research/{id}")
public String dashboardDetail(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id, Model model, HttpSession session) {
    
    // --- TAMBAHAN PENTING: Ambil User dari Session (Sama seperti di method dashboard) ---
    User user = (User) session.getAttribute("user");
    if (user == null) {
        // Logic bypass login untuk demo (agar konsisten dengan method dashboard)
        user = userRepository.findByEmail("admin@del.ac.id").orElse(null);
        if (user == null) {
            user = new User();
            user.setId(UUID.randomUUID());
            user.setName("Pengguna Demo");
        }
        session.setAttribute("user", user);
    }
    model.addAttribute("user", user); // <--- INI YANG HILANG SEBELUMNYA!
    // -----------------------------------------------------------------------------------

    com.rafaelnobel.researchtracker.entity.Research research = researchService.getResearchById(id);
    
    // Cek null untuk menghindari error di HTML
    if (research == null) {
        model.addAttribute("errorMessage", "Data tidak ditemukan atau sudah dihapus.");
        return "error"; 
    }
    
    model.addAttribute("research", research);
    return "research/detail";
}}
