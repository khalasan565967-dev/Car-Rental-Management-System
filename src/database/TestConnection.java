
package database;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection conn = DataBaseConnection.getConnection();

        if (conn != null) {

            System.out.println("Connection test successful!");

            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Connection test failed!");
        }
    }
}

