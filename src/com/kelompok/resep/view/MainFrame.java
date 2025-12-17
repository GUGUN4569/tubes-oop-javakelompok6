package com.kelompok.resep.view;

//membuat kelompok import di file
import com.kelompok.resep.logic.RecipeManager;
import com.kelompok.resep.model.InputKosongException; // PENTING: Import Exception
import com.kelompok.resep.model.Recipe;
import com.kelompok.resep.model.Recipe.Ingredient;

//membuat import kode java
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
        // 1. TEMA MODERN (NIMBUS)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        manager = new RecipeManager();
        tempBahanList = new ArrayList<>();

        setTitle("Aplikasi Resep & Nutrisi - Kelompok 6 (Ultimate UI)");
        // Ukuran Window
        setSize(1000, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(buatFormPanel(), "form");
        mainPanel.add(buatTabelPanel(), "output");

        add(mainPanel);
    }

    // ==========================================
    // 1. PANEL FORM INPUT
    // ==========================================
    private JPanel buatFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80));
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel judul = new JLabel("FORM INPUT RESEP BARU");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        judul.setForeground(Color.WHITE);
        header.add(judul);
        panel.add(header, BorderLayout.NORTH);

        // --- BODY FORM ---
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

        // Baris 3: Langkah Pembuatan (AREA BESAR)
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
        // Paksa Tinggi 120px
        scrollLangkah.setPreferredSize(new Dimension(scrollLangkah.getPreferredSize().width, 120));
        form.add(scrollLangkah, gbc);

        // SEPARATOR
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        form.add(sep, gbc);
        gbc.gridwidth = 1;

        // Baris 4: Input Bahan
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(createLabel("Tambah Bahan:"), gbc);

        JPanel panelBahan = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBahan.setBackground(Color.WHITE);

        // --- INPUT NAMA BAHAN ---
        inpBahan = new JTextField();
        styleField(inpBahan);
        inpBahan.setBorder(BorderFactory.createTitledBorder("Nama Bahan"));
        inpBahan.setPreferredSize(new Dimension(inpBahan.getPreferredSize().width, 55));

        // --- INPUT KALORI ---
        inpKalori = new JTextField();
        styleField(inpKalori);
        inpKalori.setBorder(BorderFactory.createTitledBorder("Kalori (Angka)"));
        inpKalori.setPreferredSize(new Dimension(inpKalori.getPreferredSize().width, 55));

        panelBahan.add(inpBahan);
        panelBahan.add(inpKalori);

        gbc.gridx = 1; gbc.gridy = 4;
        form.add(panelBahan, gbc);

        // Baris 5: Tombol Tambah Bahan
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnAddBahan = new JButton("+ Tambahkan ke List");
        styleButton(btnAddBahan, new Color(52, 152, 219));
        form.add(btnAddBahan, gbc);

        // Baris 6: Preview List Bahan (AREA BESAR)
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

        // Baris 7: Tombol Simpan
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton btnSimpan = new JButton("SIMPAN RESEP KE DATABASE");
        styleButton(btnSimpan, new Color(39, 174, 96));
        btnSimpan.setPreferredSize(new Dimension(200, 50));
        form.add(btnSimpan, gbc);

        panel.add(form, BorderLayout.CENTER);

        // FOOTER
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        JButton btnNext = new JButton("Lihat Tabel Data >>");
        styleButton(btnNext, Color.GRAY);
        btnNext.addActionListener(e -> { refreshTable(); card.show(mainPanel, "output"); });
        footer.add(btnNext);
        panel.add(footer, BorderLayout.SOUTH);

        // --- LOGIC FORM ---
        // 1. Logic Tambah Bahan ke List Sementara
        btnAddBahan.addActionListener(e -> {
            try {
                String nm = inpBahan.getText();
                String k = inpKalori.getText();
                if(nm.isEmpty() || k.isEmpty()) return;

                double val = Double.parseDouble(k);
                tempBahanList.add(new String[]{nm, String.valueOf(val)});
                displayBahanSementara.append("• " + nm + " (" + val + " kkal)\n");
                inpBahan.setText(""); inpKalori.setText(""); inpBahan.requestFocus();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Kalori harus angka!"); }
        });

        // 2. Logic Simpan Resep (UPDATED SESUAI BACKEND BARU)
        btnSimpan.addActionListener(e -> {
            String nm = inpNamaResep.getText();
            String kat = (String) cmbKategori.getSelectedItem();

            // Validasi Combo Box
            if(cmbKategori.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Pilih Kategori dulu!"); return;
            }

            try {
                // A. Buat Object Recipe (Validasi Nama & Kategori ada di sini -> InputKosongException)
                Recipe r = new Recipe(nm, kat);

                // B. Masukkan Bahan
                for(String[] b : tempBahanList) {
                    r.tambahBahan(b[0], Double.parseDouble(b[1]));
                }

                // C. Masukkan Langkah (Split Text Area per Baris)
                String stepsText = inpLangkah.getText();
                String[] stepsArray = stepsText.split("\n"); // Pisahkan setiap ada Enter
                
                for(String s : stepsArray) {
                    if(!s.trim().isEmpty()) {
                        r.tambahLangkah(s.trim()); // Masukkan ke ArrayList di Backend
                    }
                }

                // D. Simpan ke File Manager
                manager.tambahResep(r);
                
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
                resetForm();

            } catch (InputKosongException ex) {
                // Tangkap Error dari Backend (Sesuai Syarat Tugas)
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error sistem: " + ex.getMessage());
            }
        });

        return panel;
    }

    
    // 2. PANEL TABEL
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

        // Setup Tabel
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
        tabelOutput.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabelOutput.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabelOutput.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Center Align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelOutput.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabelOutput.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        panel.add(new JScrollPane(tabelOutput), BorderLayout.CENTER);

        // Tombol Bawah
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("<< Kembali Input");
        JButton btnDel = new JButton("Hapus Data Terpilih");

        styleButton(btnBack, Color.GRAY);
        styleButton(btnDel, new Color(192, 57, 43)); // Merah

        btnBack.addActionListener(e -> card.show(mainPanel, "form"));
        btnDel.addActionListener(e -> {
            int row = tabelOutput.getSelectedRow();
            if(row >= 0) { manager.hapusResep(row); refreshTable(); }
            else JOptionPane.showMessageDialog(this, "Pilih baris dulu!");
        });

        footer.add(btnBack); footer.add(btnDel);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // UTILITIES    
    private void styleField(JTextField tf) {
        tf.setFont(fontInput);
        // Default tinggi 30, tapi akan ditimpa untuk field yang butuh lebih besar
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
        inpNamaResep.setText(""); cmbKategori.setSelectedIndex(0);
        inpLangkah.setText(""); inpBahan.setText(""); inpKalori.setText("");
        displayBahanSementara.setText(""); tempBahanList.clear();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for(Recipe r : manager.getAllResep()) {
            StringBuilder sb = new StringBuilder();
            for(Ingredient i : r.getListBahan())
                sb.append(i.getNamaBahan()).append(" (").append(i.getKalori()).append("), ");
            String detail = sb.length()>2 ? sb.substring(0, sb.length()-2) : "-";

            tableModel.addRow(new Object[]{
                    r.getNama(), r.getKategori(), detail, r.hitungTotalKalori(), r.cekStatusKesehatan()
            });
        }
    }
}
