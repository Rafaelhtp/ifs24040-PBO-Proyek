package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.dto.request.RegisterRequest;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.TokenRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterUser_Success() {
        // Siapkan data register
        RegisterRequest req = new RegisterRequest();
        req.setEmail("baru@del.ac.id");
        req.setPassword("123456");
        req.setName("Mahasiswa Baru");

        // Anggap email belum terdaftar (return empty)
        Mockito.when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        // Jalankan register
        authService.registerUser(req);

        // Pastikan userRepository.save dipanggil 1 kali
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        System.out.println("✅ Test Register User: SUKSES");
    }

    @Test
    void testAuthenticate_WrongPassword_ShouldFail() {
        // Siapkan user di database
        User dbUser = new User();
        dbUser.setEmail("test@del.ac.id");
        dbUser.setPassword("rahasia");

        Mockito.when(userRepository.findByEmail("test@del.ac.id")).thenReturn(Optional.of(dbUser));

        // Coba login dengan password SALAH
        User result = authService.authenticate("test@del.ac.id", "salah123");

        // Harusnya null (gagal)
        Assertions.assertNull(result);
        System.out.println("✅ Test Login Gagal: SUKSES (Sistem menolak password salah)");
    }
}