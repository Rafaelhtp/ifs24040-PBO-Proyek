package com.rafaelnobel.researchtracker.config;

import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    // TEST 1: Menghijaukan blok IF (Saat data kosong, harus create)
    @Test
    void testInitDatabase_EmptyDB_CreatesData() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        ResearchRepository researchRepo = mock(ResearchRepository.class);

        // Simulasi kondisi KOSONG
        when(userRepo.findByEmail("admin@del.ac.id")).thenReturn(Optional.empty());
        when(researchRepo.count()).thenReturn(0L);

        DataInitializer di = new DataInitializer();
        CommandLineRunner runner = di.initDatabase(userRepo, researchRepo);
        runner.run(new String[]{});

        // Verifikasi save DIPANGGIL
        verify(userRepo, atLeastOnce()).save(any());
        verify(researchRepo, atLeastOnce()).save(any());
    }

    // TEST 2: Menghijaukan blok ELSE (Saat data ada, harus skip)
    @Test
    void testInitDatabase_PopulatedDB_SkipsCreation() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        ResearchRepository researchRepo = mock(ResearchRepository.class);

        // Simulasi kondisi ADA
        when(userRepo.findByEmail("admin@del.ac.id")).thenReturn(Optional.of(new User()));
        when(researchRepo.count()).thenReturn(10L);

        DataInitializer di = new DataInitializer();
        CommandLineRunner runner = di.initDatabase(userRepo, researchRepo);
        runner.run(new String[]{});

        // Verifikasi save TIDAK DIPANGGIL (Never)
        verify(userRepo, never()).save(any());
        verify(researchRepo, never()).save(any());
    }
}