package com.kelompok.resep.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout card;
    private JPanel mainPanel;

    private JTextField inpNamaResep, inpTipe, inpBahan, inpKalori;
    private JTextArea inpLangkah, outputArea;

    public MainFrame() {

        setTitle("Kelompok 6 - Aplikasi Resep Makanan & Nutrisi");
        setSize(650, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(formResep(), "form");
        mainPanel.add(outputPanel(), "output");

        add(mainPanel);
    }

    // FORM INPUT    
    private JPanel formResep() {

        JPanel panel = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new GridLayout(2, 1));
        JLabel judul = new JLabel("Form Input Resep - Kelompok 6", SwingConstants.CENTER);
        judul.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel anggota = new JLabel(
                "Anggota: GUGUN | REZKI | FAKHRI",
                SwingConstants.CENTER
        );

        header.add(judul);
        header.add(anggota);

        panel.add(header, BorderLayout.NORTH);

        // FORM
        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inpNamaResep = new JTextField();
        inpTipe = new JTextField();
        inpBahan = new JTextField();
        inpKalori = new JTextField();
        inpLangkah = new JTextArea(3, 20);

        JButton btnTambahBahan = new JButton("Tambah Bahan");
        JButton btnTambahLangkah = new JButton("Tambah Langkah");
        JButton btnSimpan = new JButton("Simpan Resep");

        form.add(new JLabel("Nama Resep:"));
        form.add(inpNamaResep);

        form.add(new JLabel("Tipe Masakan:"));
        form.add(inpTipe);

        form.add(new JLabel("Nama Bahan:"));
        form.add(inpBahan);

        form.add(new JLabel("Kalori Bahan:"));
        form.add(inpKalori);

        form.add(new JLabel(""));
        form.add(btnTambahBahan);

        form.add(new JLabel("Langkah Memasak:"));
        form.add(new JScrollPane(inpLangkah));

        form.add(new JLabel(""));
        form.add(btnTambahLangkah);

        panel.add(form, BorderLayout.CENTER);

        // FOOTER
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnExit = new JButton("EXIT");
        JButton btnNext = new JButton("LIHAT OUTPUT");

        btnExit.addActionListener(e -> System.exit(0));
        btnNext.addActionListener(e -> card.show(mainPanel, "output"));

        footer.add(btnNext);
        footer.add(btnExit);

        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // =====================================================
    // OUTPUT PANEL

    private JPanel outputPanel() {

 = new JPanel(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setText(
                "=== OUTPUT RESEP ===\n\n" +
                "Nama Resep   : \n" +
                "Tipe Masakan : \n\n" +
                "Bahan:\n" +
                "- \n\n" +
                "Langkah:\n" +
                "- "
        );

        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> card.show(mainPanel, "form"));

        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        panel.add(btnBack, BorderLayout.SOUTH);

        return panel;
    }


    // MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
