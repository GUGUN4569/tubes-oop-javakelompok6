package com.kelompok.resep.model;

import java.util.ArrayList;

public class Recipe extends FoodItem implements Nutrizable {
    private String langkahPembuatan;
    private ArrayList<Ingredient> listBahan; // Pakai ArrayList biasa

    public Recipe(String nama, String kategori, String langkah) {
        super(nama, kategori);
        this.langkahPembuatan = langkah;
        this.listBahan = new ArrayList<>();
    }

    // --- INNER CLASS (Syarat Tugas) ---
    // Kelas Bahan hanya dipakai di dalam Resep
    public class Ingredient {
        private String namaBahan;
        private double kalori;

        public Ingredient(String namaBahan, double kalori) {
            this.namaBahan = namaBahan;
            this.kalori = kalori;
        }

        public String getNamaBahan() { return namaBahan; }
        public double getKalori() { return kalori; }
    }
    // ----------------------------------

    public void tambahBahan(String nama, double kalori) {
        Ingredient bahanBaru = new Ingredient(nama, kalori);
        listBahan.add(bahanBaru);
    }

    @Override
    public double hitungTotalKalori() {
        double total = 0;
        // Pakai For-Loop biasa (Dosen lebih suka ini buat mahasiswa)
        for (Ingredient bahan : listBahan) {
            total += bahan.getKalori();
        }
        return total;
    }

    @Override
    public String cekStatusKesehatan() {
        if (hitungTotalKalori() > 500) {
            return "Tinggi Kalori";
        } else {
            return "Sehat / Rendah Kalori";
        }
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("Resep: " + getNama());
        System.out.println("Total Kalori: " + hitungTotalKalori());
    }

    // Getter untuk kebutuhan GUI nanti
    public String getLangkahPembuatan() { return langkahPembuatan; }
    public ArrayList<Ingredient> getListBahan() { return listBahan; }
}