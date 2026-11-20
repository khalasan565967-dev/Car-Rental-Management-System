package Model;

import database.DataBaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalManager {

    // CREATE RENTAL
    // Saves the rental AND sets the car to 'Rented' together.
    // Returns false if the car is not available.
    public boolean createRental(int customerId, int carId,
                                Date rentalDate, Date returnDate) {

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return false;

            conn.setAutoCommit(false);

            try {

                // 1. Make sure the car is still available
                String check = "SELECT status FROM cars WHERE car_id = ? FOR UPDATE";

                try (PreparedStatement pst = conn.prepareStatement(check)) {

                    pst.setInt(1, carId);

                    try (ResultSet rs = pst.executeQuery()) {

                        if (!rs.next()
                                || !"Available".equalsIgnoreCase(rs.getString("status"))) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // 2. Insert the rental
                String insert = "INSERT INTO rentals "
                        + "(customer_id, car_id, rental_date, return_date, status) "
                        + "VALUES (?, ?, ?, ?, 'Active')";

                try (PreparedStatement pst = conn.prepareStatement(insert)) {

                    pst.setInt(1, customerId);
                    pst.setInt(2, carId);
                    pst.setDate(3, rentalDate);
                    pst.setDate(4, returnDate);
                    pst.executeUpdate();
                }

                // 3. Mark the car as rented
                String update = "UPDATE cars SET status = 'Rented' WHERE car_id = ?";

                try (PreparedStatement pst = conn.prepareStatement(update)) {

                    pst.setInt(1, carId);
                    pst.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // RETURN CAR
    // Marks the rental 'Completed' AND sets the car back to 'Available'.
    public boolean returnCar(int rentalId) {

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return false;

            conn.setAutoCommit(false);

            try {

                // 1. Find the car of this ACTIVE rental
                int carId;

                String find = "SELECT car_id FROM rentals "
                        + "WHERE rental_id = ? AND status = 'Active' FOR UPDATE";

                try (PreparedStatement pst = conn.prepareStatement(find)) {

                    pst.setInt(1, rentalId);

                    try (ResultSet rs = pst.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        carId = rs.getInt("car_id");
                    }
                }

                // 2. Complete the rental
                String complete = "UPDATE rentals SET status = 'Completed' "
                        + "WHERE rental_id = ?";

                try (PreparedStatement pst = conn.prepareStatement(complete)) {

                    pst.setInt(1, rentalId);
                    pst.executeUpdate();
                }

                // 3. Make the car available again
                String release = "UPDATE cars SET status = 'Available' WHERE car_id = ?";

                try (PreparedStatement pst = conn.prepareStatement(release)) {

                    pst.setInt(1, carId);
                    pst.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET ALL RENTALS (raw table, kept from your original code)
    public ResultSet getAllRentals() throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        PreparedStatement pst =
                conn.prepareStatement("SELECT * FROM rentals");

        return pst.executeQuery();
    }

    // SEARCH RENTAL (raw table, kept from your original code)
    public ResultSet searchRental(String keyword) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        String sql = "SELECT * FROM rentals "
                + "WHERE CAST(rental_id AS CHAR) LIKE ? "
                + "OR CAST(customer_id AS CHAR) LIKE ? "
                + "OR CAST(car_id AS CHAR) LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");

        return pst.executeQuery();
    }

    // ================= ROWS FOR THE SCREENS =================

    // {rental_id, customer name, "Brand Model", rental date, return date,
    //  total amount (Double), status}
    public List<Object[]> getRentalRows() {

        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT r.rental_id, cu.full_name, ca.brand, ca.model, "
                + "r.rental_date, r.return_date, "
                + "ca.daily_rate * GREATEST(COALESCE("
                + "DATEDIFF(r.return_date, r.rental_date), 1), 1) AS total_amount, "
                + "r.status "
                + "FROM rentals r "
                + "LEFT JOIN customers cu ON cu.customer_id = r.customer_id "
                + "LEFT JOIN cars ca ON ca.car_id = r.car_id "
                + "ORDER BY r.rental_id DESC";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return rows;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    String customer = rs.getString("full_name");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");

                    rows.add(new Object[] {
                        rs.getInt("rental_id"),
                        customer == null ? "(deleted)" : customer,
                        brand == null ? "(deleted)" : brand + " " + model,
                        dateText(rs.getDate("rental_date")),
                        dateText(rs.getDate("return_date")),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    // {rental_id, brand, model, plate number, date taken, return date, status}
    public List<Object[]> getActiveRentalRows() {

        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT r.rental_id, ca.brand, ca.model, ca.plate_number, "
                + "r.rental_date, r.return_date, r.status "
                + "FROM rentals r "
                + "LEFT JOIN cars ca ON ca.car_id = r.car_id "
                + "WHERE r.status = 'Active' "
                + "ORDER BY r.rental_id DESC";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return rows;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    rows.add(new Object[] {
                        rs.getInt("rental_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("plate_number"),
                        dateText(rs.getDate("rental_date")),
                        dateText(rs.getDate("return_date")),
                        rs.getString("status")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    // {car_id, brand, model, plate number, daily rate (Double)}
    public List<Object[]> getAvailableCarRows() {

        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT car_id, brand, model, plate_number, daily_rate "
                + "FROM cars WHERE status = 'Available' "
                + "ORDER BY brand, model";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return rows;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    rows.add(new Object[] {
                        rs.getInt("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("plate_number"),
                        rs.getDouble("daily_rate")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    // {customer_id, full_name}
    public List<Object[]> getCustomerRows() {

        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT customer_id, full_name FROM customers "
                + "ORDER BY full_name";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return rows;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    rows.add(new Object[] {
                        rs.getInt("customer_id"),
                        rs.getString("full_name")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    private String dateText(Date date) {
        return date == null ? "" : date.toString();
    }
}