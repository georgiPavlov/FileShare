package com.GP.JDBS_implementator;

import java.util.ArrayList;

/**
 * Created by user on 10/23/16.
 */
public class JDBC_ImplementatorExecutor extends JDBC_Connection implements IJDBCImplementor {


    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public ArrayList<FileEntry> getTheFileList() {
        return null;
    }

    @Override
    public boolean commitToDB() {
        return false;
    }

}
