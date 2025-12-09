package com.rafaelnobel.researchtracker.Exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class GlobalExceptionHandlerTest {
    @Test
    void testHandleMaxSizeException() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        String v = h.handleMaxSizeException(new MaxUploadSizeExceededException(1), new RedirectAttributesModelMap());
        Assertions.assertEquals("redirect:/research/add", v);
    }

    @Test
    void testHandleGeneralException() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        ConcurrentModel model = new ConcurrentModel();
        String v = h.handleGeneralException(new RuntimeException("x"), model);
        Assertions.assertEquals("error", v);
        Assertions.assertTrue(model.containsAttribute("errorMessage"));
    }
}

