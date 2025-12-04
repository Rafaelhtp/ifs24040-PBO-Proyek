package com.rafaelnobel.researchtracker.config;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, ResearchRepository researchRepo) {
        return args -> {
            // 1. Buat User Admin (Jika belum ada)
            User admin = userRepo.findByEmail("admin@del.ac.id").orElse(null);
            if (admin == null) {
                admin = new User();
                admin.setName("Admin Dosen");
                admin.setEmail("admin@del.ac.id");
                admin.setPassword("admin123");
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                userRepo.save(admin);
                System.out.println(">>> User Admin Siap: admin@del.ac.id");
            }

            // 2. Isi Data Penelitian Riil (Hanya jika database kosong)
            if (researchRepo.count() == 0) {
                System.out.println(">>> Mengisi Data Penelitian Riil (Normalisasi)...");

                // --- TEMA 1: INFOKOM ---
                createResearch(researchRepo, admin, 
                    "Ekstraksi Frasa Domain Agrikultur dalam Sistem Temu Balik Informasi (Peneliti: Togu Novriansyah Turnip)", 
                    "INFOKOM", 2024, 3300000.0);
                
                createResearch(researchRepo, admin, 
                    "Intelligent Database Index Suggestion (Peneliti: Ahmad Zatnika Purwalaksana)", 
                    "INFOKOM", 2024, 2250000.0);

                // --- TEMA 2: TEKNOLOGI TEPAT GUNA (TTG) ---
                createResearch(researchRepo, admin, 
                    "Studi Sistem Pengendali Temperatur pada Makanan Fermentasi: Tempe (Peneliti: Tiurma Lumban Gaol)", 
                    "TTG", 2024, 7500000.0);

                createResearch(researchRepo, admin, 
                    "Perbandingan Efektivitas Single-Stage dan Multi-Stage Coil Gun (Peneliti: Nenni Mona Aruan)", 
                    "TTG", 2024, 7000000.0);

                // --- TEMA 3: PENDIDIKAN ---
                createResearch(researchRepo, admin, 
                    "Analysis of Students’ Report Writing from SFL-GP Perspective (Peneliti: Hernawati Samosir)", 
                    "Pendidikan", 2024, 2950000.0);

                createResearch(researchRepo, admin, 
                    "Factors Influencing Academic Participation of Computer Engineering Students (Peneliti: Riyanthi Angrainy)", 
                    "Pendidikan", 2024, 3850000.0);

                System.out.println(">>> 6 Data Penelitian Berhasil Masuk!");
            }
        };
    }

    private void createResearch(ResearchRepository repo, User user, String title, String theme, int year, Double dana) {
        Research r = new Research();
        r.setTitle(title);
        r.setResearchTheme(theme);
        r.setReleaseYear(year);
        r.setFundAmount(dana);
        r.setUserId(user.getId()); // Semua data dikaitkan ke Admin agar muncul di dashboard saat demo
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        repo.save(r);
    }
}