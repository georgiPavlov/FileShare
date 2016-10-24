package com.GP.ServerImp;

import com.GP.JDBS_implementator.JDBC_ImplementatorExecutor;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 10/23/16.
 */
public class ServerCommunicator implements IServerCommunicator{
    private DataOutputStream out;
    private  DataInputStream in;
    private JDBC_ImplementatorExecutor jdbc;
    private boolean isAdmin ;
    private boolean run = true;
    private static final Logger LOGGER = Logger.getLogger( ServerCommunicator.class.getName() );

    public ServerCommunicator(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
        this.jdbc = new JDBC_ImplementatorExecutor();
    }

    public void  startCommucateWithConsumer() throws IOException  {
        while (run){
        requestMenu();
        }
        //to to close connection db and maybe some custom exeption
        /*File file = new File("/home/user/FileShare/files/videos/The.Flash.2014.S03E02.XviD-AFG.avi");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
          try {
            out.writeUTF(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        byte[] b = new byte[1000];
        System.out.println("Uploading File...");
        while ((count = fis.read(b)) != -1) {
            out.write(b, 0, count);
        }
        System.out.println("upload finished");*/




    }

    private void requestMenu() {
        int request = 0;
        try {
            request = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (request) {
            case 1:
                LOGGER.log(Level.ALL , "login");
                System.out.println("login");
                loginAuthentication();
                break;
            case 2:
                LOGGER.log(Level.ALL , "commit");
                System.out.println("commit");
                commitFromAdmin();
                break;
            case 3:
                LOGGER.log(Level.ALL , "sent");
                System.out.println("sent");
                sendFileToClient();
                break;
            case 4:
                LOGGER.log(Level.ALL , "delete");
                System.out.println("delete");
                deleteFromAdmin();
                break;

            default:
                LOGGER.log(Level.ALL , "exit run");
                System.out.println("exit");
                this.run = false;
                break;
        }

    }


    @Override
    public boolean loginAuthentication() {

        return false;
    }

    @Override
    public boolean commitFromAdmin() {

        return false;
    }

    @Override
    public boolean sendFileToClient() {
        return false;
    }

    @Override
    public boolean deleteFromAdmin() {
        return false;
    }
}
