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

    // --- 1. METHOD REGISTER USER ---
    public User registerUser(RegisterRequest request) {
        // Cek apakah email sudah ada?
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email sudah terdaftar!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Logika Password: Jika null atau blank, pakai default
        // (Ternary operator ini dicover oleh testRegisterUser_NullPassword dan testRegisterUser_EmptyPassword)
        String pwd = (request.getPassword() != null && !request.getPassword().isBlank()) 
                     ? request.getPassword() 
                     : "default123";
        user.setPassword(pwd); 

        // Simpan profil tambahan
        user.setInstitution(request.getInstitution());
        user.setField(request.getField());
        String themePref = request.getDefaultTheme() != null ? request.getDefaultTheme() : request.getTheme();
        user.setDefaultTheme(themePref);
        
        // Atribut Wajib Sistem
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    // --- 2. METHOD AUTHENTICATE ---
    public User authenticate(String email, String password) {
        // Guard clause: jika parameter null langsung return null
        if (email == null || password == null) {
            return null;
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Cek password sederhana
            // Pastikan user.getPassword() tidak null untuk menghindari NPE
            if (user.getPassword() != null && user.getPassword().equals(password)) {
                // Login Sukses -> Buat Token di Database
                createToken(user);
                return user;
            }
        }
        return null; // Login gagal (User tidak ketemu atau password salah)
    }

    // --- 3. HELPER METHOD: BUAT TOKEN ---
    private void createToken(User user) {
        AuthToken token = new AuthToken();
        token.setUserId(user.getId());
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
