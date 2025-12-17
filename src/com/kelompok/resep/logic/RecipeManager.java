package com.kelompok.resep.logic;

import com.kelompok.resep.model.InputKosongException;
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
        }
    }

    public List<Recipe> getAllResep() {
        return daftarResep;
    }

    private void ensureDataDirectoryExists() {
        File file = new File("data");
        if (!file.exists()) file.mkdir();
    }

    public void simpanData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Recipe r : daftarResep) {
                // 1. Format Bahan
                StringBuilder sbBahan = new StringBuilder();
                for (Ingredient b : r.getListBahan()) {
                    String namaBahanAman = b.getNamaBahan().replace(":", "-");
                    sbBahan.append(namaBahanAman).append(":").append(b.getKalori()).append(",");
                }
                String stringBahan = sbBahan.length() > 0 ? sbBahan.toString() : "Kosong:0";

                // 2. Format Langkah (ArrayList -> String satu baris dipisah | )
                StringBuilder sbLangkah = new StringBuilder();
                for (String l : r.getLangkahPembuatan()) {
                    // Ganti pipe (|) dan enter agar tidak merusak format
                    sbLangkah.append(l.replace("|", "").replace("\n", " ")).append("|");
                }
                String stringLangkah = sbLangkah.length() > 0 ? sbLangkah.toString() : "Belum ada langkah";

                // 3. Sanitasi Nama & Kategori
                String namaAman = r.getNama().replace(";", "-");
                String katAman = r.getKategori().replace(";", "-");

                // Tulis: Nama;Kategori;Langkah1|Langkah2|;Bahan1:Cal,Bahan2:Cal
                String line = String.format("%s;%s;%s;%s",
                        namaAman, katAman, stringLangkah, stringBahan);

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
                    try {
                        // REVISI: Pakai Constructor 2 Parameter
                        Recipe r = new Recipe(parts[0], parts[1]);

                        // 1. Load Langkah (Split by |)
                        String[] steps = parts[2].split("\\|");
                        for (String s : steps) {
                            if (!s.isEmpty()) r.tambahLangkah(s);
                        }

                        // 2. Load Bahan
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
                                } catch (NumberFormatException e) {
                                    // Ignore bad data
                                }
                            }
                        }
                        daftarResep.add(r);
                        
                    } catch (InputKosongException e) {
                        System.err.println("Skip data rusak: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca data: " + e.getMessage());
        }
    }
}