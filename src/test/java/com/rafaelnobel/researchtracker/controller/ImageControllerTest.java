package com.rafaelnobel.researchtracker.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private Path created;

    @AfterEach
    void cleanup() throws Exception {
        if (created != null && Files.exists(created)) {
            Files.delete(created);
        }
    }

    @Test
    void testServeExistingFile() throws Exception {
        String dir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
        Files.createDirectories(Paths.get(dir));
        created = Paths.get(dir).resolve("test-image.jpg");
        Files.writeString(created, "x");
        mockMvc.perform(get("/uploads/test-image.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    void testServeMissingFile() throws Exception {
        mockMvc.perform(get("/uploads/nope.jpg"))
                .andExpect(status().isNotFound());
    }
}
