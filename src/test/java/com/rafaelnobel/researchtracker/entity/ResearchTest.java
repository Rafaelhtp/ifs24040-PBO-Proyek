package com.rafaelnobel.researchtracker.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class ResearchTest {
    @Test
    void testGettersAndSetters() {
        Research r = new Research();
        UUID id = UUID.randomUUID();
        UUID uid = UUID.randomUUID();
        r.setId(id);
        r.setUserId(uid);
        r.setTitle("t");
        r.setResearchTheme("th");
        r.setResearcherName("rn");
        r.setResearcherEmail("re");
        r.setResearcherInstitution("ri");
        r.setTopic("tp");
        r.setDescription("d");
        r.setImpact("i");
        r.setFundCurrency("IDR");
        r.setFundSource("src");
        r.setFundAmount(3.0);
        r.setReleaseYear(2022);
        r.setCoverFilename("c.jpg");
        LocalDateTime now = LocalDateTime.now();
        r.setCreatedAt(now);
        r.setUpdatedAt(now);
        Assertions.assertEquals(id, r.getId());
        Assertions.assertEquals(uid, r.getUserId());
        Assertions.assertEquals("t", r.getTitle());
        Assertions.assertEquals("th", r.getResearchTheme());
        Assertions.assertEquals("rn", r.getResearcherName());
        Assertions.assertEquals("re", r.getResearcherEmail());
        Assertions.assertEquals("ri", r.getResearcherInstitution());
        Assertions.assertEquals("tp", r.getTopic());
        Assertions.assertEquals("d", r.getDescription());
        Assertions.assertEquals("i", r.getImpact());
        Assertions.assertEquals("IDR", r.getFundCurrency());
        Assertions.assertEquals("src", r.getFundSource());
        Assertions.assertEquals(3.0, r.getFundAmount());
        Assertions.assertEquals(2022, r.getReleaseYear());
        Assertions.assertEquals("c.jpg", r.getCoverFilename());
        Assertions.assertEquals(now, r.getCreatedAt());
        Assertions.assertEquals(now, r.getUpdatedAt());
    }
}

