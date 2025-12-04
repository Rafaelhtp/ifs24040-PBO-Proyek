package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.dto.request.RegisterRequest;
import com.rafaelnobel.researchtracker.entity.AuthToken;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.TokenRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    // --- 1. METHOD REGISTER USER (Ini yang dicari error baris 57) ---
    public void registerUser(RegisterRequest request) {
        // Cek apakah email sudah ada?
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email sudah terdaftar!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Di dunia nyata ini harus di-hash
        
        // Atribut Wajib Sistem
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

    // --- 2. METHOD AUTHENTICATE (Ini yang dicari error baris 32) ---
    public User authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Cek password sederhana
            if (user.getPassword().equals(password)) {
                // Login Sukses -> Buat Token di Database
                createToken(user);
                return user;
            }
        }
        return null; // Login gagal
    }

    // --- 3. HELPER METHOD: BUAT TOKEN ---
    private void createToken(User user) {
        AuthToken token = new AuthToken();
        token.setUserId(user.getId());
        token.setToken(UUID.randomUUID().toString()); // Generate token unik
        token.setCreatedAt(LocalDateTime.now());
        
        tokenRepository.save(token);
    }
}