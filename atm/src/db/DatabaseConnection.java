package com.atm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = System.getenv("ATM_DB_URL") != null
        ? System.getenv("ATM_DB_URL")
        : "jdbc:mysql://localhost:3306/atm_db";
    private static final String USER = System.getenv("ATM_DB_USER") != null
        ? System.getenv("ATM_DB_USER")
        : "root";
    private static final String PASSWORD = System.getenv("ATM_DB_PASSWORD") != null
        ? System.getenv("ATM_DB_PASSWORD")
        : "hello";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}