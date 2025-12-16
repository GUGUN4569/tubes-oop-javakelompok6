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
    private static final String FILE_PATH = "data/resep_data.txt"; // Konstanta agar mudah diubah

    public RecipeManager() {
        this.daftarResep = new ArrayList<>();
        ensureDataDirectoryExists();
    }

    // --- CRUD OPERATIONS ---

    public void tambahResep(Recipe r) {
        if (r != null) {
            daftarResep.add(r);
            System.out.println("[INFO] Resep " + r.getNama() + " berhasil ditambahkan.");
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
        return result.orElse(null); // Return null jika tidak ketemu
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
                String line = String.format("%s;%s;%s;%.2f", 
                    r.getNama(), 
                    r.getKategori(), 
                    r.getLangkahPembuatan().replace("\n", " "), // Sanitasi newline
                    r.hitungTotalKalori()
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("[SUCCESS] Data berhasil disimpan ke " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menyimpan data: " + e.getMessage());
        }
    }

    // Placeholder untuk Load Data (bisa dikembangkan nanti)
    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Logika parsing sederhana (split by ;)
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Recipe r = new Recipe(parts[0], parts[1], parts[2]);
                    // Note: Load bahan secara detail butuh struktur file lebih kompleks (JSON/XML)
                    // Untuk tugas ini, kita load basic info dulu.
                    daftarResep.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca data: " + e.getMessage());
        }
    }
}