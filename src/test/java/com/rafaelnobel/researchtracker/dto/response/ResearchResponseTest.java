package com.rafaelnobel.researchtracker.dto.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

class ResearchResponseTest {
    @Test
    void testFieldsAssignableViaReflection() throws Exception {
        ResearchResponse r = new ResearchResponse();
        UUID id = UUID.randomUUID();
        UUID uid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        set(r, "id", id);
        set(r, "title", "t");
        set(r, "researchTheme", "th");
        set(r, "fundAmount", 2.0);
        set(r, "releaseYear", 2023);
        set(r, "coverFilename", "f.jpg");
        set(r, "userId", uid);
        set(r, "createdAt", now);
        set(r, "updatedAt", now);
        Assertions.assertEquals(id, get(r, "id"));
        Assertions.assertEquals("t", get(r, "title"));
        Assertions.assertEquals("th", get(r, "researchTheme"));
        Assertions.assertEquals(2.0, get(r, "fundAmount"));
        Assertions.assertEquals(2023, get(r, "releaseYear"));
        Assertions.assertEquals("f.jpg", get(r, "coverFilename"));
        Assertions.assertEquals(uid, get(r, "userId"));
        Assertions.assertEquals(now, get(r, "createdAt"));
        Assertions.assertEquals(now, get(r, "updatedAt"));
    }

    private void set(Object o, String field, Object value) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }

    private Object get(Object o, String field) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.get(o);
    }
}
