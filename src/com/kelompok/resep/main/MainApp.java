package com.kelompok.resep.main;

import com.kelompok.resep.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry Point Aplikasi.
 * Melakukan setup environment sebelum GUI dimuat.
 */
public class MainApp {
    
    public static void main(String[] args) {
        // Setup Tampilan agar terlihat modern (Mengikuti OS User)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thread Safety: Menjalankan GUI di Event Dispatch Thread (EDT)
        // Ini wajib hukumnya di Java Swing profesional
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                // Center screen location
                frame.setLocationRelativeTo(null); 
            } catch (Exception e) {
                System.err.println("Gagal memulai aplikasi: " + e.getMessage());
            }
        });
    }
}