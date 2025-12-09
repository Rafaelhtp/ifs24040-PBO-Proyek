package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void testFindAndDeleteByToken() {
        AuthToken t = new AuthToken();
        t.setToken("abc");
        t.setUserId(UUID.randomUUID());
        t.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(t);
        Optional<AuthToken> got = tokenRepository.findByToken("abc");
        Assertions.assertTrue(got.isPresent());
        tokenRepository.deleteByToken("abc");
        Assertions.assertTrue(tokenRepository.findByToken("abc").isEmpty());
    }
}

