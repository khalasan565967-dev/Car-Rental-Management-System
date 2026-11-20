
package View;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import Controller.LoginController;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnRegister;

    private LoginController loginController;

    public LoginView() {

        loginController = new LoginController();

        setTitle("Car Rental System - Login");
        setSize(600, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 55, 57));
        panel.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Car Rental System");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        lblTitle.setBounds(150, 20, 300, 25);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        JLabel lblSubtitle = new JLabel(
                "Vehicle Rental Management System");
        lblSubtitle.setForeground(Color.LIGHT_GRAY);
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 10));
        lblSubtitle.setBounds(150, 43, 300, 15);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblSubtitle);

        JSeparator separator = new JSeparator();
        separator.setBounds(35, 70, 515, 1);
        panel.add(separator);

        // Welcome
        JLabel lblWelcome = new JLabel("WELCOME");
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 15));
        lblWelcome.setBounds(200, 85, 200, 25);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblWelcome);

        JLabel lblMessage = new JLabel(
                "Please log in to access your account");
        lblMessage.setForeground(Color.LIGHT_GRAY);
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 11));
        lblMessage.setBounds(150, 110, 300, 20);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblMessage);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(65, 150, 200, 20);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(65, 170, 470, 32);
        panel.add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(65, 215, 200, 20);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(65, 235, 470, 32);
        panel.add(txtPassword);

        // Login Button
        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(65, 290, 225, 35);
        panel.add(btnLogin);

        // Register Button
        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(310, 290, 225, 35);
        panel.add(btnRegister);

        // Login Action
        btnLogin.addActionListener(e -> loginAccount());

        // Register Action
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterView();
        });

        txtPassword.addActionListener(e -> loginAccount());

        add(panel);
        setVisible(true);
    }

    private void loginAccount() {

        String username = txtUsername.getText().trim();
        String password =
                new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username and password.");
            return;
        }

        try {

            boolean valid = loginController.login(
                    username, password);

            if (valid) {

                JOptionPane.showMessageDialog(this,
                        "Login successful!");

                dispose();
                new DashboardView().setVisible(true);

            } else {

                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.");
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage());

            ex.printStackTrace();
        }
    }
}
