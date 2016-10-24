package com.GP.ServerImp;

import com.GP.JDBS_implementator.JDBC_ImplementatorExecutor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private boolean isLoginSuccessful = false;
    private String USER;
    private   String PASSWORD;

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
        boolean isLoginOK = false;
        String user = null;
        try {
             user = in.readUTF();
            String password = in.readUTF();
            this.USER = user;
            this.PASSWORD = password;
            isLoginOK  = jdbc.authenticatedUser(user , password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isLoginOK){
            isAdmin = jdbc.isAdmin(user);
        }
        isLoginSuccessful = isLoginOK;
        return isLoginOK;
    }

    @Override
    public boolean commitFromAdmin() { // String path , file data!
        if(isLoginSuccessful && isAdmin){
            //custom lock
            try {
                String textPath = in.readUTF();
                String category = in.readUTF();

                File file = new File(textPath);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                LOGGER.log(Level.FINE, "waiting for file");
                System.out.println("Waiting for File");
                int count = 0;
                byte[] b = new byte[1000];
                System.out.println("Incoming File");
                while((count = in.read(b)) != -1){
                    fos.write(b, 0, count);
                }
                LOGGER.log(Level.FINE, "file is written");
                fos.close();
                jdbc.commitToDB(category , textPath ,USER );
                System.out.println("fos closed");

            } catch (IOException e) {
                e.printStackTrace();
            }
            //custom unlock

        }
        return false;
    }

    @Override
    public boolean sendFileToClient() {
        String path = null;

        try {
            path = in.readUTF();
            if (jdbc.isInDB(path)) {
                //lock writer
                File file = new File(path);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                out.writeUTF(file.getName());
                int count = 0;
                byte[] b = new byte[1000];
                LOGGER.log(Level.FINE, "uploading");
                System.out.println("Uploading File...");

                while ((count = fis.read(b)) != -1) {
                    out.write(b, 0, count);
                }
                LOGGER.log(Level.FINE, "upload finished");
                System.out.println("upload finished");

                fis.close();
                //unlock writer
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        LOGGER.log(Level.FINE , "file is not in the db!");
        return false;
    }

    @Override
    public boolean deleteFromAdmin() {
        String path = null;
        try {
            path = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteFromAdmin(path);
        return deleteFromAdmin(path);
    }

    @Override
    public boolean deleteFromAdmin(String textPath) {
        //lock
        if(jdbc.deleteToDB(textPath , USER)){
        Path path = Paths.get(textPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            LOGGER.log(Level.FINE , "ERROR with deleting file");
            e.printStackTrace();
        }
        LOGGER.log(Level.FINE , "the file with path: " + textPath +  " is deleted");
        return true;
        }
        //unlock
        return false;
    }
}
