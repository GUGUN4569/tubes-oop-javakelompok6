package com.kelompok.resep.logic;

import com.kelompok.resep.model.Recipe;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class untuk memanipulasi data resep.
 * Menangani CRUD dan File I/O.
 */
public class RecipeManager {
    private List<Recipe> daftarResep;
    private static final String FILE_PATH = "data/resep_data.txt";

    public RecipeManager() {
        this.daftarResep = new ArrayList<>();
        ensureDataDirectoryExists();
        loadData(); // <--- BARU: Load data saat Manager dibuat
    }

    // --- CRUD OPERATIONS ---

    public void tambahResep(Recipe r) {
        if (r != null) {
            daftarResep.add(r);
            simpanData(); // <--- BARU: Simpan data setiap ada penambahan resep
            System.out.println("[INFO] Resep " + r.getNama() + " berhasil ditambahkan.");
        }
    }
    
    // Metode Hapus Resep (Diperlukan oleh MainFrame)
    public void hapusResep(int index) {
        if (index >= 0 && index < daftarResep.size()) {
            String nama = daftarResep.get(index).getNama();
            daftarResep.remove(index);
            simpanData(); // <--- BARU: Simpan data setelah penghapusan
            System.out.println("[INFO] Resep " + nama + " berhasil dihapus.");
        }
    }

    public List<Recipe> getAllResep() {
        return daftarResep;
    }

    // Fitur Tambahan: Mencari Resep (Java Stream Filter)
    public Recipe cariResep(String nama) {
        Optional<Recipe> result = daftarResep.stream()
                .filter(r -> r.getNama().equalsIgnoreCase(nama))
                .findFirst();
        return result.orElse(null);
    }

    // --- FILE I/O OPERATIONS ---

    private void ensureDataDirectoryExists() {
        File file = new File("data");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void simpanData() {
        // Try-with-resources: Otomatis menutup file stream (Anti Memory Leak)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Recipe r : daftarResep) {
                // Format CSV custom: Nama;Kategori;Langkah;TotalKalori
                // PENTING: Untuk keperluan load, total kalori di sini tidak digunakan, 
                // karena total kalori harus dihitung saat loading bahan (yang belum Anda simpan).
                // Kita simpan Nama;Kategori;Langkah saja untuk load sederhana.
                String line = String.format("%s;%s;%s", 
                    r.getNama(), 
                    r.getTipe(), // Ganti getKategori() menjadi getTipe() jika di Recipe menggunakan getTipe
                    r.getLangkahPembuatan().replace("\n", " ")
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("[SUCCESS] Data berhasil disimpan ke " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menyimpan data: " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        // Kosongkan list yang ada sebelum load
        daftarResep.clear(); 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Recipe r = new Recipe(parts[0], parts[1], parts[2]);
                    // Karena kita tidak menyimpan detail bahan, resep yang diload memiliki total kalori 0.
                    // Ini cukup untuk menampilkan Nama, Kategori, dan Langkah di GUI.
                    daftarResep.add(r);
                }
            }
            System.out.println("[SUCCESS] Data berhasil diload: " + daftarResep.size() + " resep.");
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca data: " + e.getMessage());
        }
    }
}