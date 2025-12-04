package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Aktifkan mode test
public class ResearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService; // Palsukan StorageService

    @Test
    void testListResearch_WithoutLogin_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/research"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
        System.out.println("✅ Test Akses Tanpa Login: BERHASIL DITOLAK");
    }

    @Test
    void testListResearch_WithLogin_ShouldSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setName("Tester Mahasiswa");
        mockUser.setEmail("tester@del.ac.id");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser);

        mockMvc.perform(get("/research").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("research/list"));
        System.out.println("✅ Test Akses Dengan Login: BERHASIL MASUK");
    }
}