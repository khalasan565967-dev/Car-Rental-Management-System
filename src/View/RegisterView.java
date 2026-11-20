
package View;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import Controller.LoginController;

public class RegisterView extends JFrame {

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    private JButton btnRegister;
    private JButton btnBack;

    private LoginController loginController;

    public RegisterView() {

        loginController = new LoginController();

        setTitle("Car Rental Registration System");
        setSize(600, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(29, 29, 29));
        panel.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Car Rental Registration System");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        lblTitle.setBounds(120, 20, 360, 25);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        JLabel lblSubtitle = new JLabel(
                "Vehicle Rental Management System");
        lblSubtitle.setForeground(Color.LIGHT_GRAY);
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 10));
        lblSubtitle.setBounds(120, 43, 360, 15);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblSubtitle);

        JSeparator separator = new JSeparator();
        separator.setBounds(35, 70, 515, 1);
        panel.add(separator);

        // Welcome
        JLabel lblWelcome = new JLabel("Welcome");
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 15));
        lblWelcome.setBounds(200, 85, 200, 25);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblWelcome);

        JLabel lblMessage = new JLabel(
                "If you're new here, please register");
        lblMessage.setForeground(Color.LIGHT_GRAY);
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 11));
        lblMessage.setBounds(150, 110, 300, 20);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblMessage);

        // Full Name
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setForeground(Color.WHITE);
        lblFullName.setBounds(60, 150, 200, 20);
        panel.add(lblFullName);

        txtFullName = new JTextField();
        txtFullName.setBounds(60, 170, 480, 32);
        panel.add(txtFullName);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(60, 210, 200, 20);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(60, 230, 480, 32);
        panel.add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(60, 270, 200, 20);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(60, 290, 480, 32);
        panel.add(txtPassword);

        // Confirm Password
        JLabel lblConfirm = new JLabel("Confirm Password");
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setBounds(60, 330, 200, 20);
        panel.add(lblConfirm);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(60, 350, 480, 32);
        panel.add(txtConfirmPassword);

        // Register Button
        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(60, 405, 225, 35);
        panel.add(btnRegister);

        // Back Button
        btnBack = new JButton("BACK TO LOGIN");
        btnBack.setBounds(315, 405, 225, 35);
        panel.add(btnBack);

        // Button Actions
        btnRegister.addActionListener(e -> registerAccount());

        btnBack.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        add(panel);
        setVisible(true);
    }

    private void registerAccount() {

        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();

        String password =
                new String(txtPassword.getPassword());

        String confirmPassword =
                new String(txtConfirmPassword.getPassword());

        // Empty field validation
        if (fullName.isEmpty() || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.");

            return;
        }

        // Password confirmation
        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!");

            return;
        }

        try {

            boolean registered = loginController.register(
                    fullName,
                    username,
                    password
            );

            if (registered) {

                JOptionPane.showMessageDialog(this,
                        "Account registered successfully!");

                dispose();
                new LoginView();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Username already exists or registration failed.");

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage());

            ex.printStackTrace();
        }
    }
}
