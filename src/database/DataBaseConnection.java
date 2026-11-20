
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final String URL =
        "jdbc:mariadb://localhost:3306/car_rental_db";

    private static final String USER = "rental_app";
    private static final String PASSWORD = "RentalApp2026!";

    public static Connection getConnection() {

        try {
            Connection conn = DriverManager.getConnection(
                URL, USER, PASSWORD
            );

            System.out.println("Database connected successfully!");

            return conn;

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

            return null;
        }
    }
}
