package com.rafaelnobel.researchtracker.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityConfigTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoderMatches() {
        String raw = "secret";
        String encoded = passwordEncoder.encode(raw);
        Assertions.assertTrue(passwordEncoder.matches(raw, encoded));
    }
}

