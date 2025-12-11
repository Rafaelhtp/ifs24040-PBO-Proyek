package com.rafaelnobel.researchtracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResearchTrackerApplicationTest {

    @Test
    void contextLoads() {
        // Test ini memastikan ApplicationContext dapat dimuat dengan sukses
        // Ini biasanya meng-cover inisialisasi kelas, tetapi tidak selalu meng-cover method main()
    }

    @Test
    void testMain() {
        // Memanggil method main secara eksplisit untuk menaikkan coverage menjadi 100%
        ResearchTrackerApplication.main(new String[] {});
    }

}