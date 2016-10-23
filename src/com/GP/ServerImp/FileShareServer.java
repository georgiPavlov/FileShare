package com.GP.ServerImp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by georgipavlov on 13.12.15.
 */
public class FileShareServer implements Runnable {
   private Socket socket;
    private Scanner scanner;
    private DataOutputStream out;
    private  DataInputStream in;


    public FileShareServer(Socket socket){
        this.socket=socket;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             in =
                    new DataInputStream(
                           socket.getInputStream());
             out =
                    new DataOutputStream(
                            socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        System.out.println("start");
        ServerCommunicator com = new ServerCommunicator(out,in);
        try {
            com.startCommucateWithConsumer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeStreams();
        scanner.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeStreams(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}