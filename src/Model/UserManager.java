
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import database.DataBaseConnection;

public class UserManager {

    // Hash password
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Check username
    public boolean usernameExists(String username) throws SQLException {

        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) {
                throw new SQLException("Database connection failed.");
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, username);

                try (ResultSet rs = pst.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    // Register user
    public boolean registerUser(String fullName, String username,
                                String password) throws SQLException {

        if (fullName == null || fullName.trim().isEmpty()
                || username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO users "
                + "(full_name, username, password_hash, role) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) {
                throw new SQLException("Database connection failed.");
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, fullName.trim());
                pst.setString(2, username.trim());
                pst.setString(3, hashPassword(password));
                pst.setString(4, "Staff");

                return pst.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false;
            }
            throw e;
        }
    }

    // Login user
    public boolean loginUser(String username, String password)
            throws SQLException {

        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) {
                throw new SQLException("Database connection failed.");
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, username.trim());

                try (ResultSet rs = pst.executeQuery()) {

                    if (rs.next()) {
                        String storedPassword =
                                rs.getString("password_hash");

                        String enteredPassword =
                                hashPassword(password);

                        return storedPassword.equals(enteredPassword);
                    }
                }
            }
        }

        return false;
    }
}
