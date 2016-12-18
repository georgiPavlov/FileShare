package com.GP.ServerImp;

import com.GP.JDBS_implementator.FileEntry;
import com.GP.JDBS_implementator.JDBC_ImplementatorExecutor;
import com.GP.MultithreadImplementation.IThreadLock;
import com.GP.MultithreadImplementation.ThreadLock;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private IThreadLock lock;
    private static final int  SIZE = 1000;


    public ServerCommunicator(DataOutputStream out, DataInputStream in ) {
        this.out = out;
        this.in = in;
        this.jdbc = new JDBC_ImplementatorExecutor();
        this.lock = new ThreadLock();
        jdbc.createConnection();


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
            System.out.println(request + "current request");
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
            sendTrueForOK();
            isAdmin = jdbc.isAdmin(user);
            sendToClientIfIsAdmin();
            sendEntriesToClientAPP();


        }else {
            sendFalseForNOT_OK();
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

                lock.lockWriteFile(textPath);
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
                byte[] b = new byte[SIZE];
                long fileCount = in.readLong();
                System.out.println("loops " + fileCount);
                System.out.println("Incoming File");
                for(int i = 0; i < fileCount ; i++){
                    in.read(b);
                    fos.write(b, 0, count);
                }
                LOGGER.log(Level.FINE, "file is written");
               // sendTrueForOK();
                fos.close();
                if(!jdbc.isInDB(textPath)){
                jdbc.commitToDB(category , textPath ,USER );
                }
                System.out.println("fos closed");
                lock.unlockWriteFile(textPath);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            //custom unlock
        }else {
            System.out.println("some err");
        }
        sendFalseForNOT_OK();
        return false;
    }

    @Override
    public boolean sendFileToClient() {
        String path = null;
        try {
            path = in.readUTF();
            boolean isInDb = jdbc.isInDB(path);
            out.writeBoolean(isInDb);
            if(!isInDb){
                return false;
            }
            lock.lockReadFile(path);
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
               // out.writeUTF(file.getName());
                int count = 0;
                byte[] b = new byte[SIZE];
                LOGGER.log(Level.FINE, "uploading");
                System.out.println("Uploading File...");
                out.writeLong(getFileLoops(file));
                while ((count = fis.read(b)) != -1) {
                    out.write(b, 0, count);
                }
                String string = "upload finished";
                byte[] bytes = string.getBytes();
               // LOGGER.log(Level.FINE, "upload finished");
                System.out.println("upload finished");
                //sendTrueForOK();
                fis.close();
                lock.unlockReadFile(path);
                //unlock writer
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        LOGGER.log(Level.FINE , "file is not in the db!");
        sendFalseForNOT_OK();
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
        lock.lockWriteFile(textPath);
        if(jdbc.deleteToDB(textPath , USER)){
        Path path = Paths.get(textPath);

        try {
            Files.delete(path);
        } catch (IOException e) {
            LOGGER.log(Level.FINE , "ERROR with deleting file");
            e.printStackTrace();
        }
        LOGGER.log(Level.FINE , "the file with path: " + textPath +  " is deleted");
            sendTrueForOK();
            return true;
        }
        //unlock
        lock.unlockWriteFile(textPath);
        sendFalseForNOT_OK();
        return false;
    }

    @Override
    public void sendEntriesToClientAPP() {
        ArrayList<FileEntry> entries = jdbc.getTheFileList();
        if(entries.isEmpty()){
            //some custom exception maybe
            sendFalseForNOT_OK();
            return;
        }else {
            try {
                out.writeInt(entries.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < entries.size(); i++) {
                try {
                    out.writeUTF(entries.get(i).getCategorie());
                    out.writeUTF(entries.get(i).getName());
                    out.writeUTF(entries.get(i).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void sendToClientIfIsAdmin() {
        try {
            out.writeBoolean(isAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTrueForOK(){
        try {
            out.writeBoolean(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFalseForNOT_OK(){
        try {
            out.writeBoolean(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }

    private long getFileLoops(File file){
        long fileSize = file.length();
        long loopCount = fileSize / SIZE;
        if(fileSize % SIZE != 0){
            loopCount++;
        }
        return loopCount;
    }
}
