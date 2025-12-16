package com.hbk.fast_food.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "fastfoodmanagement_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "131104";
    
    private static final String DB_URL=String.format("jdbc:mysql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);

    private static final String DB_DRIVER="com.mysql.cj.jdbc.Driver";

    private static final ThreadLocal<Connection> threadLocalConnection=new ThreadLocal<>();

    // GET Connection

    public static Connection getConnection() throws SQLException{
        Connection conn=threadLocalConnection.get();
        try {
            Class.forName(DB_DRIVER);
            // isValid(2): is the server still there? (wait max 2s)
            if(conn == null || conn.isClosed() || !conn.isValid(2))
            conn=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            threadLocalConnection.set(conn);
            
            return conn;
            
        } catch (ClassNotFoundException e) {

            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Please add mysql-connector-j to dependencies");
            throw new SQLException("JDBC Driver not found: " + e.getMessage());

        } catch (SQLException e) {

            System.err.println("Database connection failed!");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            System.err.println("Error: " + e.getMessage());
            throw e;

        }
    }

    // CLOSE Collection
    public static void closeConnection() {
        Connection conn=threadLocalConnection.get();
        if(conn!=null) {
            try{
                if(!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            } finally {
                threadLocalConnection.remove();
            }
        }
    } 

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing statement: " + e.getMessage());
        }
    }
}
