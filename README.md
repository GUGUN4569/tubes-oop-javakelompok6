# 🥗 Aplikasi Manajemen Resep & Nutrisi

**Tugas Besar Pemrograman Berorientasi Objek (PBO)**  
**Kelompok 6** | *Java Swing Application*

Aplikasi desktop berbasis Java untuk mencatat resep masakan, menghitung total kalori secara otomatis, dan menentukan status kesehatan makanan berdasarkan komposisi bahannya.

---

## 📋 Daftar Isi
- [✨ Fitur Utama](#-fitur-utama)
- [🛠 Teknologi & Konsep OOP](#-teknologi--konsep-oop)
- [📂 Struktur Project](#-struktur-project)
- [📝 Penjelasan File](#-penjelasan-file)
- [🚀 Cara Menjalankan](#-cara-menjalankan)
- [🔑 Akun Login](#-akun-login)
- [👥 Anggota Kelompok](#-anggota-kelompok)

---

## ✨ Fitur Utama

1. **Sistem Login**: Akses aplikasi aman menggunakan username dan password.
2. **Input Resep Lengkap**:
   - Memasukkan Nama Resep & Kategori (Sarapan, Makan Siang, dll).
   - Menulis langkah-langkah pembuatan.
   - Menambahkan bahan-bahan dan kalori secara dinamis.
3. **Kalkulator Nutrisi Cerdas**:
   - Otomatis menjumlahkan total kalori dari semua bahan.
   - **Indikator Kesehatan**: Memberikan status *"Sehat"* atau *"Tinggi Kalori"* (jika > 500 kkal).
4. **Database Penyimpanan (File TXT)**:
   - Data resep disimpan permanen di file `data/resep_data.txt`.
   - Data tidak hilang saat aplikasi ditutup.
5. **Tabel Data Interaktif**: Melihat daftar resep dan menghapus data yang dipilih.

---

## 🛠 Teknologi & Konsep OOP

Project ini dibangun menggunakan **Java JDK 8+** dan library **Java Swing**. Berikut penerapan OOP dalam kode:

| Konsep OOP | Implementasi pada Code |
| :--- | :--- |
| **Encapsulation** | Variabel bersifat `private` dan diakses melalui Getter/Setter. |
| **Inheritance** | Class `Recipe` mewarisi (extends) Abstract Class `FoodItem`. |
| **Abstraction** | Penggunaan `abstract class FoodItem` sebagai kerangka dasar. |
| **Interface** | Interface `Nutrizable` sebagai kontrak untuk fitur hitung gizi. |
| **Polymorphism** | Overriding method `hitungTotalKalori()` dan `tampilkanInfo()`. |
| **Exception** | Custom Error `InputKosongException` untuk validasi input kosong. |

---

## 📂 Struktur Project

```
TUBES-OOP-JAVAKELOMPOK6/
├── data/
│   └── resep_data.txt                 # Database penyimpanan resep
├── src/com/kelompok/resep/
│   ├── logic/
│   │   └── RecipeManager.java         # Business logic dan file handling
│   ├── main/
│   │   └── MainApp.java              # Entry point aplikasi
│   ├── model/
│   │   ├── FoodItem.java             # Abstract class dasar makanan
│   │   ├── Recipe.java               # Class utama resep dengan inner class
│   │   ├── Nutrizable.java           # Interface untuk perhitungan nutrisi
│   │   └── InputKosongException.java # Custom exception handler
│   └── view/
│       ├── Login.java                # GUI halaman login
│       └── MainFrame.java            # GUI utama aplikasi
└── README.md                         # Dokumentasi project
```

---

## 📝 Penjelasan File

Berikut adalah detail fungsi dari setiap file dalam package:

### **Package `logic`**
- **`RecipeManager.java`**  
  *Brain of the application* - Mengatur semua logika bisnis termasuk:
  - CRUD (Create, Read, Update, Delete) data resep
  - Penyimpanan data ke file TXT (`simpanData()`)
  - Pembacaan data dari file TXT (`loadData()`)
  - Validasi dan manajemen list resep

### **Package `main`**
- **`MainApp.java`**  
  *Entry point* - Titik masuk utama aplikasi yang:
  - Mengatur Look and Feel GUI
  - Menjalankan aplikasi di Event Dispatch Thread (EDT) yang aman
  - Memposisikan window di tengah layar sebelum ditampilkan

### **Package `model`**
- **`FoodItem.java`**  
  *Abstract class* - Blueprint dasar untuk semua item makanan:
  - Mengimplementasikan konsep **Abstraction** dan **Encapsulation**
  - Menyediakan properti dasar (nama, kategori) yang diwarisi
- **`Recipe.java`**  
  *Main entity* - Class resep yang kompleks:
  - Extends `FoodItem` (**Inheritance**)
  - Implements `Nutrizable` (**Interface**)
  - Mengandung inner class `Ingredient` untuk komposisi bahan
  - Menghitung total kalori dan status kesehatan
- **`Nutrizable.java`**  
  *Interface contract* - Wajibkan implementasi fitur nutrisi:
  - `hitungTotalKalori()` - metode perhitungan kalori
  - `cekStatusKesehatan()` - metode pengecekan status
- **`InputKosongException.java`**  
  *Custom exception* - Menangani error validasi input:
  - Dilempar saat nama resep atau kategori kosong
  - Syarat tugas untuk **Exception Handling**

### **Package `view`**
- **`Login.java`**  
  *Login form* - Halaman awal aplikasi:
  - Menggunakan `JFrame` dengan layout `BorderLayout`
  - Validasi sederhana username/password
  - Mengarahkan ke MainFrame setelah login sukses
- **`MainFrame.java`**  
  *Main application window* - Inti GUI aplikasi:
  - Menggunakan `CardLayout` untuk switching panel
  - Dua panel utama: Form Input dan Tabel Data
  - Integrasi penuh dengan `RecipeManager` untuk data handling
  - Styling modern dengan warna yang konsisten

---

## 🚀 Cara Menjalankan

### **Persiapan:**
1. Pastikan **Java JDK 8 atau lebih tinggi** sudah terinstall di komputer
2. Download atau clone repository ini

### **Metode 1: Menggunakan IDE (Direkomendasikan)**
1. **VS Code / IntelliJ IDEA / NetBeans**
   - Buka folder project di IDE
   - Pastikan struktur folder sesuai (src, data, dll)
   - Compile project terlebih dahulu
   - Jalankan file: `src/com/kelompok/resep/main/MainApp.java`
   - Atau jalankan file: `src/com/kelompok/resep/view/Login.java`

### **Metode 2: Menggunakan Command Line**
```bash
# 1. Compile semua file Java
javac -d bin src/com/kelompok/resep/**/*.java

# 2. Pastikan folder 'bin' dan 'data' ada
mkdir -p bin data

# 3. Run aplikasi
java -cp bin com.kelompok.resep.main.MainApp
```

### **Catatan Penting:**
- Folder `data` harus berada di **sejajar** dengan folder `src`
- Jika file `resep_data.txt` tidak ada, akan dibuat otomatis
- Gunakan encoding UTF-8 untuk menghindari masalah karakter

---

## 🔑 Akun Login

Gunakan kredensial default berikut untuk masuk ke aplikasi:

```
👤 Username: Kelompok6
🔒 Password: 123
```

*Note: Password disimpan dalam plain text untuk keperluan demo*

---

## 👥 Anggota Kelompok 6

| No | Nama | NIM | Peran Utama |
|----|------|-----|------------|
| 1. | Fakhri Shafwan Malik | 1324050 | GUI Design |
| 2. | Rezky Pradikta Nugraha | 1324053 | Backend |
| 3. | Ganjar Rizki Anugrah | 1324059 | Testing & Integrasi |

---
