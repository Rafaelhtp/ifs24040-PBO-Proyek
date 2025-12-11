package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.dto.request.RegisterRequest;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// PERBAIKAN UTAMA: Tambahkan 'addFilters = false'
// Ini mematikan Spring Security Filter Chain pada MockMvc, 
// sehingga request /logout benar-benar masuk ke method controller Anda.
@AutoConfigureMockMvc(addFilters = false) 
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void testProcessLogin_Success() throws Exception {
        User user = new User();
        user.setName("Rafael");
        user.setEmail("test@del.ac.id");

        Mockito.when(authService.authenticate("test@del.ac.id", "password")).thenReturn(user);

        mockMvc.perform(post("/login")
                        .param("email", "test@del.ac.id")
                        .param("password", "password")
                        // csrf() tetap bisa dibiarkan meski filter mati (tidak akan error)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void testProcessLogin_Failed() throws Exception {
        Mockito.when(authService.authenticate(any(), any())).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "salah@del.ac.id")
                        .param("password", "ngawur")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    void testProcessRegister_Success() throws Exception {
        User user = new User();
        Mockito.when(authService.registerUser(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                        .param("email", "new@del.ac.id")
                        .param("password", "123456")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/researchers/add"));
    }

    @Test
    void testProcessRegister_Failed() throws Exception {
        Mockito.when(authService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("Email sudah terdaftar!"));

        mockMvc.perform(post("/register")
                        .param("email", "exist@del.ac.id")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        // Setup session awal seolah user sedang login
        session.setAttribute("user", new User()); 

        mockMvc.perform(get("/logout")
                        .session(session)) // Inject session ke request
                .andExpect(status().is3xxRedirection())
                // Pastikan URL redirect sesuai persis dengan return di Controller (Line 71)
                .andExpect(redirectedUrl("/login?logout")); 
        
        // Assertions ini yang memastikan baris 70 (session.invalidate) tereksekusi
        Assertions.assertTrue(session.isInvalid(), "Session harusnya sudah invalid setelah logout");
    }
}