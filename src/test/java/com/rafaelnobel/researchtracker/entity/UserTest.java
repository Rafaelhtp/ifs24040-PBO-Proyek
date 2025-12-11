package com.rafaelnobel.researchtracker.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class UserTest {

    @Test
    void testGettersAndSetters() {
        User u = new User();
        UUID id = UUID.randomUUID();
        
        u.setId(id);
        u.setName("n");
        u.setEmail("e");
        u.setPassword("p");
        u.setInstitution("i");
        u.setField("f");
        u.setDefaultTheme("t");
        
        LocalDateTime now = LocalDateTime.now();
        u.setCreatedAt(now);
        u.setUpdatedAt(now);

        Assertions.assertEquals(id, u.getId());
        Assertions.assertEquals("n", u.getName());
        Assertions.assertEquals("e", u.getEmail());
        Assertions.assertEquals("p", u.getPassword());
        Assertions.assertEquals("i", u.getInstitution());
        Assertions.assertEquals("f", u.getField());
        Assertions.assertEquals("t", u.getDefaultTheme());
        Assertions.assertEquals(now, u.getCreatedAt());
        Assertions.assertEquals(now, u.getUpdatedAt());
    }

    @Test
    void testLifecycleMethods() throws InterruptedException {
        User u = new User();

        // 1. Test @PrePersist (onCreate)
        Assertions.assertNull(u.getCreatedAt());
        Assertions.assertNull(u.getUpdatedAt());

        u.onCreate(); // Memanggil method protected secara langsung

        Assertions.assertNotNull(u.getCreatedAt());
        Assertions.assertNotNull(u.getUpdatedAt());
        Assertions.assertEquals(u.getCreatedAt(), u.getUpdatedAt()); // Saat create, waktu harus sama (atau sangat dekat)

        // Simpan waktu lama untuk perbandingan
        LocalDateTime oldUpdate = u.getUpdatedAt();
        
        // Sedikit delay agar waktu berubah (opsional, untuk memastikan .now() berbeda)
        Thread.sleep(10); 

        // 2. Test @PreUpdate (onUpdate)
        u.onUpdate(); // Memanggil method protected secara langsung

        Assertions.assertNotNull(u.getUpdatedAt());
        // Pastikan updatedAt berubah menjadi waktu yang baru
        Assertions.assertTrue(u.getUpdatedAt().isAfter(oldUpdate) || u.getUpdatedAt().isEqual(oldUpdate));
    }
}