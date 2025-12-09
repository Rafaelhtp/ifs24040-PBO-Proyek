package com.rafaelnobel.researchtracker.repository;

import com.rafaelnobel.researchtracker.entity.Research;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DataJpaTest
class ResearchRepositoryTest {
    @Autowired
    private ResearchRepository researchRepository;

    @Test
    void testFindByUserIdAndDistinctThemes() {
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        Research r1 = new Research();
        r1.setUserId(u1);
        r1.setTitle("t1");
        r1.setResearchTheme("A");
        r1.setFundAmount(1.0);
        r1.setReleaseYear(2020);
        r1.setCreatedAt(LocalDateTime.now());
        r1.setUpdatedAt(LocalDateTime.now());
        Research r2 = new Research();
        r2.setUserId(u1);
        r2.setTitle("t2");
        r2.setResearchTheme("B");
        r2.setFundAmount(2.0);
        r2.setReleaseYear(2021);
        r2.setCreatedAt(LocalDateTime.now());
        r2.setUpdatedAt(LocalDateTime.now());
        Research r3 = new Research();
        r3.setUserId(u2);
        r3.setTitle("t3");
        r3.setResearchTheme("A");
        r3.setFundAmount(3.0);
        r3.setReleaseYear(2022);
        r3.setCreatedAt(LocalDateTime.now());
        r3.setUpdatedAt(LocalDateTime.now());
        researchRepository.save(r1);
        researchRepository.save(r2);
        researchRepository.save(r3);
        List<Research> list = researchRepository.findByUserId(u1);
        Assertions.assertEquals(2, list.size());
        List<String> themes = researchRepository.findDistinctThemesByUserId(u1);
        Assertions.assertTrue(themes.contains("A"));
        Assertions.assertTrue(themes.contains("B"));
        Assertions.assertEquals(2, themes.size());
    }
}

