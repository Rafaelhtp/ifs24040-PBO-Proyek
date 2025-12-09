package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.repository.UserRepository;
import com.rafaelnobel.researchtracker.service.ResearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResearchService researchService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testDashboardLoads() throws Exception {
        Mockito.when(userRepository.findByEmail("admin@del.ac.id")).thenReturn(Optional.empty());
        Mockito.when(researchService.getAllResearch(Mockito.any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void testDashboardDetailLoads() throws Exception {
        java.util.UUID id = java.util.UUID.randomUUID();
        com.rafaelnobel.researchtracker.entity.Research r = new com.rafaelnobel.researchtracker.entity.Research();
        r.setId(id);
        r.setTitle("Judul");
        r.setResearchTheme("Tema");
        r.setFundAmount(1000.0);
        r.setReleaseYear(2024);
        Mockito.when(userRepository.findByEmail("admin@del.ac.id")).thenReturn(Optional.empty());
        Mockito.when(researchService.getResearchById(id)).thenReturn(r);
        mockMvc.perform(get("/dashboard/research/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("research/detail"));
    }
}
