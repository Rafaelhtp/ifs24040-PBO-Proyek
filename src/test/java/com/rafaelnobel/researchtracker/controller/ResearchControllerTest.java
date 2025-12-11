package com.rafaelnobel.researchtracker.controller;

import com.rafaelnobel.researchtracker.dto.request.ResearchRequest;
import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.service.ResearchService;
import com.rafaelnobel.researchtracker.service.StorageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ResearchController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ResearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @MockBean
    private ResearchService researchService;

    // ==========================================
    // 1. TEST LIST RESEARCH (GET /research)
    // ==========================================

    @Test
    void testListResearch_WithoutLogin_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/research"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testListResearch_WithLogin_ShouldSuccess() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/research").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("research/list"));
    }

    // ==========================================
    // 2. TEST ADD FORM & SUBMIT (GET/POST /research/add)
    // ==========================================

    @Test
    void testShowAddForm_WithLogin() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        
        Mockito.when(researchService.getDistinctThemes(user.getId())).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/research/add").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("research/form"));
    }
    
    // Menambahkan test Redirect jika Add Form diakses tanpa login (untuk memastikan coverage 100% pada logic if user == null)
    @Test
    void testShowAddForm_WithoutLogin() throws Exception {
        mockMvc.perform(get("/research/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddResearch_WithFile_ShouldStoreFile() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        MockMultipartFile file = new MockMultipartFile("coverImage", "foto.jpg", "image/jpeg", "content".getBytes());
        Mockito.when(storageService.store(any())).thenReturn("stored-foto.jpg");

        mockMvc.perform(multipart("/research/add")
                        .file(file)
                        .param("title", "Judul")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
        
        Mockito.verify(storageService, Mockito.times(1)).store(any());
    }
    
    @Test
    void testAddResearch_WithoutLogin() throws Exception {
        MockMultipartFile file = new MockMultipartFile("coverImage", "foto.jpg", "image/jpeg", "content".getBytes());
        mockMvc.perform(multipart("/research/add").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddResearch_WithoutFile_ShouldSkipStorage() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        MockMultipartFile emptyFile = new MockMultipartFile("coverImage", "", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/research/add")
                        .file(emptyFile)
                        .param("title", "Judul Tanpa File")
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(storageService, Mockito.never()).store(any());
    }

    // ==========================================
    // 3. TEST DETAIL & DELETE
    // ==========================================

    @Test
    void testDeleteResearch_WithCover() throws Exception {
        UUID id = UUID.randomUUID();
        Research r = new Research(); r.setId(id); r.setCoverFilename("ada.jpg");
        Mockito.when(researchService.getResearchById(id)).thenReturn(r);

        mockMvc.perform(get("/research/delete/" + id))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(storageService, Mockito.times(1)).delete("ada.jpg");
    }

    @Test
    void testDeleteResearch_NoCover() throws Exception {
        UUID id = UUID.randomUUID();
        Research r = new Research(); r.setId(id); r.setCoverFilename(null);
        Mockito.when(researchService.getResearchById(id)).thenReturn(r);

        mockMvc.perform(get("/research/delete/" + id))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(storageService, Mockito.never()).delete(any());
    }

    // --- NEW: DETAIL RESEARCH (Menutup DetailResearch Method) ---

    @Test
    void testDetailResearch_Found() throws Exception {
        UUID id = UUID.randomUUID();
        Research r = new Research();
        r.setId(id);
        r.setTitle("Judul Detail");

        Mockito.when(researchService.getResearchById(id)).thenReturn(r);

        mockMvc.perform(get("/research/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("research/detail"))
                .andExpect(model().attributeExists("research"))
                .andExpect(model().attribute("research", Matchers.hasProperty("title", Matchers.is("Judul Detail"))));
    }

    @Test
    void testDetailResearch_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(researchService.getResearchById(id)).thenReturn(null);

        mockMvc.perform(get("/research/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    // ==========================================
    // 4. TEST RESEARCHERS
    // ==========================================

    @Test
    void testListResearchers_StreamLogic() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        Research r1 = new Research(); r1.setResearcherName("Budi");
        Research r2 = new Research(); r2.setResearcherName("Budi");
        Research r3 = new Research(); r3.setResearcherName(null);
        Research r4 = new Research(); r4.setResearcherName("Ani");

        Mockito.when(researchService.getAllResearch(user.getId())).thenReturn(Arrays.asList(r1, r2, r3, r4));

        mockMvc.perform(get("/research/researchers").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("researcher/list"))
                .andExpect(model().attribute("researchers", Matchers.hasSize(2)))
                .andExpect(model().attribute("researchers", Matchers.containsInAnyOrder("Budi", "Ani")));
    }
    
    @Test
    void testListResearchers_NotLoggedIn() throws Exception {
        mockMvc.perform(get("/research/researchers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddResearcherForm_WithLogin() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        
        Mockito.when(researchService.getDistinctThemes(user.getId())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/research/researchers/add").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("researcher/form"));
    }
    
    @Test
    void testAddResearcherForm_NotLoggedIn() throws Exception {
        mockMvc.perform(get("/research/researchers/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddResearcher_WithFundAmount() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(post("/research/researchers/add")
                        .param("researcherName", "Dosen Sultan")
                        .param("fundAmount", "5000000")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/researchers")); 

        Mockito.verify(researchService).createResearch(
            Mockito.argThat(r -> r.getFundAmount() == 5000000.0), 
            eq(user.getId())
        );
    }

    @Test
    void testAddResearcher_NullFundAmount() throws Exception {
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(post("/research/researchers/add")
                        .param("researcherName", "Dosen Baru")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/researchers"));
        
        Mockito.verify(researchService).createResearch(
            Mockito.argThat(r -> r.getFundAmount() == 0.0), 
            eq(user.getId())
        );
    }

    // ==========================================
    // 5. TEST EDIT FORM & SUBMIT (NEW - MENUTUP BAGIAN MERAH)
    // ==========================================

    @Test
    void testEditForm_NotLoggedIn() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/research/edit/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testEditForm_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(); user.setId(userId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        UUID researchId = UUID.randomUUID();
        Research existingResearch = new Research();
        existingResearch.setId(researchId);
        existingResearch.setTitle("Judul Lama");
        existingResearch.setResearchTheme("Tema Lama");
        existingResearch.setFundAmount(1000.0);

        Mockito.when(researchService.getResearchById(researchId)).thenReturn(existingResearch);
        Mockito.when(researchService.getDistinctThemes(userId)).thenReturn(Arrays.asList("Tema A", "Tema B"));

        // Memastikan form terisi dengan data lama (req.setTitle, dll di controller)
        mockMvc.perform(get("/research/edit/" + researchId).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("research/form"))
                .andExpect(model().attribute("editId", researchId))
                .andExpect(model().attribute("researchRequest", Matchers.hasProperty("title", Matchers.is("Judul Lama"))))
                .andExpect(model().attribute("researchRequest", Matchers.hasProperty("researchTheme", Matchers.is("Tema Lama"))));
    }

    @Test
    void testEditSubmit_NotLoggedIn() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(multipart("/research/edit/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testEditSubmit_WithFile_ShouldUpdateAndStore() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(); user.setId(userId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        UUID researchId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("coverImage", "baru.jpg", "image/jpeg", "data".getBytes());
        
        Mockito.when(storageService.store(any())).thenReturn("stored-baru.jpg");

        mockMvc.perform(multipart("/research/edit/" + researchId)
                        .file(file)
                        .param("title", "Judul Revisi")
                        .param("researchTheme", "Tema Baru")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/" + researchId));

        // Verifikasi Store dipanggil
        Mockito.verify(storageService, Mockito.times(1)).store(any());

        // Verifikasi updateResearch dipanggil dengan object yang benar
        ArgumentCaptor<Research> researchCaptor = ArgumentCaptor.forClass(Research.class);
        Mockito.verify(researchService).updateResearch(eq(researchId), researchCaptor.capture());
        
        Research captured = researchCaptor.getValue();
        assertEquals("Judul Revisi", captured.getTitle());
        assertEquals("stored-baru.jpg", captured.getCoverFilename());
    }

    @Test
    void testEditSubmit_NoFile_ShouldUpdateWithoutStore() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(); user.setId(userId);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        UUID researchId = UUID.randomUUID();
        MockMultipartFile emptyFile = new MockMultipartFile("coverImage", "", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/research/edit/" + researchId)
                        .file(emptyFile)
                        .param("title", "Judul Revisi No File")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/" + researchId));

        // Verifikasi Store TIDAK dipanggil
        Mockito.verify(storageService, Mockito.never()).store(any());

        // Verifikasi updateResearch tetap dipanggil
        ArgumentCaptor<Research> researchCaptor = ArgumentCaptor.forClass(Research.class);
        Mockito.verify(researchService).updateResearch(eq(researchId), researchCaptor.capture());
        
        assertEquals("Judul Revisi No File", researchCaptor.getValue().getTitle());
    }
    @Test
    void testAddResearcher_Submit_NotLoggedIn() throws Exception {
        mockMvc.perform(post("/research/researchers/add")
                        .param("researcherName", "Researcher Ilegal")
                        .param("fundAmount", "1000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        
        // Pastikan service tidak pernah dipanggil
        Mockito.verify(researchService, Mockito.never()).createResearch(any(), any());
    }
    @Test
    void testEditSubmit_FileParamIsNull_ShouldSkipStorage() throws Exception {
        // Setup User Login
        User user = new User(); user.setId(UUID.randomUUID());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        UUID researchId = UUID.randomUUID();
        
        // Kita tidak perlu mock getResearchById karena di controller logic-nya langsung 'new Research()'
        // lalu set properties. Tapi service update dipanggil.
        
        // TRIK UTAMA: Gunakan multipart() TAPI JANGAN panggil .file()
        // Ini akan membuat parameter 'MultipartFile file' di controller bernilai NULL
        mockMvc.perform(multipart("/research/edit/" + researchId)
                        .param("title", "Judul Edit Tanpa File Param")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/research/" + researchId));

        // Verifikasi:
        // 1. StorageService TIDAK BOLEH dipanggil (karena file null)
        Mockito.verify(storageService, Mockito.never()).store(any());

        // 2. Update Research TETAP dipanggil
        Mockito.verify(researchService, Mockito.times(1)).updateResearch(eq(researchId), any(Research.class));
    }
}