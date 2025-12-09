package com.rafaelnobel.researchtracker.config;

import com.rafaelnobel.researchtracker.entity.Research;
import com.rafaelnobel.researchtracker.entity.User;
import com.rafaelnobel.researchtracker.repository.ResearchRepository;
import com.rafaelnobel.researchtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, ResearchRepository researchRepo) {
        return args -> {
            // 1. Buat User Admin
            User admin = userRepo.findByEmail("admin@del.ac.id").orElse(null);
            if (admin == null) {
                admin = new User();
                admin.setName("Admin Dosen");
                admin.setEmail("admin@del.ac.id");
                admin.setPassword("admin123");
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                userRepo.save(admin);
                System.out.println(">>> User Admin Siap: admin@del.ac.id");
            }

            // 2. Isi Data Penelitian (Internal + Eksternal)
            if (researchRepo.count() == 0) {
                System.out.println(">>> Mengisi Database: Data Internal + 10 Data Eksternal/Tahun...");

                // ==================== TAHUN 2019 (BERT, Climate Change) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Ekstraksi Frasa Domain Agrikultur dalam Sistem Temu Balik Informasi (Togu Novriansyah Turnip)", "INFOKOM", 2019, 3300000.0,
                    "Pengembangan algoritma NLP untuk mengidentifikasi istilah pertanian secara otomatis.", "Mempercepat pencarian literatur pertanian bagi peneliti lokal.");
                createResearch(researchRepo, admin, "Studi Sistem Pengendali Temperatur pada Makanan Fermentasi: Tempe (Indra Hartarto Tambunan)", "TTG", 2019, 7500000.0,
                    "Perancangan alat kontrol suhu otomatis untuk menjaga kualitas fermentasi tempe.", "Mengurangi kegagalan produksi UMKM tempe akibat cuaca.");
                
                // --- EKSTERNAL (Google Scholar / Kampus Lain) ---
                createResearch(researchRepo, admin, "BERT: Pre-training of Deep Bidirectional Transformers for Language Understanding (Google AI)", "INFOKOM", 2019, 500000000.0,
                    "Model bahasa revolusioner yang memahami konteks kata dari dua arah (kiri-kanan).", "Menjadi standar baru industri untuk mesin pencari dan chatbot.");
                createResearch(researchRepo, admin, "Global Warming of 1.5°C Special Report (IPCC/Global)", "Lingkungan", 2019, 2500000000.0,
                    "Laporan komprehensif dampak pemanasan global di atas 1.5 derajat.", "Dasar kebijakan iklim internasional (Paris Agreement).");
                createResearch(researchRepo, admin, "Deep Learning for Electronic Health Records (Stanford University)", "Kesehatan", 2019, 750000000.0,
                    "Prediksi risiko kematian pasien menggunakan data rekam medis elektronik.", "Meningkatkan akurasi triase di rumah sakit modern.");
                createResearch(researchRepo, admin, "The Age of Surveillance Capitalism (Harvard University)", "Sosial Humaniora", 2019, 150000000.0,
                    "Analisis ekonomi politik terhadap perusahaan teknologi besar.", "Meningkatkan kesadaran global tentang privasi data.");
                createResearch(researchRepo, admin, "Perancangan Wisata Edukasi Geopark Ciletuh (ITB)", "Pariwisata", 2019, 120000000.0,
                    "Masterplan pengembangan kawasan wisata berbasis konservasi geologi.", "Meningkatkan ekonomi lokal Sukabumi melalui pariwisata berkelanjutan.");
                createResearch(researchRepo, admin, "Quantum Supremacy Using a Programmable Superconducting Processor (Google)", "INFOKOM", 2019, 1500000000.0,
                    "Demonstrasi komputer kuantum menyelesaikan tugas yang mustahil bagi komputer klasik.", "Tonggak sejarah komputasi masa depan.");
                createResearch(researchRepo, admin, "Reusable Rocket Technology for Low Earth Orbit (SpaceX/NASA)", "TTG", 2019, 3000000000.0,
                    "Pengembangan roket yang dapat mendarat kembali secara vertikal.", "Menurunkan biaya peluncuran satelit hingga 70%.");
                createResearch(researchRepo, admin, "Analisis Kestabilan Lereng Tol Trans Jawa (UGM)", "Teknik Sipil", 2019, 200000000.0,
                    "Mitigasi longsor pada infrastruktur strategis nasional.", "Menjamin keselamatan pengguna jalan tol.");
                createResearch(researchRepo, admin, "Crispr-Cas9 Gene Editing for Sickle Cell Disease (Vertex Pharm)", "Kesehatan", 2019, 900000000.0,
                    "Uji klinis pengeditan gen untuk menyembuhkan penyakit darah genetik.", "Harapan baru penyembuhan penyakit turunan.");
                createResearch(researchRepo, admin, "Smart City Implementation Framework for Jakarta (Universitas Indonesia)", "Tata Kota", 2019, 300000000.0,
                    "Integrasi IoT untuk manajemen banjir dan kemacetan Jakarta.", "Dasar aplikasi Jakarta Kini (JAKI).");

                // ==================== TAHUN 2020 (COVID-19 Era) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Deteksi Covid-19 berdasarkan Citra X-Ray Dada dengan Deep Learning (Arlinta Christy Barus)", "INFOKOM", 2020, 2466936.0,
                    "Model AI untuk screening awal Covid-19 melalui foto rontgen dada.", "Alat bantu diagnosa cepat di fasilitas kesehatan terbatas.");
                createResearch(researchRepo, admin, "Warehousing Tourism Information From Social Media (Mario E. S. Simaremare)", "INFOKOM", 2020, 7010000.0,
                    "Big data warehouse pariwisata dari media sosial.", "Dashboard intelijen bisnis untuk Dinas Pariwisata.");

                // --- EKSTERNAL ---
                createResearch(researchRepo, admin, "Clinical features of patients infected with 2019 novel coronavirus (Wuhan/Lancet)", "Kesehatan", 2020, 0.0,
                    "Studi klinis pertama karakteristik pasien COVID-19.", "Pedoman medis global penanganan awal pandemi.");
                createResearch(researchRepo, admin, "GPT-3: Language Models are Few-Shot Learners (OpenAI)", "INFOKOM", 2020, 1000000000.0,
                    "Model bahasa AI generasi ke-3 dengan 175 miliar parameter.", "Memicu revolusi AI generatif teks.");
                createResearch(researchRepo, admin, "mRNA Vaccine Development for SARS-CoV-2 (BioNTech/Pfizer)", "Kesehatan", 2020, 5000000000.0,
                    "Pengembangan vaksin berbasis Messenger RNA tercepat dalam sejarah.", "Menyelamatkan jutaan nyawa dan mengakhiri fase akut pandemi.");
                createResearch(researchRepo, admin, "Zoom Fatigue: Implications for Leadership (Harvard Business Review)", "Psikologi", 2020, 50000000.0,
                    "Dampak psikologis rapat virtual berkepanjangan.", "Mengubah kebijakan kerja jarak jauh (WFH) perusahaan global.");
                createResearch(researchRepo, admin, "AlphaFold 2: Protein Structure Prediction (DeepMind)", "Bioinformatika", 2020, 800000000.0,
                    "AI yang memecahkan masalah 'protein folding' yang telah bertahan 50 tahun.", "Percepatan penemuan obat baru secara drastis.");
                createResearch(researchRepo, admin, "Desain Ventilator Portable Low-Cost (ITB/UI)", "TTG", 2020, 1000000000.0,
                    "Ventilator darurat buatan dalam negeri untuk pasien COVID-19.", "Kemandirian alat kesehatan Indonesia saat krisis.");
                createResearch(researchRepo, admin, "Impact of Lockdown on Global CO2 Emissions (Nature)", "Lingkungan", 2020, 100000000.0,
                    "Analisis penurunan emisi karbon selama lockdown global.", "Bukti empiris dampak aktivitas manusia terhadap iklim.");
                createResearch(researchRepo, admin, "Remote Learning Effectiveness in Developing Countries (World Bank)", "Pendidikan", 2020, 500000000.0,
                    "Evaluasi kesenjangan digital dalam pendidikan selama pandemi.", "Strategi pemerataan akses internet sekolah.");
                createResearch(researchRepo, admin, "Supply Chain Resilience Strategy (MIT)", "Ekonomi", 2020, 200000000.0,
                    "Strategi rantai pasok tangguh menghadapi disrupsi global.", "Mengubah model Just-in-Time menjadi Just-in-Case.");
                createResearch(researchRepo, admin, "Pengembangan Vaksin Merah Putih (Eijkman Institute)", "Kesehatan", 2020, 5000000000.0,
                    "Inisiatif vaksin COVID-19 berbasis strain virus lokal Indonesia.", "Upaya kedaulatan kesehatan nasional.");

                // ==================== TAHUN 2021 (Metaverse, NFT) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Piranti Cerdas Penghasil Motif Tenun Nusantara (Albert Sagala)", "INFOKOM", 2021, 1403220000.0,
                    "AI Generatif untuk menciptakan variasi motif tenun baru.", "Melestarikan budaya sekaligus inovasi desain fashion etnik.");
                createResearch(researchRepo, admin, "Pontryagin Minimum Principle untuk Kontrol Optimal Covid-19 (Sony Adhi Susanto)", "INFOKOM", 2021, 9920000.0,
                    "Model matematika untuk kebijakan PPKM optimal.", "Dasar ilmiah penyeimbangan kesehatan dan ekonomi.");

                // --- EKSTERNAL ---
                createResearch(researchRepo, admin, "The Metaverse: And How It Will Revolutionize Everything (Matthew Ball)", "INFOKOM", 2021, 200000000.0,
                    "Kerangka kerja konseptual internet spasial masa depan.", "Memicu investasi besar-besaran perusahaan teknologi ke VR/AR.");
                createResearch(researchRepo, admin, "Non-Fungible Tokens (NFTs) Market Analysis (Chainalysis)", "Ekonomi", 2021, 100000000.0,
                    "Analisis tren ekonomi aset digital berbasis blockchain.", "Memahami spekulasi dan utilitas kepemilikan digital.");
                createResearch(researchRepo, admin, "DALL-E: Creating Images from Text (OpenAI)", "INFOKOM", 2021, 800000000.0,
                    "AI yang mampu menggambar apapun dari instruksi teks.", "Awal mula gelombang AI Art Generator.");
                createResearch(researchRepo, admin, "CRISPR-Cas9 Genome Editing in vivo (Intellia)", "Kesehatan", 2021, 600000000.0,
                    "Penyuntikan langsung CRISPR ke tubuh pasien untuk perbaikan gen.", "Terobosan pengobatan penyakit genetik tanpa transplantasi.");
                createResearch(researchRepo, admin, "Perancangan Kereta Cepat Jakarta-Bandung (KCIC/ITB)", "Transportasi", 2021, 5000000000.0,
                    "Studi teknis operasional kereta kecepatan tinggi pertama di ASEAN.", "Modernisasi transportasi massal Indonesia.");
                createResearch(researchRepo, admin, "Mars Helicopter Ingenuity Flight (NASA)", "Dirgantara", 2021, 1200000000.0,
                    "Demonstrasi penerbangan bertenaga pertama di planet lain.", "Membuka era eksplorasi udara di Mars.");
                createResearch(researchRepo, admin, "Studi Potensi Energi Baru Terbarukan (EBT) Sumba (IESR)", "Energi", 2021, 300000000.0,
                    "Peta jalan pulau Sumba sebagai ikon energi terbarukan 100%.", "Model transisi energi untuk pulau-pulau kecil.");
                createResearch(researchRepo, admin, "Algoritma TikTok: Analisis Dampak Adiksi (Univ. of Chicago)", "Sosial", 2021, 150000000.0,
                    "Bagaimana algoritma rekomendasi video pendek mempengaruhi atensi otak.", "Dasar regulasi konten digital untuk anak.");
                createResearch(researchRepo, admin, "Penerapan 5G untuk Industri 4.0 (Telkom University)", "INFOKOM", 2021, 400000000.0,
                    "Uji coba jaringan privat 5G untuk otomatisasi pabrik.", "Meningkatkan efisiensi manufaktur lokal.");
                createResearch(researchRepo, admin, "Varian Delta COVID-19: Transmisi dan Keparahan (Imperial College)", "Kesehatan", 2021, 200000000.0,
                    "Epidemiologi varian virus yang sangat menular.", "Dasar pengetatan protokol kesehatan global.");

                // ==================== TAHUN 2022 (Generative AI Boom) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Pengembangan Aplikasi Belajar Bahasa Inggris NOVO AI (Verawaty Situmorang)", "INFOKOM", 2022, 317877000.0,
                    "Aplikasi belajar bahasa dengan koreksi pengucapan otomatis (ASR).", "Meningkatkan kemampuan bahasa asing mahasiswa daerah.");
                createResearch(researchRepo, admin, "Analisis Potensi Wisata Olahraga Danau Toba (Tegar Arifin Prasetyo)", "Ekonomi", 2022, 10000000.0,
                    "Pemetaan spot olahraga air untuk pariwisata internasional (F1H2O).", "Diversifikasi produk wisata prioritas nasional.");

                // --- EKSTERNAL ---
                createResearch(researchRepo, admin, "ChatGPT: Optimizing Language Models for Dialogue (OpenAI)", "INFOKOM", 2022, 2000000000.0,
                    "Chatbot AI yang mampu berdialog luwes dan mengerjakan tugas kompleks.", "Produk konsumen dengan pertumbuhan tercepat dalam sejarah.");
                createResearch(researchRepo, admin, "Fusion Energy Breakthrough (Lawrence Livermore Lab)", "Fisika", 2022, 5000000000.0,
                    "Pencapaian 'Net Energy Gain' pertama dalam reaksi fusi nuklir.", "Langkah besar menuju energi bersih tak terbatas.");
                createResearch(researchRepo, admin, "Dampak Perang Ukraina terhadap Ketahanan Pangan (FAO)", "Ekonomi", 2022, 300000000.0,
                    "Analisis gangguan rantai pasok gandum dan pupuk global.", "Strategi mitigasi krisis pangan di negara berkembang.");
                createResearch(researchRepo, admin, "Midjourney: AI Art Generation (Midjourney Lab)", "Seni & Desain", 2022, 0.0,
                    "Platform teks-ke-gambar dengan kualitas artistik tinggi.", "Disrupsi industri ilustrasi dan konsep seni.");
                createResearch(researchRepo, admin, "Presidensi G20 Indonesia: Transisi Energi (Kemenkeu/UI)", "Kebijakan Publik", 2022, 500000000.0,
                    "Perumusan mekanisme transisi energi (Energy Transition Mechanism).", "Komitmen pendanaan pensiun dini PLTU batubara.");
                createResearch(researchRepo, admin, "James Webb Space Telescope First Images (NASA/ESA)", "Astronomi", 2022, 10000000000.0,
                    "Observasi inframerah terdalam dari alam semesta awal.", "Mengubah pemahaman tentang pembentukan galaksi.");
                createResearch(researchRepo, admin, "Ethereum Merge: Proof of Work to Proof of Stake (Ethereum Foundation)", "INFOKOM", 2022, 0.0,
                    "Migrasi mekanisme konsensus blockchain terbesar kedua dunia.", "Mengurangi konsumsi energi jaringan hingga 99.9%.");
                createResearch(researchRepo, admin, "Studi Genomik Manusia Purba Indonesia (Eijkman/Griffith)", "Arkeologi", 2022, 400000000.0,
                    "Analisis DNA fosil 'Besse' di Sulawesi.", "Revisi sejarah migrasi manusia di Asia Tenggara.");
                createResearch(researchRepo, admin, "Kendaraan Listrik Otonom Level 4 (Waymo)", "Otomotif", 2022, 3000000000.0,
                    "Operasional taksi tanpa supir sepenuhnya di San Francisco.", "Masa depan transportasi perkotaan.");
                createResearch(researchRepo, admin, "Pengolahan Nikel HPAL untuk Baterai EV (ITB/Industri)", "Metalurgi", 2022, 800000000.0,
                    "Teknologi pemurnian nikel kadar rendah untuk bahan baterai.", "Mendukung Indonesia sebagai pusat baterai dunia.");

                // ==================== TAHUN 2023 (LLM War, Climate) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Pengembangan Metode Pengenalan Wajah JST (Roy Deddy Hasiholan)", "INFOKOM", 2023, 14000000.0,
                    "Sistem absensi biometrik wajah akurasi tinggi.", "Sistem keamanan kampus yang handal.");
                createResearch(researchRepo, admin, "Implementasi Fuzzy AHP-TOPSIS Pemilihan Supplier (Albert Sagala)", "INFOKOM", 2023, 10000000.0,
                    "Sistem pendukung keputusan pengadaan barang.", "Efisiensi anggaran operasional institusi.");

                // --- EKSTERNAL ---
                createResearch(researchRepo, admin, "GPT-4 Technical Report (OpenAI)", "INFOKOM", 2023, 3000000000.0,
                    "Model multimodal yang lulus ujian bar pengacara dan olimpiade biologi.", "Standar emas baru kecerdasan buatan.");
                createResearch(researchRepo, admin, "Room-Temperature Superconductor Claims (LK-99) Analysis (Max Planck)", "Fisika", 2023, 100000000.0,
                    "Verifikasi klaim material superkonduktor suhu ruang.", "Pelajaran penting tentang integritas dan replikasi sains.");
                createResearch(researchRepo, admin, "El Nino 2023 Impact on Southeast Asia Agriculture (NUS)", "Pertanian", 2023, 200000000.0,
                    "Prediksi gagal panen padi akibat kekeringan ekstrem.", "Strategi impor dan stok pangan nasional.");
                createResearch(researchRepo, admin, "6G Wireless Communication Vision (Samsung/Nokia)", "INFOKOM", 2023, 1000000000.0,
                    "Konsep jaringan seluler terahertz dan integrasi satelit.", "Persiapan infrastruktur telekomunikasi 2030.");
                createResearch(researchRepo, admin, "Carbon Capture and Storage (CCS) di Lapangan Tangguh (BP/ITB)", "Lingkungan", 2023, 4000000000.0,
                    "Proyek penangkapan emisi karbon terbesar di Indonesia.", "Solusi dekarbonisasi industri migas.");
                createResearch(researchRepo, admin, "Obat Penurun Berat Badan GLP-1 (Ozempic/Wegovy) (Novo Nordisk)", "Kesehatan", 2023, 2000000000.0,
                    "Dampak agonis reseptor GLP-1 terhadap obesitas global.", "Pergeseran paradigma penanganan obesitas medis.");
                createResearch(researchRepo, admin, "AI Ethics and Alignment Problem (Anthropic)", "Filosofi/AI", 2023, 500000000.0,
                    "Metode 'Constitutional AI' agar kecerdasan buatan tetap aman.", "Regulasi keamanan pengembangan AI supercerdas.");
                createResearch(researchRepo, admin, "Kereta Otonom IKN Nusantara (Kemenhub/ITS)", "Transportasi", 2023, 300000000.0,
                    "Konsep transportasi cerdas tanpa awak di Ibu Kota Baru.", "Visi kota masa depan ramah lingkungan.");
                createResearch(researchRepo, admin, "Solid-State Battery for EV (Toyota/Panasonic)", "Energi", 2023, 2000000000.0,
                    "Baterai mobil listrik dengan jarak tempuh 1200km.", "Mengatasi kecemasan jarak tempuh (range anxiety) konsumen.");
                createResearch(researchRepo, admin, "Generative AI in Coding: GitHub Copilot Impact (Microsoft)", "INFOKOM", 2023, 100000000.0,
                    "Studi peningkatan produktivitas programmer sebesar 55%.", "Transformasi cara kerja pengembangan perangkat lunak.");

                // ==================== TAHUN 2024 (AI Integration) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Deteksi Objek Menggunakan Region Based CNN (Deni Parlindungan)", "INFOKOM", 2024, 17500000.0,
                    "Peningkatan akurasi deteksi objek kecil pada CCTV.", "Aplikasi pengawasan keamanan cerdas.");
                createResearch(researchRepo, admin, "The Penetration of Generative AI in Higher Education (Tahan HJ Sihombing)", "Pendidikan", 2024, 10000000.0,
                    "Survei penggunaan ChatGPT di kalangan mahasiswa Toba.", "Pedoman etika akademik AI kampus.");

                // --- EKSTERNAL ---
                createResearch(researchRepo, admin, "Sora: Text-to-Video Generation (OpenAI)", "INFOKOM", 2024, 1000000000.0,
                    "Model AI yang membuat video realistis berdurasi 1 menit.", "Revolusi industri perfilman dan pembuatan konten.");
                createResearch(researchRepo, admin, "Neuralink Human Trials (Neuralink)", "Biomedis", 2024, 1000000000.0,
                    "Implan chip otak pertama yang memungkinkan kontrol telepati komputer.", "Harapan bagi penderita kelumpuhan total.");
                createResearch(researchRepo, admin, "Pemilu 2024: Analisis Sentimen Big Data (UI/Drone Emprit)", "Politik", 2024, 200000000.0,
                    "Pemetaan polarisasi pemilih di media sosial.", "Memahami dinamika demokrasi digital Indonesia.");
                createResearch(researchRepo, admin, "Starship Orbital Flight Success (SpaceX)", "Dirgantara", 2024, 5000000000.0,
                    "Roket terbesar dalam sejarah mencapai orbit stabil.", "Kunci kolonisasi Bulan dan Mars.");
                createResearch(researchRepo, admin, "AI for Material Science: GNoME (Google DeepMind)", "Material", 2024, 500000000.0,
                    "Penemuan 2.2 juta kristal baru menggunakan Deep Learning.", "Percepatan penemuan material baterai dan panel surya.");
                createResearch(researchRepo, admin, "Apple Vision Pro: Spatial Computing Adoption (Apple)", "INFOKOM", 2024, 0.0,
                    "Studi interaksi manusia dengan komputer spasial (Mixed Reality).", "Era baru antarmuka komputasi setelah layar sentuh.");
                createResearch(researchRepo, admin, "Desain Istana Negara Garuda IKN (KemenPUPR/Nyoman Nuarta)", "Arsitektur", 2024, 0.0,
                    "Analisis struktur dan aerodinamika selubung Garuda.", "Simbol arsitektur monumental Indonesia baru.");
                createResearch(researchRepo, admin, "Personalisasi Obat Kanker dengan AI (MD Anderson)", "Kesehatan", 2024, 800000000.0,
                    "Terapi imunoterapi yang disesuaikan profil genetik pasien.", "Meningkatkan tingkat kesembuhan kanker stadium lanjut.");
                createResearch(researchRepo, admin, "Geothermal Energy: Advanced Geothermal Systems (Pertamina/Chevron)", "Energi", 2024, 2000000000.0,
                    "Teknologi siklus tertutup untuk ekstraksi panas bumi.", "Memaksimalkan potensi panas bumi Indonesia.");
                createResearch(researchRepo, admin, "Gemini 1.5 Pro: Million Token Context Window (Google)", "INFOKOM", 2024, 0.0,
                    "Model AI yang bisa memproses buku tebal atau video panjang sekaligus.", "Kemampuan analisis dokumen super masif.");

                // ==================== TAHUN 2025 (Future Tech) ====================
                // --- INTERNAL ---
                createResearch(researchRepo, admin, "Sistem Penyortir Kemiri Berbasis YOLOv8 (Merry Meryam Martgrita)", "TTG", 2025, 52105768.0,
                    "Otomatisasi sortasi kualitas kemiri pasca panen.", "Meningkatkan nilai jual komoditas lokal.");
                createResearch(researchRepo, admin, "Prediction Model for Mental Health LSTM (Good Fried Panggabean)", "INFOKOM", 2025, 72513458.0,
                    "Deteksi dini depresi dari pola teks media sosial.", "Intervensi kesehatan mental proaktif.");

                // --- EKSTERNAL (Prediksi/Ongoing) ---
                createResearch(researchRepo, admin, "Quantum Error Correction Milestone (IBM/Google)", "INFOKOM", 2025, 2000000000.0,
                    "Mengatasi 'noise' pada qubit untuk komputasi kuantum stabil.", "Langkah vital menuju komputer kuantum komersial.");
                createResearch(researchRepo, admin, "6G Network Architecture Standard (ITU)", "INFOKOM", 2025, 500000000.0,
                    "Finalisasi standar global komunikasi seluler generasi ke-6.", "Menyatukan dunia fisik dan digital secara real-time.");
                createResearch(researchRepo, admin, "Bio-printing Organ Manusia Fungsional (Wake Forest)", "Biomedis", 2025, 1000000000.0,
                    "Pencetakan ginjal mini yang berfungsi untuk uji obat.", "Masa depan transplantasi organ tanpa donor.");
                createResearch(researchRepo, admin, "Pembangkit Listrik Tenaga Nuklir Thorium (ThorCon/Indonesia)", "Energi", 2025, 10000000000.0,
                    "Studi kelayakan reaktor garam cair (MSR) di Bangka Belitung.", "Alternatif energi bersih baseload selain batubara.");
                createResearch(researchRepo, admin, "Autonomous Humanoid Robots in Manufacturing (Tesla/Figure)", "Robotika", 2025, 3000000000.0,
                    "Robot berbentuk manusia bekerja berdampingan di pabrik mobil.", "Solusi kekurangan tenaga kerja global.");
                createResearch(researchRepo, admin, "Direct-to-Cell Satellite Connectivity (Starlink/Telkomsel)", "Telekomunikasi", 2025, 1000000000.0,
                    "Layanan SMS/Data satelit langsung ke HP biasa tanpa alat tambahan.", "Menghapus blank spot sinyal di seluruh kepulauan.");
                createResearch(researchRepo, admin, "AI Agents for Scientific Discovery (Microsoft)", "Sains", 2025, 800000000.0,
                    "Agen AI otonom yang merancang dan menjalankan eksperimen lab.", "Percepatan penemuan sains 100x lipat.");
                createResearch(researchRepo, admin, "Climate Geoengineering: Stratospheric Aerosol Injection (Harvard)", "Lingkungan", 2025, 200000000.0,
                    "Studi kontroversial pendinginan bumi dengan memantulkan sinar matahari.", "Opsi darurat terakhir mitigasi pemanasan global.");
                createResearch(researchRepo, admin, "Brain-Computer Interface for Consumer (Meta/Apple)", "Neuroteknologi", 2025, 1500000000.0,
                    "Gelang saraf untuk mengontrol AR Glasses dengan pikiran.", "Interaksi manusia-komputer tanpa sentuhan.");
                createResearch(researchRepo, admin, "Food Estate Digital Twin (Kementan/IPB)", "Pertanian", 2025, 500000000.0,
                    "Simulasi digital lahan lumbung pangan nasional.", "Optimalisasi masa tanam dan panen berbasis data presisi.");

                System.out.println(">>> Database Terisi: 100+ Data (Internal & Eksternal, 2019-2025)!");
            }
        };
    }

    private void createResearch(ResearchRepository repo, User user, String title, String theme, int year, Double dana, String desc, String impact) {
        Research r = new Research();
        r.setTitle(title);
        r.setResearchTheme(theme);
        r.setReleaseYear(year);
        r.setFundAmount(dana);
        r.setDescription(desc); // Set Deskripsi
        r.setImpact(impact);    // Set Dampak
        r.setUserId(user.getId());
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        repo.save(r);
    }
}