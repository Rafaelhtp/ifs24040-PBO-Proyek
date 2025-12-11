package com.rafaelnobel.researchtracker.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StorageServiceTest {

    // Pastikan path ini sesuai dengan yang ada di kodingan Service Anda
    private final Path rootLocation = Paths.get("src/main/resources/static/uploads");

    @AfterEach
    void cleanup() {
        // Hapus file/folder sampah sisa testing
        try {
            Files.deleteIfExists(rootLocation.resolve("test-file.jpg"));
            Files.deleteIfExists(rootLocation.resolve("cant-delete-me/child.txt"));
            Files.deleteIfExists(rootLocation.resolve("cant-delete-me"));
        } catch (IOException e) {
            // ignore
        }
    }

    // 1. HAPPY PATH: Simpan file normal
    @Test
    void testStore_Success() throws Exception {
        StorageService service = new StorageService();
        MockMultipartFile file = new MockMultipartFile("file", "test-file.jpg", "image/jpeg", "content".getBytes());
        
        String storedFilename = service.store(file);
        
        assertNotNull(storedFilename);
        Path savedFile = rootLocation.resolve(storedFilename);
        assertTrue(Files.exists(savedFile));
        
        Files.deleteIfExists(savedFile);
    }

    // 2. ERROR PATH: File Kosong (Menghijaukan Baris 31)
    @Test
    void testStore_EmptyFile_ThrowsException() {
        StorageService service = new StorageService();
        // Byte array kosong new byte[0] memicu file.isEmpty() = true
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.store(emptyFile));
        assertTrue(ex.getMessage().contains("file kosong"));
    }

    // 3. ERROR PATH: Serangan Path Traversal (Menghijaukan Baris 44)
    @Test
    void testStore_PathTraversal_ThrowsException() {
        StorageService service = new StorageService();
        // Filename "../" mencoba keluar folder
        MockMultipartFile hackFile = new MockMultipartFile("file", "../hack.txt", "text/plain", "data".getBytes());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.store(hackFile));
        assertTrue(ex.getMessage().contains("di luar folder uploads"));
    }

    // 4. ERROR PATH: IOException saat Copy (Menghijaukan Baris 53 - Catch Block)
    @Test
    void testStore_IoException_ThrowsRuntimeException() throws IOException {
        StorageService service = new StorageService();
        
        // Kita Mock file agar saat getInputStream() dipanggil, dia melempar IOException
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("error.jpg");
        when(mockFile.getInputStream()).thenThrow(new IOException("Disk Error Simulasi"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.store(mockFile));
        assertTrue(ex.getMessage().contains("Gagal menyimpan file"));
    }

    // 5. DELETE PATH: Test Hapus & Null (Menghijaukan Baris 63)
    @Test
    void testDelete_SuccessAndNull() throws IOException {
        StorageService service = new StorageService();

        // A. Delete Normal
        String filename = "todelete.txt";
        Files.createDirectories(rootLocation);
        Path filePath = rootLocation.resolve(filename);
        Files.writeString(filePath, "dummy content");
        
        service.delete(filename);
        assertFalse(Files.exists(filePath)); // Pastikan terhapus

        // B. Delete Null (Pastikan tidak error)
        assertDoesNotThrow(() -> service.delete(null));
    }

    // 6. DELETE PATH: IOException (Menghijaukan Baris 66 - Catch Block)
    @Test
    void testDelete_IoException_CatchBlock() throws IOException {
        StorageService service = new StorageService();
        
        // TRIK: Buat FOLDER (bukan file) yang ada isinya.
        // Files.deleteIfExists() tidak bisa menghapus folder yang ada isinya -> Melempar IOException
        String folderName = "cant-delete-me";
        Path folderPath = rootLocation.resolve(folderName);
        Files.createDirectories(folderPath);
        Files.writeString(folderPath.resolve("child.txt"), "isi folder");

        // Panggil delete pada folder tersebut
        // Method delete() di service kamu menangkap error dan cuma print (tidak throw), jadi assertDoesNotThrow
        assertDoesNotThrow(() -> service.delete(folderName));
        
        // (Catch block di service sekarang sudah tereksekusi dan hijau)
    }
     // 7. CONSTRUCTOR ERROR PATH: Gagal Buat Folder (Menghijaukan Constructor Catch Block)
    @Test
    void testConstructor_FailToCreateDirectory() throws IOException {
        // TRIK: Kita buat FILE biasa, lalu kita suruh Service menganggap itu FOLDER.
        // Files.createDirectories() akan error jika path tujuan sudah ada tapi berupa FILE.
        Path fileBukanFolder = rootLocation.resolve("ini-file-bukan-folder.txt");
        
        // Pastikan parent foldernya ada dulu
        Files.createDirectories(rootLocation); 
        // Buat file dummy
        Files.writeString(fileBukanFolder, "Saya menghalangi pembuatan folder");

        // Panggil Constructor kedua dengan path ke FILE tersebut
        // Ini akan memicu IOException di dalam constructor karena dia mencoba createDirectories di atas sebuah File
        RuntimeException ex = assertThrows(RuntimeException.class, () -> new StorageService(fileBukanFolder));

        assertTrue(ex.getMessage().contains("Gagal membuat folder upload"));
        
        // Bersihkan
        Files.deleteIfExists(fileBukanFolder);
    }
}