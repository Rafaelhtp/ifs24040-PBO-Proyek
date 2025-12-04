package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Mencari user berdasarkan email (penting untuk login)
    Optional<User> findByEmail(String email);
}