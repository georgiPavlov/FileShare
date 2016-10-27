import com.mysql.jdbc.*;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.*;
import java.sql.Driver;

/**
 * Created by user on 10/23/16.
 */
public class TestJDBC {
    public static void main(String[] args) {
        try {
            Driver d = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(d);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String URL = "jdbc:mysql://localhost:3306/file_share";
        String USER = "root";
        String PASS = "root";
        try {
          //  Connection conn = DriverManager.getConnection(URL, USER, PASS);
            com.mysql.jdbc.Connection  c = (com.mysql.jdbc.Connection) DriverManager.getConnection(URL, USER, PASS);
            System.out.println("conn ready");
            Statement stmt = null;
            stmt = (Statement) c.createStatement();
            //String sql1 = "select * from categories;";
            String sql1 = "select * from registrations where user  = 'basicUser' " ;
            com.mysql.jdbc.ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            rs.next();
            System.out.println(rs.getBoolean("is_admin"));


        } catch (SQLException e) {
            e.printStackTrace();
        }





    }
}
