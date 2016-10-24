package com.GP.JDBS_implementator;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;

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
            String sql1 = "select isAdmin from registration where user=" + user ;
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            //rs.next();
            isAdminBool = rs.getBoolean("is_admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdminBool;
    }

    @Override
    public ArrayList<FileEntry> getTheFileList() {
        ArrayList<FileEntry> entries = new FinalArrayList();
        Statement stmt = null;
        try {
            stmt = (Statement) conn.createStatement();
            String sql1 = "select * from files JOIN  categories ON " +
                    "files.categoriesID = categories.id";
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);

            while (rs.next()){
                String path = rs.getString("path");
                String category = rs.getString("name");
                entries.add(new FileEntry(path,category));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //to do my custom exeptions
        return entries;
    }

    @Override
    public boolean commitToDB(String category, String path , String name) {
        if(isInDB(path) && isAdmin(name)){

            Statement stmt ;
            try {
                stmt = (Statement) conn.createStatement();
                if(categoryExists(category)){
                    String sql = "INSERT INTO categories " +
                         "VALUES ("+ category +", 'basic description')";
                    stmt.executeUpdate(sql);
                }
                String sqlIndex = "select * from categories where name="+ category  ;
                ResultSet rs = (ResultSet) stmt.executeQuery(sqlIndex);
                int index = rs.getInt("id");
                        String sql1 = "INSERT INTO files " +
                                "VALUES ("+ path +", " + index + ")";
                stmt.executeUpdate(sql1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean authenticatedUser(String user, String password) {
        Statement stmt ;
        try {
            stmt = (Statement) conn.createStatement();
            String sql = "select * from registrations where name = "+ user + " AND password= "+ password ;
            ResultSet rs = (ResultSet) stmt.executeQuery(sql);
            if (!rs.isBeforeFirst() ) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return true;
    }

    @Override
    public boolean isInDB(String path) {
        Statement stmt ;
        try {
            stmt = (Statement) conn.createStatement();
            String sql1 = "select * from files WHERE  files.path = " + path ;
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            if (!rs.isBeforeFirst() ) {
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteToDB(String path, String user) {

        if(isInDB(path) && isAdmin(user)){
            Statement stmt ;
            try {
                stmt = (Statement) conn.createStatement();
                String sql1 = "DELETE FROM files " +
                        "WHERE path = " + path ;
                stmt.executeUpdate(sql1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean categoryExists(String category) {
        Statement stmt ;
        try {
            stmt = (Statement) conn.createStatement();
            String sql1 = "select * from categories WHERE  categories.name = " + category ;
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            if (!rs.isBeforeFirst() ) {
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
}
