package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.Research;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResearchRepository extends JpaRepository<Research, UUID> {
    // Mengambil semua penelitian milik user tertentu saja (agar data tidak campur aduk)
    List<Research> findByUserId(UUID userId);

    // Mengambil daftar tema unik milik user
    @org.springframework.data.jpa.repository.Query("select distinct r.researchTheme from Research r where r.userId = :userId")
    List<String> findDistinctThemesByUserId(@org.springframework.data.repository.query.Param("userId") UUID userId);
}
