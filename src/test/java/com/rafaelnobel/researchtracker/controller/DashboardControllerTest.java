package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import com.rafaelnobel.researchtracker.service.ResearchService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    // =========================================================================
    // BAGIAN 1: LOGIKA BYPASS USER DI DASHBOARD (Lines 30-38)
    // =========================================================================

    @Test
    void testUser_AlreadyInSession() throws Exception {
        // Skenario: User SUDAH login. Skip DB check.
        MockHttpSession session = new MockHttpSession();
        User sessionUser = new User();
        sessionUser.setEmail("session@test.com");
        session.setAttribute("user", sessionUser);

        Mockito.when(researchService.getAllResearch(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("user", sessionUser));

        // Pastikan Repo TIDAK dipanggil (Coverage untuk 'if (user == null)' -> False)
        Mockito.verify(userRepository, Mockito.never()).findByEmail(any());
    }

    @Test
    void testUser_NotInSession_FoundInRepo() throws Exception {
        // Skenario: Session kosong, User ADA di DB.
        User dbUser = new User();
        dbUser.setName("Admin From DB");
        dbUser.setEmail("admin@del.ac.id");

        Mockito.when(userRepository.findByEmail("admin@del.ac.id")).thenReturn(Optional.of(dbUser));
        Mockito.when(researchService.getAllResearchForAdmin()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("user", dbUser)); 
    }

    @Test
    void testUser_NotInSession_NotFoundInRepo_CreateDummy() throws Exception {
        // Skenario: Session kosong, DB kosong -> Buat Dummy.
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(researchService.getAllResearch(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("user", Matchers.notNullValue()))
                .andExpect(request().sessionAttribute("user", Matchers.hasProperty("name", Matchers.equalTo("Pengguna Demo"))));
    }

    // =========================================================================
    // BAGIAN 2: LOGIKA SEARCH FILTER (Lines 47-53)
    // =========================================================================

    @Test
    void testDashboard_QueryNullOrBlank() throws Exception {
        // Meng-cover baris: if (q != null && !q.isBlank()) -> False condition
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(researchService.getAllResearch(any())).thenReturn(Collections.emptyList());

        // 1. q = null
        mockMvc.perform(get("/dashboard")) 
                .andExpect(status().isOk());

        // 2. q = blank string
        mockMvc.perform(get("/dashboard").param("q", "   "))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchFilter_ComplexLogic_FullCoverage() throws Exception {
        // Meng-cover logika OR (||) di dalam stream filter
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Setup Data Presisi:
        
        // 1. Matches Title ("Java")
        Research r1 = new Research(); r1.setTitle("Java Programming"); 
        
        // 2. Matches Name ("Nobel") -> Title mismatch, Name match
        Research r2 = new Research(); r2.setTitle("Other"); r2.setResearcherName("Rafael Nobel"); 

        // 3. Matches Theme ("AI") -> Title & Name mismatch, Theme match
        Research r3 = new Research(); r3.setTitle("Other"); r3.setResearcherName("Joko"); r3.setResearchTheme("AI System"); 

        // 4. Null Checks -> Title/Name/Theme is null
        Research rNull = new Research(); 
        rNull.setTitle(null); rNull.setResearcherName(null); rNull.setResearchTheme(null);

        Mockito.when(researchService.getAllResearch(any())).thenReturn(Arrays.asList(r1, r2, r3, rNull));

        // Test Cari "Java" (Hit Title)
        mockMvc.perform(get("/dashboard").param("q", "Java"))
                .andExpect(model().attribute("researches", Matchers.hasSize(1)));

        // Test Cari "Nobel" (Hit Name)
        mockMvc.perform(get("/dashboard").param("q", "Nobel"))
                .andExpect(model().attribute("researches", Matchers.hasSize(1)));

        // Test Cari "AI" (Hit Theme)
        mockMvc.perform(get("/dashboard").param("q", "AI"))
                .andExpect(model().attribute("researches", Matchers.hasSize(1)));
    }

    // =========================================================================
    // BAGIAN 3: LOGIKA CHART FILTER (Lines 68)
    // =========================================================================

    @Test
    void testChartData_FilterNulls() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        Research rValid = new Research();
        rValid.setFundAmount(1000.0);
        rValid.setReleaseYear(2023);

        Research rNullFund = new Research();
        rNullFund.setFundAmount(null); // Harus difilter (False condition)
        rNullFund.setReleaseYear(2023);

        Research rNullYear = new Research();
        rNullYear.setFundAmount(2000.0);
        rNullYear.setReleaseYear(null); // Harus difilter (False condition)

        Mockito.when(researchService.getAllResearch(any())).thenReturn(Arrays.asList(rValid, rNullFund, rNullYear));

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("researches", Matchers.hasSize(3))) // List utama tetap 3
                .andExpect(model().attribute("yearLabels", Matchers.hasSize(1))); // Data chart cuma 1
    }

    // =========================================================================
    // BAGIAN 4: LOGIKA DETAIL VIEW (Lines 113-120)
    // =========================================================================

    @Test
    void testDashboardDetail_UserAlreadyInSession() throws Exception {
        // Meng-cover baris 113: if (user == null) -> False (User sudah ada)
        UUID id = UUID.randomUUID();
        Research r = new Research(); r.setId(id);
        
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());

        Mockito.when(researchService.getResearchById(id)).thenReturn(r);

        mockMvc.perform(get("/dashboard/research/" + id).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("research/detail"));

        Mockito.verify(userRepository, Mockito.never()).findByEmail(any());
    }

    @Test
    void testDashboardDetail_UserNotInSession() throws Exception {
        // Meng-cover baris 113: if (user == null) -> True (Ambil dari Repo)
        UUID id = UUID.randomUUID();
        Research r = new Research(); r.setId(id);
        User dbUser = new User(); dbUser.setEmail("admin@del.ac.id");

        Mockito.when(userRepository.findByEmail("admin@del.ac.id")).thenReturn(Optional.of(dbUser));
        Mockito.when(researchService.getResearchById(id)).thenReturn(r);

        mockMvc.perform(get("/dashboard/research/" + id))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("user", dbUser));
    }
    
    @Test
    void testDashboardDetail_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(researchService.getResearchById(id)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty()); 

        mockMvc.perform(get("/dashboard/research/" + id))
                .andExpect(status().isOk()) 
                .andExpect(view().name("error"));
    }

    // =========================================================================
    // BAGIAN 5: ADMIN CHECK & REDIRECTS
    // =========================================================================

    @Test
    void testAdminAccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User admin = new User();
        admin.setEmail("admin@del.ac.id"); 
        session.setAttribute("user", admin);

        mockMvc.perform(get("/dashboard").session(session));
        
        Mockito.verify(researchService).getAllResearchForAdmin();
    }

    @Test
    void testRegularUserAccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User regular = new User();
        regular.setId(UUID.randomUUID());
        regular.setEmail("student@del.ac.id"); 
        session.setAttribute("user", regular);

        mockMvc.perform(get("/dashboard").session(session));

        Mockito.verify(researchService).getAllResearch(regular.getId());
    }

    @Test
    void testRedirects() throws Exception {
        mockMvc.perform(get("/researchers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/researchers"));

        mockMvc.perform(get("/researchers/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/researchers/add"));
    }
}