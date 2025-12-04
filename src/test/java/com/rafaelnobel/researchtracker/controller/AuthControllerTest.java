package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Aktifkan mode test (DataInitializer MATI)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService; // Palsukan StorageService (Biar ga error folder)

    @Test
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login")); 
        System.out.println("✅ Test Halaman Login: BERHASIL");
    }

    @Test
    void testShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
        System.out.println("✅ Test Halaman Register: BERHASIL");
    }
}