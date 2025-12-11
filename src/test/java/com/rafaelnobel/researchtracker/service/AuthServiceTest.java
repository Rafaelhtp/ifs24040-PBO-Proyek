package com.rafaelnobel.researchtracker.service;

import com.rafaelnobel.researchtracker.dto.request.RegisterRequest;
import com.rafaelnobel.researchtracker.entity.AuthToken;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.TokenRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthService authService;

    // --- 1. Test Register Sukses (Happy Path) ---
    @Test
    void testRegisterUser_Success() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("baru@del.ac.id");
        req.setPassword("123456");
        req.setName("Mahasiswa Baru");
        // Tambahan: Test field optional agar baris 40-42 tereksekusi dengan data
        req.setInstitution("IT Del");
        req.setField("Research");
        req.setDefaultTheme("dark");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        // Mock save agar mengembalikan user yang sama
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = authService.registerUser(req);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("IT Del", result.getInstitution());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // --- 2. Test Register Email Sudah Ada ---
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("lama@del.ac.id");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(new User()));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.registerUser(req);
        });

        Assertions.assertEquals("Email sudah terdaftar!", exception.getMessage());
        // Pastikan TIDAK ada save ke database
        verify(userRepository, never()).save(any(User.class));
    }

    // --- 3.A. Test Register Password Kosong ("") ---
    @Test
    void testRegisterUser_EmptyPassword() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("nopass@del.ac.id");
        req.setPassword(""); // Empty string

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        authService.registerUser(req);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        Assertions.assertEquals("default123", userCaptor.getValue().getPassword());
    }

    // --- 3.B. Test Register Password Null (PENTING untuk 100% Branch Coverage) ---
    @Test
    void testRegisterUser_NullPassword() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("nullpass@del.ac.id");
        req.setPassword(null); // Explicitly null

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        authService.registerUser(req);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        // Harus tetap default123
        Assertions.assertEquals("default123", userCaptor.getValue().getPassword());
    }

    // --- 4. Test Login Sukses + Create Token ---
    @Test
    void testAuthenticate_Success() {
        String email = "valid@del.ac.id";
        String password = "rahasia";

        User dbUser = new User();
        dbUser.setId(UUID.randomUUID());
        dbUser.setEmail(email);
        dbUser.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(dbUser));
        
        // Penting: Mock tokenRepository.save untuk memastikan method createToken berjalan lancar
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = authService.authenticate(email, password);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(email, result.getEmail());
        
        // Verifikasi bahwa tokenRepository.save DIPANGGIL (bukti createToken dijalankan)
        ArgumentCaptor<AuthToken> tokenCaptor = ArgumentCaptor.forClass(AuthToken.class);
        verify(tokenRepository, times(1)).save(tokenCaptor.capture());
        
        // Cek isi token valid
        AuthToken savedToken = tokenCaptor.getValue();
        Assertions.assertNotNull(savedToken.getToken());
        Assertions.assertEquals(dbUser, savedToken.getUser());
    }

    // --- 5. Test Login Gagal (Password Salah) ---
    @Test
    void testAuthenticate_WrongPassword() {
        User dbUser = new User();
        dbUser.setEmail("test@del.ac.id");
        dbUser.setPassword("rahasia");

        when(userRepository.findByEmail("test@del.ac.id")).thenReturn(Optional.of(dbUser));

        User result = authService.authenticate("test@del.ac.id", "salah123");

        Assertions.assertNull(result);
        // Pastikan token TIDAK dibuat
        verify(tokenRepository, never()).save(any(AuthToken.class));
    }
    
    // --- 6. Test Login User Tidak Ditemukan ---
    @Test
    void testAuthenticate_UserNotFound() {
        when(userRepository.findByEmail("ghost@del.ac.id")).thenReturn(Optional.empty());
        
        User result = authService.authenticate("ghost@del.ac.id", "anypass");
        
        Assertions.assertNull(result);
        verify(tokenRepository, never()).save(any(AuthToken.class));
    }
    // --- 7. Test Login dengan Parameter Null (Menghandle Baris 59-60) ---
    @Test
    void testAuthenticate_NullParameters() {
        // Case A: Email null
        User resultEmailNull = authService.authenticate(null, "pass123");
        Assertions.assertNull(resultEmailNull);

        // Case B: Password null
        User resultPassNull = authService.authenticate("valid@del.ac.id", null);
        Assertions.assertNull(resultPassNull);

        // Pastikan Repository TIDAK PERNAH dipanggil (Short-circuit logic)
        // Ini membuktikan baris 59-60 tereksekusi dan langsung return
        verify(userRepository, never()).findByEmail(any());
    }

    // --- 8. Test Login tapi User di Database Password-nya Null (Menghandle Baris 68) ---
    @Test
    void testAuthenticate_DatabaseUserHasNullPassword() {
        // Skenario: User ditemukan, tapi field password di DB null (misal data rusak/migrasi)
        User dbUser = new User();
        dbUser.setEmail("corrupt@del.ac.id");
        dbUser.setPassword(null); // Set password DB null

        when(userRepository.findByEmail("corrupt@del.ac.id")).thenReturn(Optional.of(dbUser));

        // Action: Login dengan password apa saja
        User result = authService.authenticate("corrupt@del.ac.id", "anyPass");

        // Assert: Harus gagal (null) karena cek `user.getPassword() != null` di baris 68 akan false
        Assertions.assertNull(result);
        verify(tokenRepository, never()).save(any(AuthToken.class));
    }
    
    // --- 9. Test Register dengan Theme Fallback (Menghandle Ternary di Baris 46) ---
    @Test
    void testRegisterUser_ThemeFallback() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("theme@del.ac.id");
        req.setPassword("123456");
        // Kita set DefaultTheme NULL, tapi set Theme biasa (jika ada getter getTheme)
        // Atau biarkan keduanya null untuk memastikan tidak error
        req.setDefaultTheme(null); 
        
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        
        authService.registerUser(req);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        
        // Memastikan logika ternary operator berjalan tanpa error meski null
        Assertions.assertNull(userCaptor.getValue().getDefaultTheme());
    }
}