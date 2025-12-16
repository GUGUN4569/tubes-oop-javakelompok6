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
    // Pastikan path ini benar. Kalau error, coba ganti jadi absolute path atau cukup "resep_data.txt"
    private static final String FILE_PATH = "data/resep_data.txt"; 

    public RecipeManager() {
        this.daftarResep = new ArrayList<>();
        ensureDataDirectoryExists();
        loadData(); // PERBAIKAN 1: Panggil load data saat aplikasi mulai!
    }

    // --- CRUD OPERATIONS ---

    public void tambahResep(Recipe r) {
        if (r != null) {
            daftarResep.add(r);
            simpanData(); // PERBAIKAN 2: Auto-save setiap kali nambah resep!
            System.out.println("[INFO] Resep " + r.getNama() + " berhasil ditambahkan & disimpan.");
        }
    }

    public void hapusResep(int index) {
        if (index >= 0 && index < daftarResep.size()) {
            daftarResep.remove(index);
            simpanData(); // Auto-save saat hapus
        }
    }

    public List<Recipe> getAllResep() {
        return daftarResep;
    }

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Recipe r : daftarResep) {
                // Format: Nama;Kategori;Langkah;TotalKalori
                // Kita ganti enter (\n) dengan spasi biar file txt tidak rusak
                String langkahBersih = r.getLangkahPembuatan().replace("\n", " ");
                
                String line = String.format("%s;%s;%s;%.2f", 
                    r.getNama(), 
                    r.getKategori(), 
                    langkahBersih,
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

    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                // Pastikan ada minimal 4 bagian (Nama, Kategori, Langkah, Kalori)
                if (parts.length >= 4) {
                    String nama = parts[0];
                    String kategori = parts[1];
                    String langkah = parts[2];
                    double totalKalori = Double.parseDouble(parts[3]);

                    Recipe r = new Recipe(nama, kategori, langkah);
                    
                    // PERBAIKAN 3: RESTORE KALORI
                    // Karena kita tidak menyimpan detail bahan satu per satu di file txt,
                    // kita masukkan total kalori sebagai satu "bahan gabungan" agar hitungan tetap benar.
                    if (totalKalori > 0) {
                        r.tambahBahan("Data Tersimpan", totalKalori);
                    }

                    daftarResep.add(r);
                }
            }
        } catch (Exception e) { // Catch Exception biar kalau format angka salah ga crash
            System.err.println("[ERROR] Gagal membaca data: " + e.getMessage());
        }
    }
}