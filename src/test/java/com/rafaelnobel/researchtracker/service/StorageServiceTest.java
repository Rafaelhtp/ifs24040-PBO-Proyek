package com.rafaelnobel.researchtracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class StorageServiceTest {
    @Test
    void testStoreCreatesFile() throws Exception {
        StorageService s = new StorageService();
        MockMultipartFile f = new MockMultipartFile("file", "a.jpg", "image/jpeg", new byte[]{1,2,3});
        String name = s.store(f);
        Assertions.assertNotNull(name);
        Path p = Paths.get("src/main/resources/static/uploads").resolve(name).toAbsolutePath();
        Assertions.assertTrue(Files.exists(p));
        Files.deleteIfExists(p);
    }
}

