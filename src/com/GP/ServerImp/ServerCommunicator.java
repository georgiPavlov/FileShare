package com.GP.ServerImp;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by user on 10/23/16.
 */
public class ServerCommunicator implements IServerCommunicator{
    private DataOutputStream out;
    private  DataInputStream in;
    private boolean isAdmin ;

    public ServerCommunicator(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }

    public void  startCommucateWithConsumer() throws IOException  {

        File file = new File("/home/user/FileShare/files/videos/The.Flash.2014.S03E02.XviD-AFG.avi");
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
        System.out.println("upload finished");




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
}
