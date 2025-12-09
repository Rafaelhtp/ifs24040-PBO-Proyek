package com.rafaelnobel.researchtracker.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@SpringBootTest
class WebMvcConfigTest {
    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    private MessageSource messageSource;

    @Test
    void testLocaleResolverDefaultLocale() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        Locale loc = localeResolver.resolveLocale(req);
        Assertions.assertEquals("en", loc.getLanguage());
    }

    @Test
    void testLocaleChangeInterceptorParamName() {
        Assertions.assertEquals("lang", localeChangeInterceptor.getParamName());
    }

    @Test
    void testMessageSourceLoadsMessages() {
        String msg = messageSource.getMessage("nav.dashboard", null, Locale.ENGLISH);
        Assertions.assertNotNull(msg);
    }
}

