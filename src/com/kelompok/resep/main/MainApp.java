package com.kelompok.resep.main;

import com.kelompok.resep.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry Point (Titik Masuk) Utama Aplikasi.
 * Bertugas mengatur konfigurasi awal dan meluncurkan GUI dengan aman.
 */
public class MainApp {
    
    public static void main(String[] args) {
        
        // --- 1. SETUP TAMPILAN (Look and Feel) ---
        // Mengubah tampilan agar sesuai dengan Sistem Operasi pengguna (Windows/Mac/Linux)
        // supaya terlihat modern dan tidak kaku.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(); // Jika gagal, tetap jalan dengan tampilan default
        }

        // --- 2. MELUNCURKAN GUI (Thread Safety) ---
        // Menggunakan 'invokeLater' untuk memastikan GUI berjalan di Event Dispatch Thread (EDT).
        // Ini WAJIB di Java Swing agar aplikasi tidak 'hang' atau crash saat memuat komponen berat.
        SwingUtilities.invokeLater(() -> {
            try {
                // Membuat objek window utama
                MainFrame frame = new MainFrame();
                
                // PENTING: Set posisi ke tengah layar DULU sebelum ditampilkan
                // (Agar user tidak melihat window 'lompat' dari pojok ke tengah)
                frame.setLocationRelativeTo(null); 
                
                // Menampilkan window ke layar
                frame.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Gagal memulai aplikasi: " + e.getMessage());
            }
        });
    }
}