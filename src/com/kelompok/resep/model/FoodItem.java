package com.kelompok.resep.model;

/**
 * Kelas abstrak yang merepresentasikan dasar dari sebuah item makanan.
 * Kelas ini berfungsi sebagai kerangka (blueprint) untuk jenis makanan spesifik.
 * * Menggunakan prinsip Encapsulation dan Abstraction.
 */
public abstract class FoodItem {
    
    // -- ENCAPSULATION: Variabel private agar tidak bisa diakses langsung dari luar --
    
    /** Nama dari item makanan (contoh: "Nasi Goreng", "Telur") */
    private String nama;
    
    /** Kategori makanan (contoh: "Makanan Utama", "Bahan Baku", "Minuman") */
    private String kategori;

    /**
     * Constructor untuk menginisialisasi objek FoodItem.
     * * @param nama Nama makanan
     * @param kategori Kategori makanan
     */
    public FoodItem(String nama, String kategori) {
        this.nama = nama;
        this.kategori = kategori;
    }

    // -- GETTERS & SETTERS: Akses aman ke variabel private --

    /**
     * Mengambil nama makanan.
     * @return String nama makanan
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengubah nama makanan.
     * @param nama Nama baru yang ingin diset
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * Mengambil kategori makanan.
     * @return String kategori makanan
     */
    public String getKategori() {
        return kategori;
    }

    /**
     * Mengubah kategori makanan.
     * @param kategori Kategori baru yang ingin diset
     */
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // -- ABSTRACTION --

    /**
     * Method abstrak untuk menampilkan informasi detail makanan.
     * Method ini WAJIB di-override (ditulis ulang) oleh kelas turunan (subclass).
     * Implementasinya akan berbeda tergantung jenis makanannya.
     */
    public abstract void tampilkanInfo();
}