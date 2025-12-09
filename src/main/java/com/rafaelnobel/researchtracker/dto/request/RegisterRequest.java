package com.rafaelnobel.researchtracker.dto.request;

public class RegisterRequest {
    private String name;
    private String email;
    private String password; // optional
    private String institution; // institusi
    private String field;       // bidang
    private String theme;       // tema

    // --- MANUAL GETTER & SETTER ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
}
