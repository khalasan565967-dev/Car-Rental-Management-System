package Model;

import database.DataBaseConnection;
import java.sql.*;

public class CustomerManager {

    // 1. ADD CUSTOMER
    public boolean addCustomer(String fullName, String contactNumber,
            String email, String address, String licenseNumber) {

        String sql = "INSERT INTO customers "
                + "(full_name, contact_number, email, address, license_number) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, fullName);
            pst.setString(2, contactNumber);
            pst.setString(3, email);
            pst.setString(4, address);
            pst.setString(5, licenseNumber);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. UPDATE CUSTOMER
    public boolean updateCustomer(int customerId, String fullName,
            String contactNumber, String email,
            String address, String licenseNumber) {

        String sql = "UPDATE customers SET full_name=?, "
                + "contact_number=?, email=?, address=?, "
                + "license_number=? WHERE customer_id=?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, fullName);
            pst.setString(2, contactNumber);
            pst.setString(3, email);
            pst.setString(4, address);
            pst.setString(5, licenseNumber);
            pst.setInt(6, customerId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. DELETE CUSTOMER
    public boolean deleteCustomer(int customerId) {

        String sql = "DELETE FROM customers WHERE customer_id=?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, customerId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DISPLAY ALL CUSTOMERS
    public ResultSet getAllCustomers() throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        PreparedStatement pst =
                conn.prepareStatement("SELECT * FROM customers");

        return pst.executeQuery();
    }

    // 5. SEARCH CUSTOMER
    public ResultSet searchCustomer(String keyword) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        String sql = "SELECT * FROM customers "
                + "WHERE full_name LIKE ? "
                + "OR contact_number LIKE ? "
                + "OR license_number LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");

        return pst.executeQuery();
    }

    // 6. COUNT ALL CUSTOMERS
    public int countAllCustomers() {

        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return 0;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}