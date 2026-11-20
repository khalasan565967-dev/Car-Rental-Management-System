package Controller;

import Model.CustomerManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private CustomerManager customerManager;

    public CustomerController() {
        customerManager = new CustomerManager();
    }

    // Add Customer
    public boolean addCustomer(String fullName, String contactNumber,
                               String email, String address,
                               String licenseNumber) {

        return customerManager.addCustomer(
            fullName, contactNumber, email, address, licenseNumber
        );
    }

    // Update Customer
    public boolean updateCustomer(int customerId, String fullName,
                                  String contactNumber, String email,
                                  String address, String licenseNumber) {

        return customerManager.updateCustomer(
            customerId, fullName, contactNumber,
            email, address, licenseNumber
        );
    }

    // Delete Customer
    public boolean deleteCustomer(int customerId) {
        return customerManager.deleteCustomer(customerId);
    }

    // Get All Customers
    public ResultSet getAllCustomers() {

        try {
            return customerManager.getAllCustomers();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Search Customer
    public ResultSet searchCustomer(String keyword) {

        try {
            return customerManager.searchCustomer(keyword);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= NEW: USED BY CustomerView =================

    // Each row: {customer_id, full_name, contact_number, email, address, license_number}
    public List<Object[]> getAllCustomerRows() {

        List<Object[]> rows = new ArrayList<>();
        Statement st = null;
        Connection con = null;

        try (ResultSet rs = customerManager.getAllCustomers()) {

            st = rs.getStatement();
            con = st.getConnection();

            while (rs.next()) {
                rows.add(new Object[] {
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("contact_number"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("license_number")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(st);
            closeQuietly(con);
        }

        return rows;
    }

    public int getTotalCustomers() {
        return customerManager.countAllCustomers();
    }

    private void closeQuietly(AutoCloseable resource) {
        try {
            if (resource != null) resource.close();
        } catch (Exception ignored) {
        }
    }
}