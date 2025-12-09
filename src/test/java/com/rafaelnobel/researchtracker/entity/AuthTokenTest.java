package com.rafaelnobel.researchtracker.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class AuthTokenTest {
    @Test
    void testGettersAndSetters() {
        AuthToken t = new AuthToken();
        UUID id = UUID.randomUUID();
        UUID uid = UUID.randomUUID();
        t.setId(id);
        t.setToken("tok");
        t.setUserId(uid);
        LocalDateTime now = LocalDateTime.now();
        t.setCreatedAt(now);
        Assertions.assertEquals(id, t.getId());
        Assertions.assertEquals("tok", t.getToken());
        Assertions.assertEquals(uid, t.getUserId());
        Assertions.assertEquals(now, t.getCreatedAt());
    }
}

