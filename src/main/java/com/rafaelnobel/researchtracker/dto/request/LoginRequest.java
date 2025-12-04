package com.rafaelnobel.researchtracker.dto.request;

public class LoginRequest {
    private String email;
    private String password;

    // --- MANUAL GETTER & SETTER ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}