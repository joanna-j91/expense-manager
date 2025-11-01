package com.expensemanager;

import java.sql.*;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully!");


            String url = "jdbc:mysql://localhost:3306/expense_manager";
            String username = "root";
            String password = "avocadodo";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");


            String sql = "SELECT username, password FROM users WHERE username = 'testuser'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Found user: " + rs.getString("username"));
                System.out.println("Password hash: " + rs.getString("password"));
            } else {
                System.out.println("User 'testuser' not found in database!");
            }

            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver NOT found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}
