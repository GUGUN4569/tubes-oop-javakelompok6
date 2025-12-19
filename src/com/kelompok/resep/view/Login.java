package com.kelompok.resep.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    // Deklarasi Constructor
    private JTextField inpUser;
    private JPasswordField inpPass;
    private JButton btnLogin;

    // Font
    private final Font fontTitle = new Font("Segoe UI", Font.BOLD, 22);
    private final Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

    public Login() {

        // Setting Frame
        setTitle("Login Aplikasi Resep Masakan");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Judul
        JLabel lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(fontTitle);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Panel Form
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(10, 30, 10, 30));

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(fontLabel);

        inpUser = new JTextField();
        inpUser.setFont(fontInput);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(fontLabel);

        inpPass = new JPasswordField();
        inpPass.setFont(fontInput);

        panelForm.add(lblUser);
        panelForm.add(inpUser);
        panelForm.add(lblPass);
        panelForm.add(inpPass);

        add(panelForm, BorderLayout.CENTER);

        // Panel Tombol
        JPanel panelButton = new JPanel();
        btnLogin = new JButton("Login");
        btnLogin.setFont(fontLabel);
        panelButton.add(btnLogin);
        add(panelButton, BorderLayout.SOUTH);

        // Action Listener Login
        btnLogin.addActionListener(e -> prosesLogin());
    }

    // METHOD LOGIN backend
    private void prosesLogin() {
        String user = inpUser.getText();
        String pass = new String(inpPass.getPassword());

        if (user.equals("Kelompok6") && pass.equals("123")) {
            JOptionPane.showMessageDialog(this, "Login Berhasil!");
            new MainFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Username atau Password salah!",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}
