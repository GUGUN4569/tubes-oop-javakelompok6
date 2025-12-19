package com.kelompok.resep.view;

import javax.swing.*;

public class Login extends JFrame {
    // 1. Deklarasi variabel
    private JTextField inpUser;
    private JPasswordField inpPass;
    private JButton btnLogin;

    public Login() {
        // ... Inisialisasi komponen (setTitle, setSize, dll) ...
        
        // 2. Pasang Listener di dalam Constructor
        btnLogin.addActionListener(e -> {
            String user = inpUser.getText();
            String pass = new String(inpPass.getPassword());

            if (user.equals("Kelompok6") && pass.equals("123")) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}