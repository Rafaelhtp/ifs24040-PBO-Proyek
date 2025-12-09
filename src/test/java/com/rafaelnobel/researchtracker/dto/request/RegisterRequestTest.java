package com.rafaelnobel.researchtracker.dto.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterRequestTest {
    @Test
    void testGettersAndSetters() {
        RegisterRequest r = new RegisterRequest();
        r.setName("n");
        r.setEmail("e");
        r.setPassword("p");
        r.setInstitution("i");
        r.setField("f");
        r.setTheme("t");
        Assertions.assertEquals("n", r.getName());
        Assertions.assertEquals("e", r.getEmail());
        Assertions.assertEquals("p", r.getPassword());
        Assertions.assertEquals("i", r.getInstitution());
        Assertions.assertEquals("f", r.getField());
        Assertions.assertEquals("t", r.getTheme());
    }
}

