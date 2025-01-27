package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://path/your_database_name", "username", "password"); // Ganti path JDBC kamu, nama database, username dan password MySQL kamu
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            // Coba untuk menghubungkan ke database
            Connection connection = getConnection();
            if (connection != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Failed to make connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error in getting the connection.");
            e.printStackTrace();
        }
    }
}

