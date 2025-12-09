package com.rafaelnobel.researchtracker.dto.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginRequestTest {
    @Test
    void testGettersAndSetters() {
        LoginRequest r = new LoginRequest();
        r.setEmail("a@b.com");
        r.setPassword("p");
        Assertions.assertEquals("a@b.com", r.getEmail());
        Assertions.assertEquals("p", r.getPassword());
    }
}

