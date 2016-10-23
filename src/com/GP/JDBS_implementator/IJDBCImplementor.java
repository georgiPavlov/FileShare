package com.GP.JDBS_implementator;

import java.util.ArrayList;

/**
 * Created by user on 10/24/16.
 */
public interface IJDBCImplementor {
    boolean isAdmin();
    ArrayList<FileEntry> getTheFileList();
    boolean commitToDB();

}
