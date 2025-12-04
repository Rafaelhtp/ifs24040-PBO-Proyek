package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<AuthToken, UUID> {
    
    // 1. Mencari data berdasarkan string tokennya (misal untuk validasi login otomatis)
    Optional<AuthToken> findByToken(String token);

    // 2. Mencari token aktif milik user tertentu
    Optional<AuthToken> findByUserId(UUID userId);
    
    // 3. Menghapus token (misal saat Logout)
    void deleteByToken(String token);
}