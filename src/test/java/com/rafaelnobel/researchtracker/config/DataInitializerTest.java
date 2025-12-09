package com.rafaelnobel.researchtracker.config;

import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {
    @Test
    void testInitDatabaseCreatesData() throws Exception {
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        ResearchRepository researchRepo = Mockito.mock(ResearchRepository.class);
        Mockito.when(userRepo.findByEmail("admin@del.ac.id")).thenReturn(Optional.empty());
        Mockito.when(researchRepo.count()).thenReturn(0L);
        DataInitializer di = new DataInitializer();
        CommandLineRunner runner = di.initDatabase(userRepo, researchRepo);
        runner.run(new String[]{});
        Mockito.verify(userRepo, Mockito.atLeastOnce()).save(any());
        Mockito.verify(researchRepo, Mockito.atLeastOnce()).save(any());
    }
}

