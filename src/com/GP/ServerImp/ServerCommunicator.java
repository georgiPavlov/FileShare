package com.GP.ServerImp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by user on 10/23/16.
 */
public class ServerCommunicator {
    private BufferedWriter out;
    private BufferedReader in;

    public ServerCommunicator(BufferedWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void  startCommucateWithConsumer(){
        try {
            out.write("the server is here , no money honey");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
