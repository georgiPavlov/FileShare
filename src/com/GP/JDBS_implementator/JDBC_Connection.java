package com.GP.JDBS_implementator;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by user on 10/24/16.
 */
public class JDBC_Connection implements IJDBC_Connection{
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/file_share";
    protected com.mysql.jdbc.Connection  conn;


    @Override
    public void createConnection() {
        try {
            Driver d = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(d);
            conn  = (com.mysql.jdbc.Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
