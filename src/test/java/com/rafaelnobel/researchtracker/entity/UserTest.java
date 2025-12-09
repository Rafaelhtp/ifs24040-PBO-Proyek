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
}

