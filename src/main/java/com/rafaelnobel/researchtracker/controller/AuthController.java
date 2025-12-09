package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.dto.request.LoginRequest;
import com.rafaelnobel.researchtracker.dto.request.RegisterRequest;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // 1. Tampilkan Halaman Login
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    // 2. Proses Login
    @PostMapping("/login")
public String processLogin(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
    System.out.println(">>> MENCOBA LOGIN: " + loginRequest.getEmail()); // DEBUG

    User user = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    
    if (user != null) {
        session.setAttribute("user", user);
        System.out.println(">>> LOGIN SUKSES! User disimpan di Session: " + user.getName()); // DEBUG
        return "redirect:/dashboard";
    }
    
    System.out.println(">>> LOGIN GAGAL! Email/Pass salah."); // DEBUG
    model.addAttribute("error", "Email atau Password salah!");
    return "auth/login";
}

    // 3. Tampilkan Halaman Register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    // 4. Proses Register
    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterRequest registerRequest, Model model, jakarta.servlet.http.HttpSession session) {
        try {
            com.rafaelnobel.researchtracker.entity.User newUser = authService.registerUser(registerRequest);
            session.setAttribute("user", newUser);
            return "redirect:/research/researchers/add"; // Arahkan langsung ke tambah profil peneliti
        } catch (RuntimeException e) {
            // Tangkap error (misal: Email sudah terdaftar)
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    // 5. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hapus sesi login
        return "redirect:/login?logout";
    }
}
