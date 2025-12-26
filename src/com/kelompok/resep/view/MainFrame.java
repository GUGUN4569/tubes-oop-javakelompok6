package com.kelompok.resep.view;

import com.kelompok.resep.logic.RecipeManager;
import com.kelompok.resep.model.InputKosongException;
import com.kelompok.resep.model.Recipe;
import com.kelompok.resep.model.Recipe.Ingredient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private int indexEdit = -1; // -1 = tambah, >=0 = edit // Menambahkan untuk edit
    private CardLayout card;
    private JPanel mainPanel;
    private RecipeManager manager;
    private ArrayList<String[]> tempBahanList;

    // Komponen GUI
    private JTextField inpNamaResep, inpBahan, inpKalori;
    private JComboBox<String> cmbKategori;
    private JTextArea inpLangkah, displayBahanSementara; // Menggunakan JTextArea agar kotak besar
    private JTable tabelOutput;
    private DefaultTableModel tableModel;

    // Font Custom
    private final Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fontBold = new Font("Segoe UI", Font.BOLD, 14);

    public MainFrame() {
        // --- 1. SETUP TEMA (Look and Feel) ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        // --- 2. INISIALISASI DATA ---
        manager = new RecipeManager();
        tempBahanList = new ArrayList<>();

        // --- 3. SETUP WINDOW UTAMA ---
        setTitle("Aplikasi Resep & Nutrisi - Kelompok 6");
        setSize(1100, 800); // Ukuran Window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menggunakan CardLayout untuk pindah halaman (Form <-> Tabel)
        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(buatFormPanel(), "form");
        mainPanel.add(buatTabelPanel(), "output");

        add(mainPanel);
    }

    // ==========================================
    // BAGIAN 1: PANEL FORM INPUT (Halaman Utama)
    // ==========================================
    private JPanel buatFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // A. HEADER
        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80)); // Warna Dark Blue
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel judul = new JLabel("FORM INPUT RESEP BARU");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        judul.setForeground(Color.WHITE);
        header.add(judul);
        panel.add(header, BorderLayout.NORTH);

        // B. BODY FORM (GridBagLayout)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Jarak antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Baris 1: Nama Resep ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        form.add(createLabel("Nama Resep:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        inpNamaResep = new JTextField();
        styleField(inpNamaResep);
        form.add(inpNamaResep, gbc);

        // --- Baris 2: Kategori ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(createLabel("Kategori Makan:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        String[] opsi = { "-- Pilih Kategori --", "Sarapan (Pagi)", "Makan Siang", "Makan Malam", "Cemilan" };
        cmbKategori = new JComboBox<>(opsi);
        cmbKategori.setFont(fontInput);
        form.add(cmbKategori, gbc);

        // --- Baris 3: Langkah Pembuatan (UI DIPERBAIKI: KOTAK BESAR) ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblLangkah = createLabel("Langkah Pembuatan:");
        // PENTING: Set alignment ke atas agar label sejajar dengan bagian atas kotak
        // teks
        lblLangkah.setVerticalAlignment(JLabel.TOP);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblLangkah, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;

        // Membuat JTextArea dengan baris awal 6
        inpLangkah = new JTextArea(6, 20);
        inpLangkah.setFont(fontInput);
        inpLangkah.setLineWrap(true); // Auto wrap text
        inpLangkah.setWrapStyleWord(true);
        inpLangkah.setToolTipText("Tekan Enter untuk membuat langkah baru");

        // Membungkus dengan ScrollPane
        JScrollPane scrollLangkah = new JScrollPane(inpLangkah);
        // PENTING: Memaksa ukuran agar tetap besar (seperti Gambar 3)
        scrollLangkah.setPreferredSize(new Dimension(scrollLangkah.getPreferredSize().width, 120));
        form.add(scrollLangkah, gbc);

        // --- SEPARATOR (Garis Pemisah) ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        form.add(sep, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // --- Baris 4: Input Bahan ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(createLabel("Tambah Bahan:"), gbc);

        // Panel Container Bahan
        JPanel panelBahan = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBahan.setBackground(Color.WHITE);

        inpBahan = new JTextField();
        panelBahan.add(createInputGroup("Nama Bahan", inpBahan)); // Helper method
        inpKalori = new JTextField();
        panelBahan.add(createInputGroup("Kalori (Angka)", inpKalori)); // Helper method

        gbc.gridx = 1;
        gbc.gridy = 4;
        form.add(panelBahan, gbc);

        // --- Baris 5: Tombol Tambah Bahan ---
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton btnAddBahan = new JButton("+ Tambahkan ke List");
        styleButton(btnAddBahan, new Color(52, 152, 219)); // Biru Muda
        form.add(btnAddBahan, gbc);

        // --- Baris 6: Preview List Bahan (UI DIPERBAIKI: KOTAK BESAR) ---
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 0.2; // Memberi sedikit bobot vertikal agar tidak gepeng
        JLabel lblPreview = createLabel("Preview Bahan:");
        lblPreview.setVerticalAlignment(JLabel.TOP); // Label di pojok kiri atas
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblPreview, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH; // PENTING: Mengisi horizontal dan vertikal
        gbc.anchor = GridBagConstraints.WEST;

        // Membuat JTextArea Read-Only
        displayBahanSementara = new JTextArea(5, 20);
        displayBahanSementara.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayBahanSementara.setEditable(false); // Tidak bisa diedit manual
        displayBahanSementara.setBackground(new Color(245, 245, 245)); // Warna abu muda

        // Membungkus dengan ScrollPane
        JScrollPane scrollPreview = new JScrollPane(displayBahanSementara);
        // PENTING: Memaksa ukuran agar tetap besar (seperti Gambar 4)
        scrollPreview.setMinimumSize(new Dimension(200, 100));
        form.add(scrollPreview, gbc);

        // --- Baris 7: Tombol Simpan Utama ---
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton btnSimpan = new JButton("SIMPAN RESEP KE DASHBOARD");
        styleButton(btnSimpan, new Color(39, 174, 96)); // Hijau
        btnSimpan.setPreferredSize(new Dimension(200, 45));
        form.add(btnSimpan, gbc);

        panel.add(form, BorderLayout.CENTER);

        // C. FOOTER
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Tombol Keluar
        JButton btnKeluar = new JButton("Keluar Aplikasi");
        styleButton(btnKeluar, new Color(192, 57, 43));
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
                System.exit(0);
        });

        // Tombol Lihat Tabel
        JButton btnNext = new JButton("Lihat Dashboard >>");
        styleButton(btnNext, Color.GRAY);
        btnNext.addActionListener(e -> {
            refreshTable();
            card.show(mainPanel, "output");
        });

        footer.add(btnKeluar, BorderLayout.WEST);
        footer.add(btnNext, BorderLayout.EAST);
        panel.add(footer, BorderLayout.SOUTH);

        // --- D. ACTION LISTENERS (LOGIKA) ---

        // 1. Logic Tombol Tambah Bahan (Update UI Preview)
        btnAddBahan.addActionListener(e -> {
            try {
                String nm = inpBahan.getText();
                String k = inpKalori.getText();
                if (nm.isEmpty() || k.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama bahan dan kalori harus diisi!");
                    return;
                }

                double val = Double.parseDouble(k); // Validasi angka
                tempBahanList.add(new String[] { nm, String.valueOf(val) });

                // Menambahkan teks ke area Preview (Kotak Besar)
                displayBahanSementara.append("• " + nm + " (" + val + " kkal)\n");

                inpBahan.setText("");
                inpKalori.setText("");
                inpBahan.requestFocus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Kalori harus berupa angka!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Logic Tombol Simpan Resep
        btnSimpan.addActionListener(e -> {
            String nm = inpNamaResep.getText();
            String kat = (String) cmbKategori.getSelectedItem();

            if (cmbKategori.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Pilih Kategori dulu!");
                return;
            }

            try {
                Recipe r = new Recipe(nm, kat);

                // Masukkan Bahan dari list sementara
                for (String[] b : tempBahanList) {
                    r.tambahBahan(b[0], Double.parseDouble(b[1]));
                }

                // Masukkan Langkah (Ambil dari JTextArea Multi-line)
                String langkahRaw = inpLangkah.getText();
                if (!langkahRaw.trim().isEmpty()) {
                    // Pisahkan berdasarkan baris baru (Enter)
                    String[] stepsArray = langkahRaw.split("\n");
                    for (String s : stepsArray) {
                        if (!s.trim().isEmpty())
                            r.tambahLangkah(s.trim());
                    }
                }

                if (indexEdit >= 0) {
                    manager.updateResep(indexEdit, r);
                    JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
                    indexEdit = -1;
                } else {
                    manager.tambahResep(r);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                }

                resetForm();

            } catch (InputKosongException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error sistem: " + ex.getMessage());
            }
        });

        return panel;
    }

    // ==========================================
    // BAGIAN 2: PANEL TABEL (Dashboard Output)
    // ==========================================
    private JPanel buatTabelPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel lbl = new JLabel("DASHBOARD RESEP & NUTRISI");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        panel.add(header, BorderLayout.NORTH);

        // Setup Kolom Tabel (Termasuk Langkah Pembuatan)
        String[] cols = { "Nama Resep", "Kategori", "Langkah Pembuatan", "Bahan & Kalori", "Total Kalori", "Status" };
        tableModel = new DefaultTableModel(cols, 0);
        tabelOutput = new JTable(tableModel);

        // Styling Tabel
        tabelOutput.setRowHeight(40); // Tinggi baris agar teks panjang muat
        tabelOutput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelOutput.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelOutput.getTableHeader().setBackground(new Color(230, 230, 230));

        // Lebar Kolom
        tabelOutput.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelOutput.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelOutput.getColumnModel().getColumn(2).setPreferredWidth(300); // Langkah lebih lebar
        tabelOutput.getColumnModel().getColumn(3).setPreferredWidth(250);
        tabelOutput.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabelOutput.getColumnModel().getColumn(5).setPreferredWidth(100);

        // Center Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelOutput.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        panel.add(new JScrollPane(tabelOutput), BorderLayout.CENTER);

        // Footer Tombol Tabel
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("<< Kembali Input");
        JButton btnEdit = new JButton("Edit Data");
        JButton btnDel = new JButton("Hapus Data Terpilih");

        styleButton(btnBack, Color.GRAY);
        styleButton(btnEdit, new Color(241, 196, 15)); // button edit kuning 
        styleButton(btnDel, new Color(192, 57, 43));

        btnBack.addActionListener(e -> card.show(mainPanel, "form"));
        btnDel.addActionListener(e -> {
            int row = tabelOutput.getSelectedRow();
            if (row >= 0) {
                manager.hapusResep(row);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
            }
        });

        // fungsi button edit (beckend)
        btnEdit.addActionListener(e -> {
            int row = tabelOutput.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
                return;
            }

            Recipe r = manager.getAllResep().get(row);

            // === ISI FORM ===
            inpNamaResep.setText(r.getNama());
            cmbKategori.setSelectedItem(r.getKategori());

            // Langkah
            inpLangkah.setText("");
            for (String s : r.getLangkahPembuatan()) {
                inpLangkah.append(s + "\n");
            }

            // Bahan
            tempBahanList.clear();
            displayBahanSementara.setText("");
            for (Ingredient i : r.getListBahan()) {
                tempBahanList.add(new String[] {
                        i.getNamaBahan(),
                        String.valueOf(i.getKalori())
                });
                displayBahanSementara.append(
                        "• " + i.getNamaBahan() + " (" + i.getKalori() + " kkal)\n");
            }

            indexEdit = row; // AKTIFKAN MODE EDIT
            card.show(mainPanel, "form");
        });

        footer.add(btnBack);
        footer.add(btnEdit); //menambahkan tombol edit
        footer.add(btnDel);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // UTILITIES / HELPER METHODS
    // ==========================================

    // Membuat grup input (Label kecil di atas field)
    private JPanel createInputGroup(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));
        styleField(field);

        // Padding text field
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(), BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleField(JTextField tf) {
        tf.setFont(fontInput);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 30));
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(fontBold);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontLabel);
        return lbl;
    }

    private void resetForm() {
        inpNamaResep.setText("");
        cmbKategori.setSelectedIndex(0);
        inpLangkah.setText("");
        inpBahan.setText("");
        inpKalori.setText("");
        displayBahanSementara.setText("");
        tempBahanList.clear();
        indexEdit = -1; //beckend
    }

    // Refresh Tabel dengan data terbaru
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Recipe r : manager.getAllResep()) {

            // Format Bahan jadi String
            StringBuilder sbBahan = new StringBuilder();
            for (Ingredient i : r.getListBahan())
                sbBahan.append(i.getNamaBahan()).append(" (").append(i.getKalori()).append("), ");
            String strBahan = sbBahan.length() > 2 ? sbBahan.substring(0, sbBahan.length() - 2) : "-";

            // Format Langkah jadi String Berurutan (1. ... 2. ...)
            StringBuilder sbLangkah = new StringBuilder();
            int no = 1;
            for (String s : r.getLangkahPembuatan()) {
                sbLangkah.append(no++).append(". ").append(s).append("  ");
            }
            String strLangkah = sbLangkah.toString();
            if (strLangkah.isEmpty())
                strLangkah = "Tidak ada langkah";

            // Tambah Baris
            tableModel.addRow(new Object[] {
                    r.getNama(),
                    r.getKategori(),
                    strLangkah,
                    strBahan,
                    r.hitungTotalKalori(),
                    r.cekStatusKesehatan()
            });
        }
    }
}