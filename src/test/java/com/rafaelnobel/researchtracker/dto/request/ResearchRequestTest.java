package com.rafaelnobel.researchtracker.dto.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class ResearchRequestTest {
    @Test
    void testGettersAndSetters() {
        ResearchRequest r = new ResearchRequest();
        r.setTitle("t");
        r.setResearchTheme("th");
        r.setResearcherName("rn");
        r.setResearcherEmail("re");
        r.setResearcherInstitution("ri");
        r.setTopic("tp");
        r.setDescription("d");
        r.setImpact("i");
        r.setFundAmount(1.0);
        r.setReleaseYear(2024);
        r.setFundCurrency("IDR");
        r.setFundSource("src");
        MockMultipartFile file = new MockMultipartFile("cover", "a.jpg", "image/jpeg", new byte[]{1});
        r.setCoverImage(file);
        Assertions.assertEquals("t", r.getTitle());
        Assertions.assertEquals("th", r.getResearchTheme());
        Assertions.assertEquals("rn", r.getResearcherName());
        Assertions.assertEquals("re", r.getResearcherEmail());
        Assertions.assertEquals("ri", r.getResearcherInstitution());
        Assertions.assertEquals("tp", r.getTopic());
        Assertions.assertEquals("d", r.getDescription());
        Assertions.assertEquals("i", r.getImpact());
        Assertions.assertEquals(1.0, r.getFundAmount());
        Assertions.assertEquals(2024, r.getReleaseYear());
        Assertions.assertEquals("IDR", r.getFundCurrency());
        Assertions.assertEquals("src", r.getFundSource());
        Assertions.assertNotNull(r.getCoverImage());
    }
}

