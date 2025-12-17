package com.kelompok.resep.logic;

import com.kelompok.resep.model.Recipe;
import com.kelompok.resep.model.Recipe.Ingredient; 
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    private List<Recipe> daftarResep;
    private static final String FILE_PATH = "data/resep_data.txt"; 

    public RecipeManager() {
        this.daftarResep = new ArrayList<>();
        ensureDataDirectoryExists();
        loadData(); 
    }

    // --- CRUD OPERATIONS ---

    public void tambahResep(Recipe r) {
        if (r != null) {
            daftarResep.add(r);
            simpanData(); 
            System.out.println("[INFO] Resep " + r.getNama() + " berhasil disimpan.");
        }
    }

    public void hapusResep(int index) {
        if (index >= 0 && index < daftarResep.size()) {
            daftarResep.remove(index);
            simpanData(); 
            System.out.println("[INFO] Data ke-" + index + " berhasil dihapus.");
        }
    }

    public List<Recipe> getAllResep() {
        return daftarResep;
    }

    // --- FILE I/O (SIMPAN & BACA DATA) ---

    private void ensureDataDirectoryExists() {
        File file = new File("data");
        if (!file.exists()) file.mkdir();
    }

    public void simpanData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Recipe r : daftarResep) {
                // 1. Gabungkan detail bahan jadi string
                StringBuilder sbBahan = new StringBuilder();
                ArrayList<Ingredient> listBahan = r.getListBahan(); 
                
                for (int i = 0; i < listBahan.size(); i++) {
                    Ingredient b = listBahan.get(i);
                    // Pastikan nama bahan juga tidak mengandung titik dua
                    String namaBahanAman = b.getNamaBahan().replace(":", "-");
                    sbBahan.append(namaBahanAman).append(":").append(b.getKalori());
                    if (i < listBahan.size() - 1) sbBahan.append(","); 
                }

                String stringBahan = sbBahan.length() > 0 ? sbBahan.toString() : "Kosong:0";
                
                // --- PERBAIKAN PENTING DI SINI (SANITASI INPUT) ---
                // Mengganti titik koma (;) dengan strip (-) agar tidak merusak format CSV
                // Mengganti enter (\n) dengan spasi agar tetap satu baris
                String namaAman = r.getNama().replace(";", "-");
                String katAman = r.getKategori().replace(";", "-");
                String langkahAman = r.getLangkahPembuatan().replace("\n", " ").replace(";", ",");
                
                // Tulis ke file menggunakan data yang sudah 'aman'
                String line = String.format("%s;%s;%s;%s", 
                    namaAman, katAman, langkahAman, stringBahan);
                
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menyimpan data: " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        daftarResep.clear(); 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                
                if (parts.length >= 4) {
                    Recipe r = new Recipe(parts[0], parts[1], parts[2]);
                    
                    String[] rawBahan = parts[3].split(",");
                    for (String rb : rawBahan) {
                        String[] detail = rb.split(":"); 
                        if (detail.length == 2) {
                            try {
                                String nm = detail[0];
                                double kal = Double.parseDouble(detail[1]);
                                if (!nm.equals("Kosong")) {
                                    r.tambahBahan(nm, kal);
                                }
                            } catch (Exception e) { 
                                // --- PERBAIKAN DI SINI (LOGGING) ---
                                // Jangan dibiarkan kosong, print error biar ketahuan kalau ada data korup
                                System.err.println("[WARNING] Gagal parse bahan '" + rb + "': " + e.getMessage());
                            }
                        }
                    }
                    daftarResep.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca data: " + e.getMessage());
        }
    }
}