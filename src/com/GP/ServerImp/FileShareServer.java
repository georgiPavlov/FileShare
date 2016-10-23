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
    private BufferedWriter out;
    private  BufferedReader in;


    public FileShareServer(Socket socket){
        this.socket=socket;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
             out =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        System.out.println("start");
        //startcomication
        ServerCommunicator com = new ServerCommunicator(out,in);
        com.startCommucateWithConsumer();
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