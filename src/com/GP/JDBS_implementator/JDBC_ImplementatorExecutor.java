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
            System.out.println("db user " + user);
            String sql1 = "select is_admin from registrations where user = '" + user + "'";
            ResultSet rs = (ResultSet) stmt.executeQuery(sql1);
            rs.next();
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
        if(isAdmin(name)){
            System.out.println("starting #dataBase commit");
            System.out.println(category + "#" + path + "#" + name);
            Statement stmt ;
            try {
                stmt = (Statement) conn.createStatement();
                if(categoryExists(category)){
                    System.out.println("category does not exist");
                    String sql = "INSERT INTO categories ( `name`, quick_introduction) " +
                         "VALUES ('"+ category +"', 'basic description')";
                    stmt.executeUpdate(sql);
                }
                String sqlIndex = "select * from categories where name = '"+ category + "'" ;

                ResultSet rs = (ResultSet) stmt.executeQuery(sqlIndex);
                rs.next();
                int index = rs.getInt("id");
                System.out.println("category index #dataBase  + inserting file: " + index);
                        String sql1 = "INSERT INTO files ( `path`, `categoriesID` ) " +
                                "VALUES ('"+ path +"', '" + index + "')";
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
            String sql = "select * from registrations where  user = '"+ user + "' AND password= '"+ password + "'" ;
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
            String sql1 = "select * from files WHERE  files.path = '" + path + "'" ;
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
            System.out.println("starting deleting process #dataBase");
            Statement stmt ;
            try {
                stmt = (Statement) conn.createStatement();
                String sql1 = "DELETE FROM files " +
                        "WHERE path = '" + path  + "'";
                stmt.executeUpdate(sql1);
                System.out.println("#dataBase deleted");

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
            String sql1 = "select * from categories WHERE  categories.name = '" + category + "'";
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
