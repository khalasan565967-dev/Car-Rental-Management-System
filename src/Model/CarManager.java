package Model;

import database.DataBaseConnection;
import java.sql.*;

public class CarManager {

    // 1. ADD CAR
    public boolean addCar(String brand, String model,
            String plateNumber, String carType,
            int year, double dailyRate) {

        String sql = "INSERT INTO cars "
                + "(brand, model, plate_number, car_type, year, daily_rate) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brand);
            pst.setString(2, model);
            pst.setString(3, plateNumber);
            pst.setString(4, carType);
            pst.setInt(5, year);
            pst.setDouble(6, dailyRate);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. UPDATE CAR
    public boolean updateCar(int carId, String brand,
            String model, String plateNumber,
            String carType, int year, double dailyRate) {

        String sql = "UPDATE cars SET brand=?, model=?, "
                + "plate_number=?, car_type=?, year=?, daily_rate=? "
                + "WHERE car_id=?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brand);
            pst.setString(2, model);
            pst.setString(3, plateNumber);
            pst.setString(4, carType);
            pst.setInt(5, year);
            pst.setDouble(6, dailyRate);
            pst.setInt(7, carId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. DELETE CAR
    public boolean deleteCar(int carId) {

        String sql = "DELETE FROM cars WHERE car_id=?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, carId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DISPLAY ALL CARS
    public ResultSet getAllCars() throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pst =
                conn.prepareStatement("SELECT * FROM cars");

        return pst.executeQuery();
    }

    // 5. SEARCH CAR
    public ResultSet searchCar(String keyword) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        String sql = "SELECT * FROM cars WHERE brand LIKE ? "
                + "OR model LIKE ? OR plate_number LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");

        return pst.executeQuery();
    }

    // 6. UPDATE CAR STATUS
    public boolean updateCarStatus(int carId, String status) {

        String sql = "UPDATE cars SET status=? WHERE car_id=?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, status);
            pst.setInt(2, carId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. COUNT ALL CARS
    public int countAllCars() {

        String sql = "SELECT COUNT(*) FROM cars";

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

    // 8. COUNT CARS BY STATUS
    public int countCarsByStatus(String status) {

        String sql = "SELECT COUNT(*) FROM cars WHERE status = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn == null) return 0;

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, status);

                try (ResultSet rs = pst.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}