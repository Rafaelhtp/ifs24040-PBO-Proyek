package com.rafaelnobel.researchtracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation;

    // 1. Constructor Default (Dipakai Spring Boot saat aplikasi jalan)
    public StorageService() {
        // Panggil constructor kedua dengan path default
        this(Paths.get("src/main/resources/static/uploads"));
    }

    // 2. Constructor Tambahan (Untuk Testing / Dependency Injection)
    // Kita buat public agar Test bisa akses
    public StorageService(Path location) {
        this.rootLocation = location;
        try {
            // Buat folder uploads
            Files.createDirectories(location);
        } catch (IOException e) {
            // INI YANG TADI MERAH. Sekarang bisa kita test!
            throw new RuntimeException("Gagal membuat folder upload!", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Gagal menyimpan file kosong.");
        }

        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(filename)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Tidak bisa menyimpan file di luar folder uploads.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan file.", e);
        }
    }

    public void delete(String filename) {
        if (filename != null) {
            try {
                Path file = rootLocation.resolve(filename);
                Files.deleteIfExists(file);
            } catch (IOException e) {
                // Log error atau abaikan (sesuai logika bisnis Anda)
                // System.err.println("Gagal menghapus file: " + e.getMessage());
            }
        }
    }
}