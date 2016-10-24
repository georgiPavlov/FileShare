package com.GP.JDBS_implementator;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by user on 10/23/16.
 */
public class JDBC_ImplementatorExecutor extends JDBC_Connection implements IJDBCImplementor {


    @Override
    public boolean isAdmin(String user) {
        Statement stmt = null;
        boolean isAdminBool   = false;
        try {
            stmt = (Statement) conn.createStatement();
            String sql1 = "select isAdmin from registration where user=" + user + ";";
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            isAdminBool = rs.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdminBool;
    }

    @Override
    public ArrayList<FileEntry> getTheFileList() {
        return null;
    }

    @Override
    public boolean commitToDB(String category, String path) {
        return false;
    }

    @Override
    public boolean authenticatedUser(String user, String password) {
        return false;
    }

    @Override
    public boolean isInDB(String path) {
        return false;
    }
}
