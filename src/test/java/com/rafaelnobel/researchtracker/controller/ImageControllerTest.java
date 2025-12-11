package com.rafaelnobel.researchtracker.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Path createdFile;

    @AfterEach
    void cleanup() throws Exception {
        if (createdFile != null && Files.exists(createdFile)) {
            Files.delete(createdFile);
        }
    }

    // 1. Happy Path (Exists = True)
    @Test
    void testServeExistingFile() throws Exception {
        String dir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
        Files.createDirectories(Paths.get(dir));
        createdFile = Paths.get(dir).resolve("test-image.jpg");
        Files.writeString(createdFile, "dummy-content");

        mockMvc.perform(get("/uploads/test-image.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    // 2. Not Found Path (Exists = False, Readable = False)
    @Test
    void testServeMissingFile() throws Exception {
        mockMvc.perform(get("/uploads/tidak-ada.jpg"))
                .andExpect(status().isNotFound());
    }

    // 3. PENGHIJAU CATCH BLOCK (MalformedURLException)
    @Test
    void testMalformedUrlException() {
        // Kita override method loadResource biar error
        ImageController faultyController = new ImageController() {
            @Override
            protected Resource loadResource(Path file) throws MalformedURLException {
                throw new MalformedURLException("Test Error");
            }
        };
        // Panggil langsung
        ResponseEntity<Resource> response = faultyController.serveFile("error.jpg");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // 4. PENGHIJAU LOGIKA OR (Exists=False TAPI Readable=True)
    // Ini yang bikin baris 'if (exists || readable)' jadi hijau penuh
    @Test
    void testResourceNotExistsButReadable() {
        // Mock resource
        Resource mockResource = Mockito.mock(Resource.class);
        Mockito.when(mockResource.exists()).thenReturn(false);    // Kondisi 1: Gagal
        Mockito.when(mockResource.isReadable()).thenReturn(true); // Kondisi 2: Sukses

        // Inject mock resource ke controller
        ImageController weirdController = new ImageController() {
            @Override
            protected Resource loadResource(Path file) {
                return mockResource;
            }
        };

        // Execute
        ResponseEntity<Resource> response = weirdController.serveFile("ghost.jpg");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}