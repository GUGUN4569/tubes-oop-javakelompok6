package com.kelompok.resep.model;

import java.util.ArrayList;

public class Recipe extends FoodItem implements Nutrizable {
    
    // Menggunakan ArrayList untuk menyimpan banyak langkah dan bahan
    private ArrayList<String> langkahPembuatan;
    private ArrayList<Ingredient> listBahan; 

    public Recipe(String nama, String kategori) throws InputKosongException {
        super(nama, kategori);
        
        if (nama == null || nama.trim().isEmpty()) {
            throw new InputKosongException("Nama resep tidak boleh kosong!");
        }
        if (kategori == null || kategori.trim().isEmpty()) {
            throw new InputKosongException("Kategori tidak boleh kosong!");
        }

        this.langkahPembuatan = new ArrayList<>();
        this.listBahan = new ArrayList<>();
    }

    // --- INNER CLASS ---
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

    public void tambahBahan(String nama, double kalori) {
        listBahan.add(new Ingredient(nama, kalori));
    }

    public void tambahLangkah(String langkah) {
        langkahPembuatan.add(langkah);
    }

    @Override
    public double hitungTotalKalori() {
        double total = 0;
        for (Ingredient bahan : listBahan) {
            total += bahan.getKalori(); 
        }
        return total;
    }

    @Override
    public String cekStatusKesehatan() {
        return (hitungTotalKalori() > 500) ? "Tinggi Kalori" : "Sehat";
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("Resep: " + getNama());
        System.out.println("Langkah-langkah:");
        for (int i = 0; i < langkahPembuatan.size(); i++) {
            System.out.println((i + 1) + ". " + langkahPembuatan.get(i));
        }
    }

    public ArrayList<String> getLangkahPembuatan() { return langkahPembuatan; }
    public ArrayList<Ingredient> getListBahan() { return listBahan; }
}