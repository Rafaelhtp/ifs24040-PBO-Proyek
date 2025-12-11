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
import java.util.Objects;
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
        User user = (User) session.getAttribute("user");
        List<String> themes = researchService.getDistinctThemes(user.getId());
        model.addAttribute("themes", themes);
        return "research/form";
    }

    // 3. Proses Tambah Data (UPDATED: Pakai storageService)
    @PostMapping("/add")
    public String addResearch(@ModelAttribute ResearchRequest request, 
                              @RequestParam("coverImage") MultipartFile file,
                              HttpSession session,
                              org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Research research = new Research();
        research.setTitle(request.getTitle());
        research.setResearchTheme(request.getResearchTheme());
        research.setResearcherName(request.getResearcherName());
        research.setResearcherEmail(request.getResearcherEmail());
        research.setResearcherInstitution(request.getResearcherInstitution());
        research.setTopic(request.getTopic());
        research.setDescription(request.getDescription());
        research.setImpact(request.getImpact());
        research.setFundAmount(request.getFundAmount());
        research.setReleaseYear(request.getReleaseYear());
        research.setFundCurrency(request.getFundCurrency());
        research.setFundSource(request.getFundSource());
        
        // --- PERBAIKAN DI SINI ---
        // Kita tidak lagi pakai Files.write manual, tapi panggil service
        if (!file.isEmpty()) {
            String filename = storageService.store(file); // Simpan file & dapatkan nama unik
            research.setCoverFilename(filename);
        }
        // -------------------------

        researchService.createResearch(research, user.getId());
        redirectAttributes.addFlashAttribute("success", "Data berhasil ditambahkan");
        return "redirect:/dashboard";
    }

    @GetMapping("/researchers")
    public String listResearchers(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Research> list = researchService.getAllResearch(user.getId());
        model.addAttribute("researchers", list.stream().map(Research::getResearcherName).filter(Objects::nonNull).distinct().toList());
        return "researcher/list";
    }

    @GetMapping("/researchers/add")
    public String addResearcherForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("researchRequest", new ResearchRequest());
        User user = (User) session.getAttribute("user");
        List<String> themes = researchService.getDistinctThemes(user.getId());
        model.addAttribute("themes", themes);
        return "researcher/form";
    }

    @PostMapping("/researchers/add")
    public String addResearcher(@ModelAttribute ResearchRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Research research = new Research();
        research.setTitle(request.getTitle());
        research.setResearchTheme(request.getResearchTheme());
        research.setResearcherName(request.getResearcherName());
        research.setReleaseYear(request.getReleaseYear());
        research.setFundAmount(request.getFundAmount() != null ? request.getFundAmount() : 0.0);
        researchService.createResearch(research, user.getId());
        return "redirect:/researchers";
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
        if (research == null) {
            model.addAttribute("errorMessage", "Data tidak ditemukan atau sudah dihapus.");
            return "error";
        }
        model.addAttribute("research", research);
        return "research/detail";
    }

    // 6. Edit Data - Form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        Research r = researchService.getResearchById(id);
        ResearchRequest req = new ResearchRequest();
        req.setTitle(r.getTitle());
        req.setResearchTheme(r.getResearchTheme());
        req.setResearcherName(r.getResearcherName());
        req.setFundAmount(r.getFundAmount());
        req.setReleaseYear(r.getReleaseYear());
        model.addAttribute("researchRequest", req);
        model.addAttribute("editId", id);
        User user = (User) session.getAttribute("user");
        List<String> themes = researchService.getDistinctThemes(user.getId());
        model.addAttribute("themes", themes);
        return "research/form";
    }

    // 7. Edit Data - Submit
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable UUID id,
                             @ModelAttribute ResearchRequest request,
                             @RequestParam(value = "coverImage", required = false) MultipartFile file,
                             HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        Research rBaru = new Research();
        rBaru.setTitle(request.getTitle());
        rBaru.setResearchTheme(request.getResearchTheme());
        rBaru.setResearcherName(request.getResearcherName());
        rBaru.setResearcherEmail(request.getResearcherEmail());
        rBaru.setResearcherInstitution(request.getResearcherInstitution());
        rBaru.setTopic(request.getTopic());
        rBaru.setDescription(request.getDescription());
        rBaru.setImpact(request.getImpact());
        rBaru.setFundAmount(request.getFundAmount());
        rBaru.setReleaseYear(request.getReleaseYear());
        rBaru.setFundCurrency(request.getFundCurrency());
        rBaru.setFundSource(request.getFundSource());
        if (file != null && !file.isEmpty()) {
            String filename = storageService.store(file);
            rBaru.setCoverFilename(filename);
        }
        researchService.updateResearch(id, rBaru);
        return "redirect:/research/" + id;
    }
}
