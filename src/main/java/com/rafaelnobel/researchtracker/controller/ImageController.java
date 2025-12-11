package com.rafaelnobel.researchtracker.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            
            // PERUBAHAN DI SINI: Kita panggil method protected loadResource
            Resource resource = loadResource(file);

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Baris ini akan hijau jika testMalformedUrlException dijalankan
            return ResponseEntity.badRequest().build();
        }
    }

    // METHOD BARU: Protected agar bisa di-override oleh Test Class
    protected Resource loadResource(Path file) throws MalformedURLException {
        return new UrlResource(file.toUri());
    }
}