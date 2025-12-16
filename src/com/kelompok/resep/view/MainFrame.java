package com.kelompok.resep.view;

import com.kelompok.resep.logic.RecipeManager;
import com.kelompok.resep.model.Recipe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private CardLayout card;
    private JPanel mainPanel;

    // Komponen Input
    private JTextField inpNamaResep, inpTipe, inpBahan, inpKalori;
    private JTextArea inpLangkah;
    private JTextArea displayBahanSementara; // Buat nampilin bahan yang barusan diinput
    
    // Komponen Output (Tabel)
    private JTable tabelOutput;
    private DefaultTableModel tableModel;

    // Logic & Data
    private RecipeManager manager;
    private ArrayList<String[]> tempBahanList; // Penampung bahan sementara sebelum disave

    public MainFrame() {
        manager = new RecipeManager(); // Load database
        tempBahanList = new ArrayList<>();

        setTitle("Kelompok 6 - Aplikasi Resep Makanan & Nutrisi");
        setSize(800, 600); // Saya gedein dikit biar lega
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(formResep(), "form");
        mainPanel.add(outputPanel(), "output");

        add(mainPanel);
    }

    // --- PANEL 1: FORM INPUT ---
    private JPanel formResep() {
        JPanel panel = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(50, 50, 200)); // Warna Header Biru
        JLabel judul = new JLabel("Form Input Resep - Kelompok 6", SwingConstants.CENTER);
        judul.setFont(new Font("Arial", Font.BOLD, 22));
        judul.setForeground(Color.WHITE);

        JLabel anggota = new JLabel("Anggota: GUGUN | REZKI | FAKHRI", SwingConstants.CENTER);
        anggota.setForeground(Color.WHITE);

        header.add(judul);
        header.add(anggota);
        panel.add(header, BorderLayout.NORTH);

        // FORM BODY
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris 1: Nama Resep
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nama Resep:"), gbc);
        inpNamaResep = new JTextField(20);
        gbc.gridx = 1; form.add(inpNamaResep, gbc);

        // Baris 2: Tipe/Kategorigit 
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Kategori (Pagi/Siang/Malam):"), gbc);
        inpTipe = new JTextField(20);
        gbc.gridx = 1; form.add(inpTipe, gbc);

        // Baris 3: Langkah Memasak
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Langkah Memasak:"), gbc);
        inpLangkah = new JTextArea(3, 20);
        inpLangkah.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1; form.add(new JScrollPane(inpLangkah), gbc);

        // SEPARATOR BAHAN
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Baris 4: Input Bahan
        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Nama Bahan:"), gbc);
        inpBahan = new JTextField(15);
        gbc.gridx = 1; form.add(inpBahan, gbc);

        // Baris 5: Input Kalori
        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Kalori (Angka):"), gbc);
        inpKalori = new JTextField(10);
        gbc.gridx = 1; form.add(inpKalori, gbc);

        // Baris 6: Tombol Tambah Bahan
        JButton btnTambahBahan = new JButton("+ Tambah Bahan ke List");
        gbc.gridx = 1; gbc.gridy = 6; form.add(btnTambahBahan, gbc);

        // Baris 7: Tampilan List Bahan Sementara
        gbc.gridx = 0; gbc.gridy = 7; form.add(new JLabel("List Bahan:"), gbc);
        displayBahanSementara = new JTextArea(4, 20);
        displayBahanSementara.setEditable(false);
        displayBahanSementara.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1; form.add(new JScrollPane(displayBahanSementara), gbc);

        // Baris 8: Tombol Simpan Final
        JButton btnSimpan = new JButton("SIMPAN RESEP KE DATABASE");
        btnSimpan.setBackground(new Color(0, 150, 0));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 10, 5, 10);
        form.add(btnSimpan, gbc);

        panel.add(form, BorderLayout.CENTER);

        // FOOTER (NAVIGASI)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExit = new JButton("Exit");
        JButton btnLihatData = new JButton("Lihat Tabel Data >>");

        btnExit.addActionListener(e -> System.exit(0));
        btnLihatData.addActionListener(e -> {
            refreshTable(); // Update tabel sebelum pindah halaman
            card.show(mainPanel, "output");

        footer.add(btnExit);
        footer.add(btnLihatData);
        panel.add(footer, BorderLayout.SOUTH);

        // --- ACTION LISTENERS (LOGIC) ---

        // 1. Logic Tambah Bahan
        btnTambahBahan.addActionListener(e -> {
            String namaB = inpBahan.getText();
            String kalB = inpKalori.getText();

            if (namaB.isEmpty() || kalB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi nama bahan dan kalori dulu!");
                return;
            }

            try {
                double kal = Double.parseDouble(kalB); // Validasi angka
                // Simpan ke array sementara
                tempBahanList.add(new String[]{namaB, String.valueOf(kal)});
                
                // Update tampilan
                displayBahanSementara.append("- " + namaB + " (" + kal + " kkal)\n");
                
                // Reset field bahan
                inpBahan.setText("");
                inpKalori.setText("");
                inpBahan.requestFocus(); // Balikin kursor ke field bahan
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Kalori harus berupa angka! (Contoh: 100.5)");
            }
        });

        // 2. Logic Simpan Resep Utama
        btnSimpan.addActionListener(e -> {
            String nama = inpNamaResep.getText();
            String tipe = inpTipe.getText();
            String langkah = inpLangkah.getText();

            if (nama.isEmpty() || tipe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama Resep dan Kategori wajib diisi!");
                return;
            }

            // A. Buat Object Resep
            Recipe resepBaru = new Recipe(nama, tipe, langkah);

            // B. Masukin Bahan-bahan dari List Sementara ke Object Resep
            for (String[] bahan : tempBahanList) {
                resepBaru.tambahBahan(bahan[0], Double.parseDouble(bahan[1]));
            }

            // C. Kirim ke Manager (Simpan ke File)
            manager.tambahResep(resepBaru);

            // D. Feedback & Reset
            JOptionPane.showMessageDialog(this, "Berhasil! Resep " + nama + " disimpan.");
            resetForm();
        });

        return panel;
    }

    // --- PANEL 2: OUTPUT (TABEL) ---
    private JPanel outputPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Judul
        JLabel lblTitle = new JLabel("Daftar Resep Tersimpan", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Tabel Data (Sesuai Syarat Tugas)
        String[] kolom = {"Nama Resep", "Kategori", "Total Kalori", "Status Kesehatan"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabelOutput = new JTable(tableModel);
        panel.add(new JScrollPane(tabelOutput), BorderLayout.CENTER);

        // Tombol Kembali
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("<< Kembali ke Input");
        JButton btnDelete = new JButton("Hapus Baris Terpilih");

        btnBack.addActionListener(e -> card.show(mainPanel, "form"));
        
        btnDelete.addActionListener(e -> {
            int row = tabelOutput.getSelectedRow();
            if (row >= 0) {
                manager.hapusResep(row);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang mau dihapus!");
            }
        });

        footer.add(btnBack);
        footer.add(btnDelete);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // --- HELPER METHODS ---

    private void resetForm() {
        inpNamaResep.setText("");
        inpTipe.setText("");
        inpLangkah.setText("");
        inpBahan.setText("");
        inpKalori.setText("");
        displayBahanSementara.setText("");
        tempBahanList.clear(); // Kosongkan list bahan sementara
    }

    private void refreshTable() {
        // Hapus data lama di tabel
        tableModel.setRowCount(0);

        // Ambil data terbaru dari Manager
        for (Recipe r : manager.getAllResep()) {
            Object[] row = {
                r.getNama(),
                r.getKategori(),
                r.hitungTotalKalori(), // Menggunakan Method OOP
                r.cekStatusKesehatan() // Menggunakan Method OOP
            };
            tableModel.addRow(row);
        }
    }
}