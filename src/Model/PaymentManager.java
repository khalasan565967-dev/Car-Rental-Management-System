package Model;

import database.DataBaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class PaymentManager {

    // Add Payment
    public boolean addPayment(int rentalId, double amount,
                              String paymentMethod, String paymentStatus) {

        String sql = "INSERT INTO payments "
                   + "(rental_id, amount, payment_method, payment_status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            stmt.setDouble(2, amount);
            stmt.setString(3, paymentMethod);
            stmt.setString(4, paymentStatus);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to add payment!");
            e.printStackTrace();
            return false;
        }
    }

    // Get All Payments
    public ArrayList<Object[]> getAllPayments() {

        ArrayList<Object[]> payments = new ArrayList<>();

        String sql = "SELECT payments.payment_id, payments.rental_id, "
                   + "customers.full_name, payments.amount, "
                   + "payments.payment_date, payments.payment_method, "
                   + "payments.payment_status "
                   + "FROM payments "
                   + "JOIN rentals ON payments.rental_id = rentals.rental_id "
                   + "JOIN customers ON rentals.customer_id = customers.customer_id "
                   + "ORDER BY payments.payment_id DESC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Object[] row = {
                    rs.getInt("payment_id"),
                    rs.getInt("rental_id"),
                    rs.getString("full_name"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("payment_date"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status")
                };

                payments.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Search Payment
    public ArrayList<Object[]> searchPayment(String keyword) {

        ArrayList<Object[]> payments = new ArrayList<>();

        String sql = "SELECT payments.payment_id, payments.rental_id, "
                   + "customers.full_name, payments.amount, "
                   + "payments.payment_date, payments.payment_method, "
                   + "payments.payment_status "
                   + "FROM payments "
                   + "JOIN rentals ON payments.rental_id = rentals.rental_id "
                   + "JOIN customers ON rentals.customer_id = customers.customer_id "
                   + "WHERE CAST(payments.payment_id AS CHAR) LIKE ? "
                   + "OR customers.full_name LIKE ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Object[] row = {
                        rs.getInt("payment_id"),
                        rs.getInt("rental_id"),
                        rs.getString("full_name"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status")
                    };

                    payments.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Get Payments by Rental ID
    public ArrayList<Object[]> getPaymentsByRental(int rentalId) {

        ArrayList<Object[]> payments = new ArrayList<>();

        String sql = "SELECT payment_id, rental_id, amount, "
                   + "payment_date, payment_method, payment_status "
                   + "FROM payments WHERE rental_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Object[] row = {
                        rs.getInt("payment_id"),
                        rs.getInt("rental_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status")
                    };

                    payments.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Update Payment Status
    public boolean updatePaymentStatus(int paymentId, String status) {

        String sql = "UPDATE payments SET payment_status = ? "
                   + "WHERE payment_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, paymentId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= NEW METHODS =================

    // Update Payment (amount, method and status)
    public boolean updatePayment(int paymentId, double amount,
                                 String paymentMethod, String paymentStatus) {

        String sql = "UPDATE payments SET amount = ?, payment_method = ?, "
                   + "payment_status = ? WHERE payment_id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return false;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDouble(1, amount);
                stmt.setString(2, paymentMethod);
                stmt.setString(3, paymentStatus);
                stmt.setInt(4, paymentId);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Payment
    public boolean deletePayment(int paymentId) {

        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return false;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, paymentId);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Count payments with a given status
    public int countByStatus(String status) {

        String sql = "SELECT COUNT(*) FROM payments WHERE payment_status = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return 0;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, status);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Total amount of payments with a given status
    public double sumAmountByStatus(String status) {

        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments "
                   + "WHERE payment_status = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return 0;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, status);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? rs.getDouble(1) : 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}