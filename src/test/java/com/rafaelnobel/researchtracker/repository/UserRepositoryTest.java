package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        User u = new User();
        u.setName("n");
        u.setEmail("e@x.com");
        u.setPassword("p");
        userRepository.save(u);
        Optional<User> got = userRepository.findByEmail("e@x.com");
        Assertions.assertTrue(got.isPresent());
        Assertions.assertEquals("e@x.com", got.get().getEmail());
    }
}

