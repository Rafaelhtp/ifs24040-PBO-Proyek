# Research Tracker 📊

**Research Tracker** adalah aplikasi berbasis web yang dibangun dengan **Spring Boot** untuk mengelola, melacak, dan memvisualisasikan data penelitian serta alokasi dana hibah. Aplikasi ini dirancang untuk memudahkan institusi atau individu dalam memonitor performa penelitian melalui dashboard interaktif.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![Thymeleaf](https://img.shields.io/badge/Template-Thymeleaf-darkgreen)

## ✨ Fitur Utama

* **Dashboard Analitik:** Visualisasi data menggunakan grafik (Chart.js) untuk memantau total dana per tahun, distribusi tema penelitian, dan statistik peneliti.
* **Manajemen Penelitian (CRUD):** Tambah, lihat detail, ubah, dan hapus data penelitian.
* **Upload Lampiran:** Dukungan upload gambar cover/poster untuk setiap judul penelitian.
* **Manajemen Peneliti:** Pengelolaan data peneliti dan afiliasinya.
* **Otentikasi Pengguna:** Sistem Login dan Registrasi pengguna.
* **Filter & Pencarian:** Fitur pencarian data berdasarkan judul, peneliti, atau tema.
* **Data Seeding:** Database otomatis terisi dengan data dummy (tahun 2019-2025) saat aplikasi pertama kali dijalankan (untuk keperluan demo).

## 🛠️ Teknologi yang Digunakan

* **Backend:** Java 17, Spring Boot 3.2.0 (Web, Data JPA, Security).
* **Frontend:** Thymeleaf (Server-side rendering), Tailwind CSS (via CDN), Chart.js.
* **Database:** PostgreSQL.
* **Testing:** JUnit 5, Mockito.
* **Tools:** Maven, Lombok.

## ⚙️ Prasyarat

Sebelum menjalankan proyek ini, pastikan komputer kamu sudah terinstall:

1.  **Java JDK 17** atau lebih baru.
2.  **Maven** (biasanya sudah include di IDE seperti IntelliJ/Eclipse/VS Code).
3.  **PostgreSQL** (pastikan service berjalan di port 5432).
