package com.GP.JDBS_implementator;

import java.util.ArrayList;

/**
 * Created by user on 10/24/16.
 */
public interface IJDBCImplementor {
    boolean isAdmin(String user);
    ArrayList<FileEntry> getTheFileList();
    boolean commitToDB(String category ,String path, String name );
    boolean authenticatedUser(String user,String password);
    boolean isInDB(String path);
    boolean deleteToDB(String path , String user);
    boolean categoryExists(String category);

}
