package com.GP.ServerImp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by user on 10/23/16.
 */
public class ServerExecutor {
    public static void main(String[] args) {
        ServerSocket s=null;
        try {
            s= new ServerSocket(6666);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("waiting for connection");
        while(true){
            Socket socket=null;
            try {

                socket = s.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new FileShareServer(socket)).start();

        }
    }
}
