package com.rafaelnobel.researchtracker.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Menangani Error Jika File Upload Terlalu Besar (> 5MB)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        // Kirim pesan error ke halaman form
        redirectAttributes.addFlashAttribute("error", "File terlalu besar! Maksimal 5MB.");
        return "redirect:/research/add";
    }

    // 2. Menangani Error Umum Lainnya (NullPointer, Database Error, dll)
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception exc, Model model) {
        exc.printStackTrace(); // Tampilkan error di console untuk debugging
        model.addAttribute("errorMessage", "Terjadi kesalahan: " + exc.getMessage());
        return "error"; // Akan mencari file error.html (opsional)
    }
}