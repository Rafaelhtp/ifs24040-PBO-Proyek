package com.rafaelnobel.researchtracker.dto.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class ChartDataResponseTest {
    @Test
    void testNoArgsAndReflectionSetters() throws Exception {
        ChartDataResponse c = new ChartDataResponse();
        set(c, "label", "X");
        set(c, "value", 7L);
        Assertions.assertEquals("X", get(c, "label"));
        Assertions.assertEquals(7L, get(c, "value"));
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
