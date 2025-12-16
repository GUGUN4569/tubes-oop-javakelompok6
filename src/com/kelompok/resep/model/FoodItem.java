package com.kelompok.resep.model;

public abstract class FoodItem {
    // Encapsulation: Variabel private
    private String nama;
    private String kategori;

    public FoodItem(String nama, String kategori) {
        this.nama = nama;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // Abstract method
    public abstract void tampilkanInfo();
}