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

    private CardLayout card;
    private JPanel mainPanel;
    private RecipeManager manager;
    private ArrayList<String[]> tempBahanList;

    // Komponen GUI
    private JTextField inpNamaResep, inpBahan, inpKalori;
    private JComboBox<String> cmbKategori;
    private JTextArea inpLangkah, displayBahanSementara;
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
        } catch (Exception e) {}

        // --- 2. INISIALISASI DATA ---
        manager = new RecipeManager();
        tempBahanList = new ArrayList<>();

        // --- 3. SETUP WINDOW UTAMA ---
        setTitle("Aplikasi Resep & Nutrisi - Kelompok 6");
        setSize(1000, 900);
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Baris 1: Nama Resep
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        form.add(createLabel("Nama Resep:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
        inpNamaResep = new JTextField();
        styleField(inpNamaResep);
        form.add(inpNamaResep, gbc);

        // Baris 2: Kategori
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(createLabel("Kategori Makan:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        String[] opsi = {"-- Pilih Kategori --", "Sarapan (Pagi)", "Makan Siang", "Makan Malam", "Cemilan"};
        cmbKategori = new JComboBox<>(opsi);
        cmbKategori.setFont(fontInput);
        form.add(cmbKategori, gbc);

        // Baris 3: Langkah Pembuatan
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblLangkah = createLabel("Langkah Pembuatan:");
        lblLangkah.setVerticalAlignment(JLabel.TOP);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblLangkah, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        inpLangkah = new JTextArea(10, 20);
        inpLangkah.setFont(fontInput);
        inpLangkah.setLineWrap(true);
        inpLangkah.setWrapStyleWord(true);
        JScrollPane scrollLangkah = new JScrollPane(inpLangkah);
        scrollLangkah.setPreferredSize(new Dimension(scrollLangkah.getPreferredSize().width, 120));
        form.add(scrollLangkah, gbc);

        // SEPARATOR (Garis Pemisah)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        form.add(sep, gbc);
        gbc.gridwidth = 1;

        // --- BAGIAN INI YANG DIUBAH AGAR LEGA & RAPI ---
        // Baris 4: Input Bahan
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(createLabel("Tambah Bahan:"), gbc);

        // Panel Container Bahan dengan Gap 15px antar kolom
        JPanel panelBahan = new JPanel(new GridLayout(1, 2, 15, 0)); 
        panelBahan.setBackground(Color.WHITE);

        // 1. Input Nama Bahan (Menggunakan Helper createInputGroup)
        inpBahan = new JTextField();
        // Membungkus input dalam panel wrapper (Label di atas, Input di bawah)
        panelBahan.add(createInputGroup("Nama Bahan", inpBahan));

        // 2. Input Kalori (Menggunakan Helper createInputGroup)
        inpKalori = new JTextField();
        panelBahan.add(createInputGroup("Kalori (Angka)", inpKalori));

        gbc.gridx = 1; gbc.gridy = 4;
        form.add(panelBahan, gbc);
        // ------------------------------------------------

        // Baris 5: Tombol Tambah Bahan (+ Button)
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnAddBahan = new JButton("+ Tambahkan ke List");
        styleButton(btnAddBahan, new Color(52, 152, 219)); // Biru Muda
        form.add(btnAddBahan, gbc);

        // Baris 6: Preview List Bahan (TextArea Read-Only)
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblPreview = createLabel("Preview Bahan:");
        lblPreview.setVerticalAlignment(JLabel.TOP);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblPreview, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        displayBahanSementara = new JTextArea(8, 20);
        displayBahanSementara.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayBahanSementara.setEditable(false);
        displayBahanSementara.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPreview = new JScrollPane(displayBahanSementara);
        scrollPreview.setPreferredSize(new Dimension(scrollPreview.getPreferredSize().width, 100));
        form.add(scrollPreview, gbc);

        // Baris 7: Tombol Simpan Utama
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton btnSimpan = new JButton("SIMPAN RESEP ");
        styleButton(btnSimpan, new Color(39, 174, 96)); // Hijau
        btnSimpan.setPreferredSize(new Dimension(200, 50));
        form.add(btnSimpan, gbc);

        panel.add(form, BorderLayout.CENTER);

        // C. FOOTER (UPDATED: TOMBOL KELUAR & NAVIGASI)
        JPanel footer = new JPanel(new BorderLayout()); 
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Tombol Kiri (KELUAR - Merah)
        JButton btnKeluar = new JButton("Keluar Aplikasi");
        styleButton(btnKeluar, new Color(192, 57, 43)); 
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        // Tombol Kanan (LIHAT TABEL - Abu)
        JButton btnNext = new JButton("Lihat Tabel Data >>");
        styleButton(btnNext, Color.GRAY);
        btnNext.addActionListener(e -> { refreshTable(); card.show(mainPanel, "output"); });

        footer.add(btnKeluar, BorderLayout.WEST);
        footer.add(btnNext, BorderLayout.EAST);

        panel.add(footer, BorderLayout.SOUTH);

        // --- D. ACTION LISTENERS (LOGIKA TOMBOL) ---
        
        // 1. Logic Tambah Bahan Sementara
        btnAddBahan.addActionListener(e -> {
            try {
                String nm = inpBahan.getText();
                String k = inpKalori.getText();
                if(nm.isEmpty() || k.isEmpty()) return; // Cek kosong

                double val = Double.parseDouble(k); // Cek apakah angka
                tempBahanList.add(new String[]{nm, String.valueOf(val)});
                displayBahanSementara.append("• " + nm + " (" + val + " kkal)\n");
                inpBahan.setText(""); inpKalori.setText(""); inpBahan.requestFocus();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Kalori harus berupa angka!"); 
            }
        });

        // 2. Logic Simpan Resep ke Backend
        btnSimpan.addActionListener(e -> {
            String nm = inpNamaResep.getText();
            String kat = (String) cmbKategori.getSelectedItem();

            if(cmbKategori.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Pilih Kategori dulu!"); return;
            }

            try {
                Recipe r = new Recipe(nm, kat);
                // Loop masukkan bahan
                for(String[] b : tempBahanList) {
                    r.tambahBahan(b[0], Double.parseDouble(b[1]));
                }
                // Loop masukkan langkah
                String[] stepsArray = inpLangkah.getText().split("\n");
                for(String s : stepsArray) {
                    if(!s.trim().isEmpty()) r.tambahLangkah(s.trim());
                }
                
                manager.tambahResep(r); // Simpan
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
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
    // BAGIAN 2: PANEL TABEL (Halaman Output)
    // ==========================================
    private JPanel buatTabelPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header Tabel
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(new EmptyBorder(10,0,10,0));
        JLabel lbl = new JLabel("DATABASE RESEP & NUTRISI");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        panel.add(header, BorderLayout.NORTH);

        // Setup Kolom & Model Tabel
        String[] cols = {"Nama Resep", "Kategori", "Rincian Bahan (Komposisi)", "Total Kalori", "Status Gizi"};
        tableModel = new DefaultTableModel(cols, 0);
        tabelOutput = new JTable(tableModel);

        // Styling Tabel
        tabelOutput.setRowHeight(30);
        tabelOutput.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelOutput.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelOutput.getTableHeader().setBackground(new Color(230, 230, 230));

        // Lebar Kolom
        tabelOutput.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelOutput.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelOutput.getColumnModel().getColumn(2).setPreferredWidth(400); // Kolom bahan lebih lebar
        tabelOutput.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabelOutput.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Rata Tengah (Center Alignment)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelOutput.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        panel.add(new JScrollPane(tabelOutput), BorderLayout.CENTER);

        // Tombol Bawah Tabel
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("<< Kembali Input");
        JButton btnDel = new JButton("Hapus Data Terpilih");

        styleButton(btnBack, Color.GRAY);
        styleButton(btnDel, new Color(192, 57, 43)); // Merah

        // Listener Tombol
        btnBack.addActionListener(e -> card.show(mainPanel, "form")); // Pindah ke form
        btnDel.addActionListener(e -> {
            int row = tabelOutput.getSelectedRow();
            if(row >= 0) { 
                manager.hapusResep(row); 
                refreshTable(); 
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu!");
            }
        });

        footer.add(btnBack); footer.add(btnDel);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // BAGIAN 3: UTILITIES & HELPER METHODS
    // ==========================================
    
    // METHOD BARU: MEMBUAT KELOMPOK INPUT YANG RAPI (Label di Atas, Input di Bawah)
    private JPanel createInputGroup(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5)); // Jarak vertikal 5px
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(labelText);
        // Menggunakan font label yang lebih kecil sedikit dan tebal
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        lbl.setForeground(new Color(80, 80, 80)); // Abu tua
        
        styleField(field);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35)); // Tinggi 35px agar lega
        
        // Menambah padding di dalam text field agar teks tidak nempel garis
        field.setBorder(BorderFactory.createCompoundBorder(
            field.getBorder(), 
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // Mengatur gaya font dan ukuran default TextField
    private void styleField(JTextField tf) {
        tf.setFont(fontInput);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 30));
    }

    // Mengatur warna dan gaya Tombol
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(fontBold);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    // Membuat Label dengan font konsisten
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontLabel);
        return lbl;
    }

    // Mengosongkan form setelah simpan
    private void resetForm() {
        inpNamaResep.setText(""); cmbKategori.setSelectedIndex(0);
        inpLangkah.setText(""); inpBahan.setText(""); inpKalori.setText("");
        displayBahanSementara.setText(""); tempBahanList.clear();
    }

    // Mengambil data terbaru dari RecipeManager dan menampilkannya di Tabel
    private void refreshTable() {
        tableModel.setRowCount(0); // Hapus data lama di tabel GUI
        for(Recipe r : manager.getAllResep()) {
            StringBuilder sb = new StringBuilder();
            // Format tampilan bahan menjadi satu string panjang
            for(Ingredient i : r.getListBahan())
                sb.append(i.getNamaBahan()).append(" (").append(i.getKalori()).append("), ");
            
            String detail = sb.length()>2 ? sb.substring(0, sb.length()-2) : "-";

            // Tambah baris ke tabel
            tableModel.addRow(new Object[]{
                    r.getNama(), r.getKategori(), detail, r.hitungTotalKalori(), r.cekStatusKesehatan()
            });
        }
    }
}