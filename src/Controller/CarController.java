package Controller;

import Model.CarManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarController {

    private final CarManager manager = new CarManager();

    // Each row: {car_id, brand, model, plate_number, car_type, year, daily_rate, status}
    public List<Object[]> getAllCarRows() {

        List<Object[]> rows = new ArrayList<>();
        Statement st = null;
        Connection con = null;

        try (ResultSet rs = manager.getAllCars()) {

            st = rs.getStatement();
            con = st.getConnection();

            while (rs.next()) {
                rows.add(new Object[] {
                    rs.getInt("car_id"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("plate_number"),
                    rs.getString("car_type"),
                    rs.getInt("year"),
                    rs.getDouble("daily_rate"),
                    rs.getString("status")
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

    public int getTotalCars() {
        return manager.countAllCars();
    }

    public int getAvailableCars() {
        return manager.countCarsByStatus("Available");
    }

    public boolean addCar(String brand, String model, String plateNumber,
            String carType, int year, double dailyRate) {
        return manager.addCar(brand, model, plateNumber, carType, year, dailyRate);
    }

    public boolean updateCar(int carId, String brand, String model,
            String plateNumber, String carType, int year, double dailyRate) {
        return manager.updateCar(carId, brand, model, plateNumber, carType, year, dailyRate);
    }

    public boolean deleteCar(int carId) {
        return manager.deleteCar(carId);
    }

    private void closeQuietly(AutoCloseable resource) {
        try {
            if (resource != null) resource.close();
        } catch (Exception ignored) {
        }
    }
}