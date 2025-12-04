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

    // Lokasi folder uploads (sama dengan konfigurasi WebMvcConfig)
    private final Path rootLocation = Paths.get("src/main/resources/static/uploads");

    public StorageService() {
        try {
            // Buat folder uploads otomatis jika belum ada saat aplikasi start
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Gagal membuat folder upload!", e);
        }
    }

    // Fungsi utama: Simpan file dan kembalikan nama filenya
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Gagal menyimpan file kosong.");
        }

        try {
            // Generate nama unik agar tidak bentrok (misal: skripsi.pdf -> UUID_skripsi.pdf)
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            // Bersihkan path file (keamanan)
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(filename)).normalize().toAbsolutePath();

            // Cek apakah file mencoba disimpan di luar folder uploads (serangan path traversal)
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Tidak bisa menyimpan file di luar folder uploads.");
            }

            // Simpan file (Timpa jika ada nama sama)
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return filename; // Kembalikan nama file untuk disimpan di database
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan file.", e);
        }
    }
    
    // Fungsi Hapus File (Dipakai saat menghapus data penelitian)
    public void delete(String filename) {
        try {
            if (filename != null) {
                Path filePath = rootLocation.resolve(filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            System.err.println("Gagal menghapus gambar: " + filename);
        }
    }
}