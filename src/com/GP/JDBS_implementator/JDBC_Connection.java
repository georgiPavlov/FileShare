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
            System.out.println("creating connection");
            Driver d = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(d);
            conn  = (com.mysql.jdbc.Connection) DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connection is up");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void destroyConnection() {
        try {
            System.out.println("connection is down");
            conn.close();
            System.out.println("bye bye have a nice day! :)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
